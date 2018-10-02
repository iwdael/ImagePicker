package com.hacknife.demoimagepicker;

import com.hacknife.imagepicker.ui.AbstractImageCropActivity;

public class CropActivity extends AbstractImageCropActivity {


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
        return R.color.white;
    }

    @Override
    protected int attachTopBarRes() {
        return R.id.toolbar;
    }

    @Override
    protected boolean attachImmersiveLightMode() {
        return true;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_crop;
    }
}
