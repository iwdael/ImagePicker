package com.hacknife.imagepicker.ui;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ImageCropActivity extends AbstractImageCropActivity {
    @Override
    protected int attachButtonBackRes() {
        return com.hacknife.imagepicker.R.id.iv_back;
    }

    @Override
    protected int attachButtonOkRes() {
        return com.hacknife.imagepicker.R.id.btn_ok;
    }

    @Override
    protected int attachCropImageRes() {
        return com.hacknife.imagepicker.R.id.cv_crop_image;
    }

    @Override
    protected int attachTitleRes() {
        return com.hacknife.imagepicker.R.id.tv_title;
    }

    @Override
    protected int attachImmersiveColorRes() {
        return com.hacknife.imagepicker.R.color.ip_color_primary_dark;
    }

    @Override
    protected int attachTopBarRes() {
        return com.hacknife.imagepicker.R.id.top_bar;
    }

    @Override
    protected boolean attachImmersiveLightMode() {
        return false;
    }

    @Override
    protected int attachLayoutRes() {
        return com.hacknife.imagepicker.R.layout.activity_image_crop;
    }
}
