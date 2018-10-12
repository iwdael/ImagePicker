package com.hacknife.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;

import com.hacknife.imagepicker.bean.ImageFolder;
import com.hacknife.imagepicker.bean.ImageItem;
import com.hacknife.imagepicker.loader.ImageLoader;
import com.hacknife.imagepicker.ui.ImageGridActivity;
import com.hacknife.imagepicker.ui.ImageViewerActivity;
import com.hacknife.imagepicker.util.ProviderUtil;
import com.hacknife.imagepicker.util.Utils;
import com.hacknife.imagepicker.view.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ImagePicker {

    public static final String TAG = ImagePicker.class.getSimpleName();
    public static final int REQUEST_CODE_TAKE = 1001;
    public static final int REQUEST_CODE_CROP = 1002;
    public static final int REQUEST_CODE_PREVIEW = 1003;
    public static final int RESULT_CODE_ITEMS = 1004;
    public static final int RESULT_CODE_BACK = 1005;

    public static final String EXTRA_RESULT_ITEMS = "extra_result_items";
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selected_image_position";
    public static final String EXTRA_IMAGE_ITEMS = "extra_image_items";
    public static final String EXTRA_FROM_ITEMS = "extra_from_items";
    public static final String EXTRA_EXIT_POSITION = "extra_exit_position";

    private static ImagePicker mInstance;
    private boolean multiMode = true;    //图片选择模式
    private int selectLimit = 9;         //最大选择图片数量
    private boolean crop = true;         //裁剪
    private boolean showCamera = true;   //显示相机
    private boolean isSaveRectangle = false;  //裁剪后的图片是否是矩形，否者跟随裁剪框的形状
    private int outPutX = 800;           //裁剪保存宽度
    private int outPutY = 800;           //裁剪保存高度
    private int focusWidth = 280;         //焦点框的宽度
    private int focusHeight = 280;        //焦点框的高度
    private ImageLoader imageLoader;     //图片加载器
    private CropImageView.Style style = CropImageView.Style.RECTANGLE; //裁剪框的形状
    private File cropCacheFolder;
    private File takeImageFile;
    private ArrayList<ImageItem> mSelectedImages = new ArrayList<>();   //选中的图片集合
    private List<ImageFolder> mImageFolders;      //所有的图片文件夹
    private int mCurrentImageFolderPosition = 0;  //当前选中的文件夹位置 0表示所有图片
    private List<OnPictureSelectedListener> mImageSelectedListeners;          // 图片选中的监听回调
    private OnSelectedListener onImageSelectedListener;
    private boolean shareView = true;
    private MediaType loadType = MediaType.IMAGE;

    private List<String> viewerItem;

    private ImagePicker() {
    }

    public static ImagePicker getInstance() {
        if (mInstance == null) {
            synchronized (ImagePicker.class) {
                if (mInstance == null) {
                    mInstance = new ImagePicker();
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /**
     * 扫描图片
     */
    public static void galleryAddPic(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void shareView(boolean shareView) {
        this.shareView = shareView;
    }

    public boolean isShareView() {
        return shareView;
    }


    public boolean isMultiMode() {
        return multiMode;
    }

    public ImagePicker multiMode(boolean multiMode) {
        this.multiMode = multiMode;
        return this;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public ImagePicker selectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
        return this;
    }

    public boolean isCrop() {
        return crop;
    }

    public ImagePicker crop(boolean crop) {
        this.crop = crop;
        return this;
    }

    public ImagePicker loadType(MediaType loadType) {
        this.loadType = loadType;
        return this;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public ImagePicker showCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return this;
    }

    public boolean isSaveRectangle() {
        return isSaveRectangle;
    }

    public ImagePicker saveRectangle(boolean isSaveRectangle) {
        this.isSaveRectangle = isSaveRectangle;
        return this;
    }

    public int getOutPutX() {
        return outPutX;
    }

    public ImagePicker outPutX(int outPutX) {
        this.outPutX = outPutX;
        return this;
    }

    public int getOutPutY() {
        return outPutY;
    }

    public ImagePicker outPutY(int outPutY) {
        this.outPutY = outPutY;
        return this;
    }

    public int getFocusWidth() {
        return focusWidth;
    }

    public ImagePicker focusWidth(int focusWidth) {
        this.focusWidth = focusWidth;
        return this;
    }

    public int getFocusHeight() {
        return focusHeight;
    }

    public ImagePicker focusHeight(int focusHeight) {
        this.focusHeight = focusHeight;
        return this;
    }

    public File getTakeImageFile() {
        return takeImageFile;
    }

    public File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + context.getPackageName() + "/ImagePicker/cropTemp/");
            cropCacheFolder.mkdirs();
        }
        return cropCacheFolder;
    }

    public ImagePicker cropCacheFolder(File cropCacheFolder) {
        this.cropCacheFolder = cropCacheFolder;
        return this;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public ImagePicker imageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public CropImageView.Style getStyle() {
        return style;
    }

    public ImagePicker style(CropImageView.Style style) {
        this.style = style;
        return this;
    }

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public ImagePicker imageFolders(List<ImageFolder> imageFolders) {
        mImageFolders = imageFolders;
        return this;
    }

    public int getCurrentImageFolderPosition() {
        return mCurrentImageFolderPosition;
    }

    public ImagePicker currentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        mCurrentImageFolderPosition = mCurrentSelectedImageSetPosition;
        return this;
    }

    public ArrayList<ImageItem> getCurrentImageFolderItems() {
        return mImageFolders.get(mCurrentImageFolderPosition).images;
    }

    public boolean isSelect(ImageItem item) {
        return mSelectedImages.contains(item);
    }

    public int getSelectImageCount() {
        if (mSelectedImages == null) {
            return 0;
        }
        return mSelectedImages.size();
    }

    public ArrayList<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    public ImagePicker selectedImages(ArrayList<ImageItem> selectedImages) {
        if (selectedImages == null) {
            return null;
        }
        this.mSelectedImages = selectedImages;
        return this;
    }

    public void clearSelectedImages() {
        if (mSelectedImages != null) mSelectedImages.clear();
    }

    public void clear() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
        if (mImageFolders != null) {
            mImageFolders.clear();
            mImageFolders = null;
        }
        if (mSelectedImages != null) {
            mSelectedImages.clear();
        }
        mCurrentImageFolderPosition = 0;
    }

    /**
     * 拍照的方法
     */
    @Deprecated
    public void takePicture(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (Utils.existSDCard())
                takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            else takeImageFile = Environment.getDataDirectory();
            takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
            if (takeImageFile != null) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！

                Uri uri;
                if (VERSION.SDK_INT <= VERSION_CODES.M) {
                    uri = Uri.fromFile(takeImageFile);
                } else {

                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */
                    uri = FileProvider.getUriForFile(activity, ProviderUtil.getFileProviderName(activity), takeImageFile);
                    //加入uri权限 要不三星手机不能拍照
                    List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }

                Log.e("nanchen", ProviderUtil.getFileProviderName(activity));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        activity.startActivityForResult(takePictureIntent, requestCode);
    }

    public void addOnPictureSelectedListener(OnPictureSelectedListener l) {
        if (mImageSelectedListeners == null) mImageSelectedListeners = new ArrayList<>();
        mImageSelectedListeners.add(l);
    }

    public void removeOnPictureSelectedListener(OnPictureSelectedListener l) {
        if (mImageSelectedListeners == null) return;
        mImageSelectedListeners.remove(l);
    }

    public void addSelectedImageItem(int position, ImageItem item, boolean isAdd) {
        if (isAdd) mSelectedImages.add(item);
        else mSelectedImages.remove(item);
        notifyImageSelectedChanged(position, item, isAdd);
    }

    private void notifyImageSelectedChanged(int position, ImageItem item, boolean isAdd) {
        if (mImageSelectedListeners == null) return;
        for (OnPictureSelectedListener l : mImageSelectedListeners) {
            l.onImageSelected(position, item, isAdd);
        }
    }

    /**
     * 用于手机内存不足，进程被系统回收，重启时的状态恢复
     */
    public void restoreInstanceState(Bundle savedInstanceState) {
        cropCacheFolder = (File) savedInstanceState.getSerializable("cropCacheFolder");
        takeImageFile = (File) savedInstanceState.getSerializable("takeImageFile");
        imageLoader = savedInstanceState.getParcelable("imageLoader");
        style = (CropImageView.Style) savedInstanceState.getSerializable("style");
        multiMode = savedInstanceState.getBoolean("multiMode");
        crop = savedInstanceState.getBoolean("crop");
        showCamera = savedInstanceState.getBoolean("showCamera");
        isSaveRectangle = savedInstanceState.getBoolean("isSaveRectangle");
        selectLimit = savedInstanceState.getInt("selectLimit");
        outPutX = savedInstanceState.getInt("outPutX");
        outPutY = savedInstanceState.getInt("outPutY");
        focusWidth = savedInstanceState.getInt("focusWidth");
        focusHeight = savedInstanceState.getInt("focusHeight");
    }

    /**
     * 用于手机内存不足，进程被系统回收时的状态保存
     */
    public void saveInstanceState(Bundle outState) {
        outState.putSerializable("cropCacheFolder", cropCacheFolder);
        outState.putSerializable("takeImageFile", takeImageFile);
        outState.putSerializable("style", style);
        outState.putBoolean("multiMode", multiMode);
        outState.putBoolean("crop", crop);
        outState.putBoolean("showCamera", showCamera);
        outState.putBoolean("isSaveRectangle", isSaveRectangle);
        outState.putInt("selectLimit", selectLimit);
        outState.putInt("outPutX", outPutX);
        outState.putInt("outPutY", outPutY);
        outState.putInt("focusWidth", focusWidth);
        outState.putInt("focusHeight", focusHeight);
    }

    public ImagePicker selectedListener(OnSelectedListener listener) {
        onImageSelectedListener = listener;
        return this;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = data.getParcelableArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (onImageSelectedListener != null) {
                    onImageSelectedListener.onImageSelected(images);
                }
            } else {
                if (onImageSelectedListener != null) {
                    onImageSelectedListener.onImageSelected(null);
                }
            }
            Log.v("TAG", "onActivityResult");
            onImageSelectedListener = null;
        }
    }

    public void startPhotoPicker(Activity activity, Class<?> clazz) {
        if (onImageSelectedListener == null) {
            Log.e(TAG, "\n\n\nOnImageSelectedListener is null , will not return any data\n\n\n");
        }
        multiMode(false);
        ImagePicker.getInstance().selectLimit(1);
        Intent intent = new Intent(activity, clazz);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        activity.startActivityForResult(intent, 100);
    }

    public void startPhotoPicker(Activity activity) {

        startPhotoPicker(activity, ImageGridActivity.class);
    }


    public void startImagePicker(Activity activity, Class<?> clazz, ArrayList<ImageItem> images) {
        if (onImageSelectedListener == null) {
            Log.e(TAG, "\n\n\nOnImageSelectedListener is null , will not return any data\n\n\n");
        }
        Intent intent = new Intent(activity, clazz);
        if (images != null) {
            intent.putParcelableArrayListExtra(ImageGridActivity.EXTRAS_IMAGES, images);
            ImagePicker.getInstance().selectedImages(images);
        }
        loadType(MediaType.IMAGE);
        activity.startActivityForResult(intent, 100);
    }

    public void startImagePicker(Activity activity, ArrayList<ImageItem> images) {
        startImagePicker(activity, ImageGridActivity.class, images);
    }

    public void startVideoPicker(Activity activity, Class<?> clazz) {
        if (onImageSelectedListener == null) {
            Log.e(TAG, "\n\n\nOnImageSelectedListener is null , will not return any data\n\n\n");
        }
        crop(false);
        showCamera(false);
        selectLimit(1);
        multiMode(false);
        loadType(MediaType.VIDEO);
        Intent intent = new Intent(activity, clazz);
        activity.startActivityForResult(intent, 100);
    }

    public void startVideoPicker(Activity activity) {
        startVideoPicker(activity, ImageGridActivity.class);
    }

    public void startImageViewer(Activity activity, List<String> images, View view, int position) {
        if (images == null || images.size() == 0)
            return;
        ImagePicker.getInstance().viewerItem(images);
        Intent intent = new Intent(activity, ImageViewerActivity.class);
        intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && ImagePicker.getInstance().isShareView()) {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(view, activity.getString(R.string.share_view_photo) + position));
            ActivityCompat.startActivity(activity, intent, option.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public List<String> getViewerItem() {
        return viewerItem;
    }

    public void viewerItem(List<String> data) {
        viewerItem = data;
    }

    public MediaType getLoadType() {
        return loadType;
    }


    public interface OnPictureSelectedListener {
        void onImageSelected(int position, ImageItem item, boolean isAdd);
    }

    public interface OnSelectedListener {
        void onImageSelected(List<ImageItem> items);
    }

}
