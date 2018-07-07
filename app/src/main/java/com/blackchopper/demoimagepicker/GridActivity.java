package com.blackchopper.demoimagepicker;

import com.blackchopper.imagepicker.ui.AbstractImageGridActivity;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
 */
public class GridActivity extends AbstractImageGridActivity {
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
        return R.layout.activity_grid;
    }

    @Override
    protected int attachRecyclerViewRes() {
        return R.id.rc_view;
    }

    @Override
    protected int attachButtonBackRes() {
        return R.id.iv_back;
    }

    @Override
    protected int attachButtonOkRes() {
        return R.id.btn_ok;
    }

    @Override
    protected int attachButtonPreviewRes() {
        return R.id.tv_preview;
    }

    @Override
    protected int attachFooterBarRes() {
        return R.id.footer_bar;
    }

    @Override
    protected int attachDirectoryRes() {
        return R.id.ll_dir;
    }

    @Override
    protected int attachDirectoryNameRes() {
        return R.id.tv_dir;
    }

    @Override
    protected Class<?> attachPreviewActivityClass() {
        return PreviewActivity.class;
    }

    @Override
    protected Class<?> attachCropActivityClass() {
        return CropActivity.class;
    }
}
