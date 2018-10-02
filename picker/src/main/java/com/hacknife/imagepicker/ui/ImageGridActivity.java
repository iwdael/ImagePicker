package com.hacknife.imagepicker.ui;

import com.hacknife.imagepicker.R;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ImageGridActivity extends AbstractImageGridActivity {
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
        return R.layout.activity_image_grid;
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
    protected int attachTitleRes() {
        return R.id.tv_title;
    }

    @Override
    protected Class<?> attachPreviewActivityClass() {
        return ImagePreviewActivity.class;
    }

    @Override
    protected Class<?> attachCropActivityClass() {
        return ImageCropActivity.class;
    }


}
