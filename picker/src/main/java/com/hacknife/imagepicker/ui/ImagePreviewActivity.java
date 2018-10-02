package com.hacknife.imagepicker.ui;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ImagePreviewActivity extends AbstractImagePreviewActivity {
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
        return com.hacknife.imagepicker.R.layout.activity_image_preview;
    }

    @Override
    protected int attachViewPagerRes() {
        return com.hacknife.imagepicker.R.id.viewpager;
    }

    @Override
    protected int attachBottomViewRes() {
        return com.hacknife.imagepicker.R.id.view_bottom;
    }

    @Override
    protected int attachCheckOriginRes() {
        return com.hacknife.imagepicker.R.id.cb_origin;
    }

    @Override
    protected int attachCheckRes() {
        return com.hacknife.imagepicker.R.id.cb_check;
    }

    @Override
    protected int attachTitleRes() {
        return com.hacknife.imagepicker.R.id.tv_title;
    }

    @Override
    protected int attachBottomBarRes() {
        return com.hacknife.imagepicker.R.id.bottom_bar;
    }

    @Override
    protected int attachButtonOkRes() {
        return com.hacknife.imagepicker.R.id.btn_ok;
    }

    @Override
    protected int attachButtonBackRes() {
        return com.hacknife.imagepicker.R.id.iv_back;
    }

    @Override
    protected int attachImmersiveColorRes(boolean show) {
        if (show)
            return com.hacknife.imagepicker.R.color.ip_color_primary_dark;
        else
            return com.hacknife.imagepicker.R.color.translucent;
    }
}
