package com.hacknife.imagepicker.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hacknife.imagepicker.ImagePicker;
import com.hacknife.imagepicker.R;
import com.hacknife.imagepicker.adapter.ImagePageAdapter;

import java.util.List;
import java.util.Map;

public class ImageViewerActivity extends ImageBaseActivity {

    ViewPager viewpager;
    List<String> mImages;
    int mPosition;
    ImagePageAdapter mAdapter;
    boolean isMultiPhoto = false;
    TextView indicator;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initViewPager();
    }

    @Override
    protected boolean attachStatusEmbed() {
        return true;
    }

    @Override
    protected boolean attachNavigationEmbed() {
        return true;
    }

    private void initData() {
        mImages = ImagePicker.getInstance().getViewerItem();
        mPosition = getIntent().getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        if (mImages.size() > 1)
            isMultiPhoto = true;
        viewpager = findViewById(R.id.viewpager);
        indicator = findViewById(R.id.indicator);
        back = findViewById(R.id.iv_back);
        if (!isMultiPhoto)
            indicator.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        ImagePicker.getInstance().viewerItem(null);
        mAdapter.setPhotoViewClickListener(new ImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                if (isMultiPhoto) {
                    if (view.getVisibility() == View.VISIBLE) {
//                        indicator.setAnimation(AnimationUtils.loadAnimation(ImageViewerActivity.this, R.anim.fade_out));
                        indicator.setVisibility(View.GONE);
                    } else {

//                        indicator.setAnimation(AnimationUtils.loadAnimation(ImageViewerActivity.this, R.anim.fade_in));
                        indicator.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        viewpager.setAdapter(mAdapter);
        viewpager.setCurrentItem(mPosition);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setText(getString(R.string.indicator, position + 1, mImages.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicator.setText(getString(R.string.indicator, mPosition + 1, mImages.size()));
    }

    @SuppressLint("NewApi")
    @Override
    public void finishAfterTransition() {
        int current = viewpager.getCurrentItem();
        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_EXIT_POSITION, current);
        setResult(RESULT_OK, intent);
        if (current != mPosition) {
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
            }
        });
    }
}
