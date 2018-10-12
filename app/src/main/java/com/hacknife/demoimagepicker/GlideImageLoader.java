package com.hacknife.demoimagepicker;

import android.app.Activity;
import android.net.Uri;
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
public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayFileImage(ImageView imageView, String path) {
        if (path.equals(imageView.getTag())) return;
        GlideApp.with(imageView.getContext())                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
        imageView.setTag(path);
    }

    @Override
    public void displayNetImage(ImageView imageView, String path) {
        if (path.equals(imageView.getTag())) return;
        GlideApp.with(imageView.getContext()).load(path).into(imageView);
        imageView.setTag(path);
    }

    @Override
    public void displayFullImage(ImageView imageView, String path) {
        if (path.equals(imageView.getTag())) return;

        GlideApp.with(imageView.getContext()).load(path).into(imageView);
        imageView.setTag(path);
    }

    @Override
    public void displayFileVideo(String path) {

    }
}
