package com.blackchopper.imagepicker.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.blackchopper.imagepicker.DataHolder;
import com.blackchopper.imagepicker.ImageDataSource;
import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.R;
import com.blackchopper.imagepicker.adapter.ImageFolderAdapter;
import com.blackchopper.imagepicker.adapter.ImageRecyclerAdapter;
import com.blackchopper.imagepicker.bean.ImageFolder;
import com.blackchopper.imagepicker.bean.ImageItem;
import com.blackchopper.imagepicker.util.Utils;
import com.blackchopper.imagepicker.view.FolderPopUpWindow;
import com.blackchopper.imagepicker.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
 */
public abstract class AbstractImageGridActivity extends ImageBaseActivity implements ImageDataSource.OnImagesLoadedListener, ImageRecyclerAdapter.OnImageItemClickListener, ImagePicker.OnPictureSelectedListener, View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;
    public static final String EXTRAS_TAKE_PICKERS = "TAKE";
    public static final String EXTRAS_IMAGES = "IMAGES";
    ImagePicker imagePicker;
    boolean isOrigin = false;  //是否选中原图
    View footer_bar;     //底部栏
    Button btn_ok;       //确定按钮
    View ll_dir; //文件夹切换按钮
    TextView tv_dir; //显示当前文件夹
    TextView tv_preview;      //预览按钮
    ImageFolderAdapter mImageFolderAdapter; //图片文件夹的适配器
    FolderPopUpWindow mFolderPopupWindow;  //ImageSet的PopupWindow
    List<ImageFolder> mImageFolders;   //所有的图片文件夹
    boolean directPhoto = false; // 默认不是直接调取相机
    RecyclerView rc_view;
    ImageRecyclerAdapter mRecyclerAdapter;
    View iv_back;


    protected abstract int attachRecyclerViewRes();

    protected abstract int attachButtonBackRes();

    protected abstract int attachButtonOkRes();

    protected abstract int attachButtonPreviewRes();

    protected abstract int attachFooterBarRes();

    protected abstract int attachDirectoryRes();

    protected abstract int attachDirectoryNameRes();

    protected abstract Class<?> attachPreviewActivityClass();

    protected abstract Class<?> attachCropActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = ImagePicker.getInstance();
        imagePicker.clear();
        imagePicker.addOnPictureSelectedListener(this);
        detectPhoto();
        initView();
        initEvent();
        initRecycler();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRAS_TAKE_PICKERS, directPhoto);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        directPhoto = savedInstanceState.getBoolean(EXTRAS_TAKE_PICKERS, false);
    }

    private void initRecycler() {
        mImageFolderAdapter = new ImageFolderAdapter(this, null);
        mRecyclerAdapter = new ImageRecyclerAdapter(this, null);
        onImageSelected(0, null, false);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new ImageDataSource(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            new ImageDataSource(this, null, this);
        }
    }

    private void detectPhoto() {
        Intent data = getIntent();
        if (data != null && data.getExtras() != null) {
            directPhoto = data.getBooleanExtra(EXTRAS_TAKE_PICKERS, false); // 默认不是直接打开相机
            if (directPhoto) {
                if (!(checkPermission(Manifest.permission.CAMERA))) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ImageGridActivity.REQUEST_PERMISSION_CAMERA);
                } else {
                    imagePicker.takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
                }
            }
            ArrayList<ImageItem> images = data.getParcelableArrayListExtra(EXTRAS_IMAGES);
            imagePicker.selectedImages(images);
        }
    }

    private void initEvent() {
        iv_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_preview.setOnClickListener(this);
        ll_dir.setOnClickListener(this);

    }

    private void initView() {
        rc_view = findViewById(attachRecyclerViewRes());
        btn_ok = findViewById(attachButtonOkRes());
        tv_preview = findViewById(attachButtonPreviewRes());
        footer_bar = findViewById(attachFooterBarRes());
        ll_dir = findViewById(attachDirectoryRes());
        tv_dir = findViewById(attachDirectoryNameRes());
        iv_back = findViewById(attachButtonBackRes());
        if (imagePicker.isMultiMode()) {
            btn_ok.setVisibility(View.VISIBLE);
            tv_preview.setVisibility(View.VISIBLE);
        } else {
            btn_ok.setVisibility(View.GONE);
            tv_preview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnPictureSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == attachButtonOkRes()) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
            setResult(ImagePicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
        } else if (id == attachDirectoryRes()) {
            if (mImageFolders == null || mImageFolders.size() == 0) {
                Log.i("ImageGridActivity", "您的手机没有图片");
                return;
            }
            //点击文件夹按钮
            createPopupFolderList();
            mImageFolderAdapter.refreshData(mImageFolders);  //刷新数据
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.showAtLocation(footer_bar, Gravity.NO_GRAVITY, 0, 0);
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = mImageFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.setSelection(index);
            }
        } else if (id == attachButtonPreviewRes()) {
            Intent intent = new Intent(this, attachPreviewActivityClass());
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
            intent.putParcelableArrayListExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getSelectedImages());
            intent.putExtra(AbstractImagePreviewActivity.ISORIGIN, isOrigin);
            intent.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else if (id == attachButtonBackRes()) {
            finish();
        }
    }


    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mImageFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mImageFolderAdapter.setSelectIndex(position);
                imagePicker.currentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();
                ImageFolder imageFolder = (ImageFolder) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
                    mRecyclerAdapter.refreshData(imageFolder.images);
                    tv_dir.setText(imageFolder.name);
                }
            }
        });
        mFolderPopupWindow.setMargin(footer_bar.getHeight());
    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        imagePicker.imageFolders(imageFolders);
        if (imageFolders.size() == 0) {
            mRecyclerAdapter.refreshData(null);
        } else {
            mRecyclerAdapter.refreshData(imageFolders.get(0).images);
        }
        mRecyclerAdapter.setOnImageItemClickListener(this);
        rc_view.setLayoutManager(new GridLayoutManager(this, 3));
        rc_view.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 2), false));
        rc_view.setAdapter(mRecyclerAdapter);
        mImageFolderAdapter.refreshData(imageFolders);
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        //根据是否有相机按钮确定位置
        position = imagePicker.isShowCamera() ? position - 1 : position;
        if (imagePicker.isMultiMode()) {
            Intent intent = new Intent(this, attachPreviewActivityClass());
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
            DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, imagePicker.getCurrentImageFolderItems());
            intent.putExtra(AbstractImagePreviewActivity.ISORIGIN, isOrigin);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
        } else {
            imagePicker.clearSelectedImages();
            imagePicker.addSelectedImageItem(position, imagePicker.getCurrentImageFolderItems().get(position), true);
            if (imagePicker.isCrop()) {
                Intent intent = new Intent(this, attachCropActivityClass());
                startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
            } else {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                finish();
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            btn_ok.setText(getString(R.string.ip_select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
            btn_ok.setEnabled(true);
            tv_preview.setEnabled(true);
            tv_preview.setText(getResources().getString(R.string.ip_preview_count, imagePicker.getSelectImageCount()));
//            tv_preview.setTextColor(ContextCompat.getColor(this, R.color.ip_text_primary_inverted));
            btn_ok.setTextColor(ContextCompat.getColor(this, R.color.ip_text_primary_inverted));
        } else {
            btn_ok.setText(getString(R.string.ip_complete));
            btn_ok.setEnabled(false);
            tv_preview.setEnabled(false);
            tv_preview.setText(getResources().getString(R.string.ip_preview));
//            tv_preview.setTextColor(ContextCompat.getColor(this, R.color.ip_text_secondary_inverted));
            btn_ok.setTextColor(ContextCompat.getColor(this, R.color.ip_text_secondary_inverted));
        }
        for (int i = imagePicker.isShowCamera() ? 1 : 0; i < mRecyclerAdapter.getItemCount(); i++) {
            if (mRecyclerAdapter.getItem(i).path != null && mRecyclerAdapter.getItem(i).path.equals(item.path)) {
                mRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                isOrigin = data.getBooleanExtra(AbstractImagePreviewActivity.ISORIGIN, false);
            } else {
                //从拍照界面返回
                //点击 X , 没有选择照片
                if (data.getParcelableArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
                    //什么都不做 直接调起相机
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(ImagePicker.RESULT_CODE_ITEMS, data);
                }
                finish();
            }
        } else {
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
                String path = imagePicker.getTakeImageFile().getAbsolutePath();
                ImageItem imageItem = new ImageItem();
                imageItem.path = path;
                imagePicker.clearSelectedImages();
                imagePicker.addSelectedImageItem(0, imageItem, true);
                if (imagePicker.isCrop()) {
                    Intent intent = new Intent(this, attachCropActivityClass());
                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                    setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                    finish();
                }
            } else if (directPhoto) {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ImageDataSource(this, null, this);
            } else {
                showToast("权限被禁止，无法选择本地图片");
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imagePicker.takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
            } else {
                showToast("权限被禁止，无法打开相机");
            }
        }
    }


}
