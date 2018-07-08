package com.blackchopper.imagepicker.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.R;
import com.blackchopper.imagepicker.adapter.ImageAdapter;
import com.blackchopper.imagepicker.photo.PhotoView;

import java.util.ArrayList;

public class ImageViewerActivity extends ImageBaseActivity {

    ViewPager viewpager;
    ArrayList<String> mImages;
    int mPosition;
    PhotoView photo;
    ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initViewPager();
    }

    private void initData() {
        mImages = getIntent().getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
        mPosition = getIntent().getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);

        viewpager = findViewById(R.id.viewpager);
    }

    @Override
    protected int attachImmersiveColorRes() {
        return R.color.black;
    }

    @Override
    protected int attachTopBarRes() {
        return 0;
    }

    @Override
    protected boolean attachImmersiveLightMode() {
        return false;
    }

    @Override
    protected int attachLayoutRes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        return R.layout.activity_image_viewer;
    }

    private void initViewPager() {
        viewpager.setBackgroundResource(attachImmersiveColorRes());
        mAdapter = new ImageAdapter(this, mImages, mPosition);
        viewpager.setAdapter(mAdapter);
    }
}
