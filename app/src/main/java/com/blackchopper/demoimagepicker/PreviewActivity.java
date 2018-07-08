package com.blackchopper.demoimagepicker;

import com.blackchopper.imagepicker.ui.AbstractImagePreviewActivity;

public class PreviewActivity extends AbstractImagePreviewActivity {


    @Override
    protected int attachImmersiveColorRes() {
        return R.color.colorPrimaryDark;
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
        return R.layout.activity_preview;
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
            return R.color.colorPrimaryDark;
        else
            return  R.color.translucent_white;
    }
}
