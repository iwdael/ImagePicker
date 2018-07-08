package com.blackchopper.imagepicker.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.R;
import com.blackchopper.imagepicker.adapter.ImagePageAdapter;

import java.util.List;
import java.util.Map;

public class ImageViewerActivity extends ImageBaseActivity {

    ViewPager viewpager;
    List<String> mImages;
    int mPosition;
     ImagePageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initViewPager();
    }

    private void initData() {
        mImages = ImagePicker.getInstance().getViewerItem();
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
        mAdapter = new ImagePageAdapter(this, mImages, mPosition);
        viewpager.setAdapter(mAdapter);
        viewpager.setCurrentItem(mPosition);
    }

    @SuppressLint("NewApi")
    @Override
    public void finishAfterTransition() {
        int current = viewpager.getCurrentItem();
        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_EXIT_POSITION, current);
        setResult(RESULT_OK, intent);
        Log.i("TAG", "1");
        if (current != mPosition) {
             Log.i("TAG", "2");
            Log.i("TAG","2------tag------>>"+getString(R.string.share_view_photo) + current);
            View view = viewpager.findViewWithTag(getString(R.string.share_view_photo) + current);
            setSharedElementCallback(view);
        }
        super.finishAfterTransition();
    }

    @TargetApi(21)
    private void setSharedElementCallback(final View view) {
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                names.clear();
                sharedElements.clear();
                names.add(view.getTransitionName());
                sharedElements.put(view.getTransitionName(), view);
                Log.i("TAG", "3");
                Log.i("TAG","3------name------>>"+view.getTransitionName());

            }
        });
    }
}
