package com.blackchopper.imagepicker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackchopper.imagepicker.DataHolder;
import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.R;
import com.blackchopper.imagepicker.adapter.ImagePageAdapter;
import com.blackchopper.imagepicker.bean.ImageItem;
import com.blackchopper.imagepicker.util.CollectionHelper;
import com.blackchopper.imagepicker.util.ImmersiveHelper;
import com.blackchopper.imagepicker.util.NavigationBarChangeListener;
import com.blackchopper.imagepicker.util.Utils;
import com.blackchopper.imagepicker.view.SuperCheckBox;
import com.blackchopper.imagepicker.view.ViewPagerFixed;

import java.util.ArrayList;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
 */
public abstract class AbstractImagePreviewActivity extends ImageBaseActivity implements ImagePicker.OnPictureSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    public static final String ISORIGIN = "isOrigin";
    protected ImagePicker imagePicker;
    protected ArrayList<ImageItem> mImageItems;      //跳转进ImagePreviewFragment的图片文件夹
    protected int mCurrentPosition = 0;              //跳转进ImagePreviewFragment时的序号，第几个图片
    protected TextView tv_title;                  //显示当前图片的位置  例如  5/31
    protected ArrayList<ImageItem> selectedImages;   //所有已经选中的图片
    protected View top_bar;
    protected ViewPagerFixed viewpager;
    protected ImagePageAdapter mAdapter;
    private boolean isOrigin;                      //是否选中原图
    private SuperCheckBox cb_check;                //是否选中当前图片的CheckBox
    private SuperCheckBox cb_origin;               //原图
    private Button btn_ok;                         //确认图片的选择
    private ImageView iv_back;
    private View bottom_bar;
    private View view_bottom;
    private boolean isFromItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
        initViewPager();
        initListener();
    }

    private void initView() {
        top_bar = findViewById(attachTopBarRes());
        btn_ok = findViewById(attachButtonOkRes());
        iv_back = findViewById(attachButtonBackRes());
        tv_title = findViewById(attachTitleRes());
        viewpager = findViewById(attachViewPagerRes());
        bottom_bar = findViewById(attachBottomBarRes());
        cb_check = findViewById(attachCheckRes());
        cb_origin = findViewById(attachCheckOriginRes());
        view_bottom = findViewById(attachBottomViewRes());
    }

    private void initData() {
        mCurrentPosition = getIntent().getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        isOrigin = getIntent().getBooleanExtra(AbstractImagePreviewActivity.ISORIGIN, false);
        isFromItems = getIntent().getBooleanExtra(ImagePicker.EXTRA_FROM_ITEMS, false);
        if (isFromItems)
            mImageItems = getIntent().getParcelableArrayListExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
        else
            mImageItems = (ArrayList<ImageItem>) DataHolder.getInstance().retrieve(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS);
        if (mImageItems == null) {
            Log.i("TAG", "initData: null");
        } else {
            Log.i("TAG", "initData: " + mImageItems.size());
        }
        Log.i("TAG", "initData: isFromItems--->>" + isFromItems);

        imagePicker = ImagePicker.getInstance();
        selectedImages = imagePicker.getSelectedImages();
    }

    private void initEvent() {
        btn_ok.setVisibility(View.GONE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imagePicker.addOnPictureSelectedListener(this);
        btn_ok.setVisibility(View.VISIBLE);
        btn_ok.setOnClickListener(this);
        bottom_bar.setVisibility(View.VISIBLE);
        cb_origin.setText(getString(R.string.ip_origin));
        cb_origin.setOnCheckedChangeListener(this);
        cb_origin.setChecked(isOrigin);
        //初始化当前页面的状态
        tv_title.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
        onImageSelected(0, null, false);
        ImageItem item = mImageItems.get(mCurrentPosition);
        boolean isSelected = imagePicker.isSelect(item);
        cb_check.setChecked(isSelected);
    }

    private void initViewPager() {
        mAdapter = new ImagePageAdapter(this, CollectionHelper.imageItem2String(mImageItems));
        mAdapter.setPhotoViewClickListener(new ImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        viewpager.setAdapter(mAdapter);
        viewpager.setCurrentItem(mCurrentPosition, false);
    }

    private void initListener() {
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                ImageItem item = mImageItems.get(mCurrentPosition);
                boolean isSelected = imagePicker.isSelect(item);
                cb_check.setChecked(isSelected);
                tv_title.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });
        //当点击当前选中按钮的时候，需要根据当前的选中状态添加和移除图片
        cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItem imageItem = mImageItems.get(mCurrentPosition);
                int selectLimit = imagePicker.getSelectLimit();
                if (cb_check.isChecked() && selectedImages.size() >= selectLimit) {
                    Toast.makeText(AbstractImagePreviewActivity.this, getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                    cb_check.setChecked(false);
                } else {
                    imagePicker.addSelectedImageItem(mCurrentPosition, imageItem, cb_check.isChecked());
                }
            }
        });
        NavigationBarChangeListener.with(this).setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
            @Override
            public void onNavigationBarShow(int orientation, int height) {
                view_bottom.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = view_bottom.getLayoutParams();
                if (layoutParams.height == 0) {
                    layoutParams.height = Utils.getNavigationBarHeight(AbstractImagePreviewActivity.this);
                    view_bottom.requestLayout();
                }
            }

            @Override
            public void onNavigationBarHide(int orientation) {
                view_bottom.setVisibility(View.GONE);
            }
        });
        NavigationBarChangeListener.with(this, NavigationBarChangeListener.ORIENTATION_HORIZONTAL)
                .setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
                    @Override
                    public void onNavigationBarShow(int orientation, int height) {
                        top_bar.setPadding(0, 0, height, 0);
                        bottom_bar.setPadding(0, 0, height, 0);
                    }

                    @Override
                    public void onNavigationBarHide(int orientation) {
                        top_bar.setPadding(0, 0, 0, 0);
                        bottom_bar.setPadding(0, 0, 0, 0);
                    }
                });
    }

    protected abstract int attachViewPagerRes();

    protected abstract int attachBottomViewRes();

    protected abstract int attachCheckOriginRes();

    protected abstract int attachCheckRes();

    protected abstract int attachTitleRes();

    protected abstract int attachBottomBarRes();

    protected abstract int attachButtonOkRes();

    protected abstract int attachButtonBackRes();


    /**
     * 图片添加成功后，修改当前图片的选中数量
     * 当调用 addSelectedImageItem 或 deleteSelectedImageItem 都会触发当前回调
     */
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            btn_ok.setText(getString(R.string.ip_select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
        } else {
            btn_ok.setText(getString(R.string.ip_complete));
        }

        if (cb_origin.isChecked()) {
            long size = 0;
            for (ImageItem imageItem : selectedImages)
                size += imageItem.size;
            String fileSize = Formatter.formatFileSize(this, size);
            cb_origin.setText(getString(R.string.ip_origin_size, fileSize));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(AbstractImagePreviewActivity.ISORIGIN, isOrigin);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == attachButtonOkRes()) {
            if (imagePicker.getSelectedImages().size() == 0) {
                cb_check.setChecked(true);
                ImageItem imageItem = mImageItems.get(mCurrentPosition);
                imagePicker.addSelectedImageItem(mCurrentPosition, imageItem, cb_check.isChecked());
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
            setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
            finish();

        } else if (id == attachButtonBackRes()) {
            Intent intent = new Intent();
            intent.putExtra(AbstractImagePreviewActivity.ISORIGIN, isOrigin);
            setResult(ImagePicker.RESULT_CODE_BACK, intent);
            finish();
        }
    }

    @Override
    protected boolean attachNavigationEmbed() {
        return true;
    }

    @Override
    protected boolean attachStatusEmbed() {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == attachCheckOriginRes()) {
            if (isChecked) {
                long size = 0;
                for (ImageItem item : selectedImages)
                    size += item.size;
                String fileSize = Formatter.formatFileSize(this, size);
                isOrigin = true;
                cb_origin.setText(getString(R.string.ip_origin_size, fileSize));
            } else {
                isOrigin = false;
                cb_origin.setText(getString(R.string.ip_origin));
            }
        }
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnPictureSelectedListener(this);
        super.onDestroy();
    }

    /**
     * 单击时，隐藏头和尾
     */
    public void onImageSingleTap() {
        if (top_bar.getVisibility() == View.VISIBLE) {
            top_bar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
            bottom_bar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
            top_bar.setVisibility(View.GONE);
            bottom_bar.setVisibility(View.GONE);
            ImmersiveHelper.setOrChangeTranslucentColor(this, (Toolbar) top_bar, null, getResources().getColor(attachImmersiveColorRes(false)));
            Log.i("TAG", "onImageSingleTap: false");
        } else {
            top_bar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
            bottom_bar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            top_bar.setVisibility(View.VISIBLE);
            bottom_bar.setVisibility(View.VISIBLE);
            ImmersiveHelper.setOrChangeTranslucentColor(this, (Toolbar) top_bar, null, getResources().getColor(attachImmersiveColorRes(true)));
            Log.i("TAG", "onImageSingleTap: true");
        }
    }

    protected abstract int attachImmersiveColorRes(boolean show);


}
