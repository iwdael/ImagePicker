package com.hacknife.mediapicker.loader;

import android.widget.ImageView;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 */
public interface MediaLoader {


    void displayFileImage(ImageView imageView, String path);

    void displayUserImage(ImageView imageView, String path);

    void displayFileVideo(String path);


    Class<?> displayFullImageClass();
}
