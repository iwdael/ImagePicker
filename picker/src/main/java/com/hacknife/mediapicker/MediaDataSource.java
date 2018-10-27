package com.hacknife.mediapicker;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hacknife.mediapicker.bean.ImageFolder;
import com.hacknife.mediapicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 */
public class MediaDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ALL = 0;         //加载所有图片
    public static final int LOADER_CATEGORY = 1;    //分类加载图片
    private final String[] IMAGE_PROJECTION = {
            //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608


    private final String[] VIDEO_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Video.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Video.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Video.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Video.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Video.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Video.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Video.Media.DATE_ADDED,       //图片被添加的时间，long型  1450518608
    };


    private FragmentActivity activity;
    private OnImagesLoadedListener loadedListener;                     //图片加载完成的回调接口
    private String[] projections;
    private Uri uri;
    private int mainDirNameRes;

    public MediaDataSource(FragmentActivity activity, String path, MediaType loadType, OnImagesLoadedListener loadedListener) {
        this.activity = activity;
        this.loadedListener = loadedListener;
        if (loadType == MediaType.IMAGE) {
            projections = IMAGE_PROJECTION;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            mainDirNameRes=R.string.ip_all_images;
        } else {
            projections = VIDEO_PROJECTION;
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            mainDirNameRes=R.string.all_video;
        }
        LoaderManager loaderManager = activity.getSupportLoaderManager();
        if (path == null) {
            loaderManager.initLoader(LOADER_ALL, null, this);//加载所有的图片
        } else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        //扫描所有图片
        if (id == LOADER_ALL)
            cursorLoader = new CursorLoader(activity, uri, projections, null, null, projections[6] + " DESC");
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY)
            cursorLoader = new CursorLoader(activity, uri, projections, projections[1] + " like '%" + args.getString("path") + "%'", null, projections[6] + " DESC");

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<ImageFolder> imageFolders = new ArrayList<>();
        if (data != null) {
            ArrayList<ImageItem> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                //查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(projections[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(projections[1]));

                File file = new File(imagePath);
                if (!file.exists() || file.length() <= 0) {
                    continue;
                }

                long imageSize = data.getLong(data.getColumnIndexOrThrow(projections[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(projections[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(projections[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(projections[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(projections[6]));
                //封装实体
                ImageItem imageItem = new ImageItem();
                imageItem.name = imageName;
                imageItem.path = imagePath;
                imageItem.size = imageSize;
                imageItem.width = imageWidth;
                imageItem.height = imageHeight;
                imageItem.mimeType = imageMimeType;
                imageItem.addTime = imageAddTime;
                allImages.add(imageItem);
                //根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();

                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.cover = imageItem;
                    imageFolder.images = images;
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(imageItem);
                }
            }
            //防止没有图片报异常
            if (data.getCount() > 0 && allImages.size() > 0) {
                //构造所有图片的集合
                ImageFolder allImagesFolder = new ImageFolder();
                allImagesFolder.name = activity.getResources().getString(mainDirNameRes);
                allImagesFolder.path = "/";
                allImagesFolder.cover = allImages.get(0);
                allImagesFolder.images = allImages;
                imageFolders.add(0, allImagesFolder);  //确保第一条是所有图片
            }
        }

        if (imageFolders.size() == 0) return;
        //回调接口，通知图片数据准备完成
        MediaPicker.getInstance().imageFolders(imageFolders);
        loadedListener.onImagesLoaded(imageFolders);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<ImageFolder> imageFolders);
    }
}
