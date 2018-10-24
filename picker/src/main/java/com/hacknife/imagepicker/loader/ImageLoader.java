package com.hacknife.imagepicker.loader;

import android.app.Activity;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public interface ImageLoader {


    void displayFileImage(ImageView imageView, String path);

    void displayUserImage(ImageView imageView, String path);

    void displayFileVideo(String path);


    Class<?> displayFullImageClass();
}
