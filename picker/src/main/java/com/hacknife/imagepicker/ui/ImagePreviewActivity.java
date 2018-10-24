package com.hacknife.imagepicker.ui;

import com.hacknife.imagepicker.R;



/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ImagePreviewActivity extends AbstractImagePreviewActivity {
    @Override
    protected int attachImmersiveColorRes() {
        return R.color.ip_color_primary_dark;
    }

    @Override
    protected int attachTopBarRes() {
        return R.id.ll_top_bar;
    }

    @Override
    protected boolean attachImmersiveLightMode() {
        return false;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.imagepicker_activity_image_preview;
    }

    @Override
    protected int attachViewPagerRes() {
        return R.id.viewpager;
    }

    @Override
    protected int attachBottomViewRes() {
        return R.id.view_bottom;
    }

    @Override
    protected int attachCheckOriginRes() {
        return R.id.cb_origin;
    }

    @Override
    protected int attachCheckRes() {
        return R.id.cb_check;
    }

    @Override
    protected int attachTitleRes() {
        return R.id.tv_title;
    }

    @Override
    protected int attachBottomBarRes() {
        return R.id.bottom_bar;
    }

    @Override
    protected int attachButtonOkRes() {
        return R.id.btn_ok;
    }

    @Override
    protected int attachButtonBackRes() {
        return R.id.iv_back;
    }

    @Override
    protected int attachImmersiveColorRes(boolean show) {
        if (show)
            return R.color.ip_color_primary_dark;
        else
            return R.color.ip_color_primary_dark;
    }

}
