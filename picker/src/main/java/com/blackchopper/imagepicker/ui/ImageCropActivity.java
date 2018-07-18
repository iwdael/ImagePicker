package com.blackchopper.imagepicker.ui;

import com.blackchopper.imagepicker.R;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
 */
public class ImageCropActivity extends AbstractImageCropActivity {
    @Override
    protected int attachButtonBackRes() {
        return R.id.iv_back;
    }

    @Override
    protected int attachButtonOkRes() {
        return R.id.btn_ok;
    }

    @Override
    protected int attachCropImageRes() {
        return R.id.cv_crop_image;
    }

    @Override
    protected int attachTitleRes() {
        return R.id.tv_title;
    }

    @Override
    protected int attachImmersiveColorRes() {
        return R.color.ip_color_primary_dark;
    }

    @Override
    protected int attachTopBarRes() {
        return R.id.top_bar;
    }

    @Override
    protected boolean attachImmersiveLightMode() {
        return false;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_image_crop;
    }
}
