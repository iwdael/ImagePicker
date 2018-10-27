package com.hacknife.mediapicker.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hacknife.mediapicker.MediaPicker;
import com.hacknife.mediapicker.bean.ImageItem;
import com.hacknife.mediapicker.util.BitmapUtil;
import com.hacknife.mediapicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 */
public abstract class AbstractImageCropActivity extends ImageBaseActivity implements View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener {

    private CropImageView mCropImageView;
    private Bitmap mBitmap;
    private boolean mIsSaveRectangle;
    private int mOutputX;
    private int mOutputY;
    private ArrayList<ImageItem> mImageItems;
    private MediaPicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        imagePicker = MediaPicker.getInstance();

        //初始化View
        findViewById(attachButtonBackRes()).setOnClickListener(this);
        Button btn_ok = findViewById(attachButtonOkRes());
        btn_ok.setText(getString(com.hacknife.mediapicker.R.string.ip_complete));
        btn_ok.setOnClickListener(this);
        TextView tv_des = findViewById(attachTitleRes());
        tv_des.setText(getString(com.hacknife.mediapicker.R.string.ip_photo_crop));
        mCropImageView = findViewById(attachCropImageRes());
        mCropImageView.setOnBitmapSaveCompleteListener(this);

        //获取需要的参数
        mOutputX = imagePicker.getOutPutX();
        mOutputY = imagePicker.getOutPutY();
        mIsSaveRectangle = imagePicker.isSaveRectangle();
        mImageItems = imagePicker.getSelectedImages();
        String imagePath = mImageItems.get(0).path;

        mCropImageView.setFocusStyle(imagePicker.getStyle());
        mCropImageView.setFocusWidth(imagePicker.getFocusWidth());
        mCropImageView.setFocusHeight(imagePicker.getFocusHeight());

        //缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);
        mCropImageView.setImageBitmap(mCropImageView.rotate(mBitmap, BitmapUtil.getBitmapDegree(imagePath)));

    }

    protected abstract int attachCropImageRes();

    protected abstract int attachTitleRes();

    protected abstract int attachButtonBackRes();

    protected abstract int attachButtonOkRes();

    @Override
    protected boolean attachNavigationEmbed() {
        return false;
    }

    @Override
    protected boolean attachStatusEmbed() {
        return false;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == attachButtonBackRes()) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == attachButtonOkRes()) {
            mCropImageView.saveBitmapToFile(imagePicker.getCropCacheFolder(this), mOutputX, mOutputY, mIsSaveRectangle);
        }
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        mImageItems.remove(0);
        ImageItem imageItem = new ImageItem();
        imageItem.path = file.getAbsolutePath();
        mImageItems.add(imageItem);

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(MediaPicker.EXTRA_RESULT_ITEMS, mImageItems);
        setResult(MediaPicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCropImageView.setOnBitmapSaveCompleteListener(null);
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
