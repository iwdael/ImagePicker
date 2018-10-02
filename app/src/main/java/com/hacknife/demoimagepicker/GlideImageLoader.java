package com.hacknife.demoimagepicker;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.hacknife.imagepicker.R;
import com.hacknife.imagepicker.loader.ImageLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class GlideImageLoader implements ImageLoader,Parcelable {
    protected GlideImageLoader(Parcel in) {
    }

    public GlideImageLoader() {
    }

    public static final Creator<GlideImageLoader> CREATOR = new Creator<GlideImageLoader>() {
        @Override
        public GlideImageLoader createFromParcel(Parcel in) {
            return new GlideImageLoader(in);
        }

        @Override
        public GlideImageLoader[] newArray(int size) {
            return new GlideImageLoader[size];
        }
    };

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideApp.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.drawable.ic_default_image)           //设置错误图片
                .placeholder(R.drawable.ic_default_image)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideApp.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void displayImage(String path, ImageView imageView) {
        GlideApp.with(imageView.getContext()).load(path).into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
