package com.hacknife.demoimagepicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.Briefness;
import com.hacknife.mediapicker.MediaPicker;
import com.hacknife.mediapicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

@BindLayout(R.layout.activity_sample)
public class SampleActivity extends AppCompatActivity {
    SampleActivityBriefnessor briefnessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (SampleActivityBriefnessor) Briefness.bind(this);
        MediaPicker.getInstance().imageLoader(new GlideImageLoader());
    }

    public void onTakePhotoClick() {
        MediaPicker.getInstance()
                .crop(false)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startPhotoPicker(this);
    }

    public void onTakePhotoCropClick() {
        MediaPicker.getInstance()
                .crop(true)
                .outPutY(800)
                .outPutX(800)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startPhotoPicker(this);
    }

    public void onPickerSinglePhotoClick() {
        MediaPicker.getInstance()
                .multiMode(false)
                .showCamera(true)
                .crop(false)
                .outPutY(800)
                .outPutX(800)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startImagePicker(this);
    }

    public void onPickerMultiPhotoClick() {
        MediaPicker.getInstance()
                .multiMode(true)
                .selectLimit(8)
                .showCamera(true)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startImagePicker(this, null);
    }

    public void onPickerVideoClick() {
        MediaPicker.getInstance()
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startVideoPicker(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaPicker.getInstance().onActivityResult(requestCode, resultCode, data);
    }


    private void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void onNinePictureClick() {
        Intent intent = new Intent(this, SampleNineActivity.class);
        startActivity(intent);
    }

    public void onViewerClick() {
        List<String> list = new ArrayList<>();
        list.add("http://dpic.tiankong.com/8n/a0/QJ6177688033.jpg");
        list.add("http://dpic.tiankong.com/6j/s5/QJ6146491299.jpg");
        list.add("http://dpic.tiankong.com/zt/pz/QJ6519443181.jpg");
        list.add("http://dpic.tiankong.com/br/ja/QJ6223394486.jpg");
        list.add("http://dpic.tiankong.com/i6/ke/QJ6322734186.jpg");
        list.add("http://dpic.tiankong.com/cl/at/QJ6769314375.jpg");
        list.add("http://dpic.tiankong.com/xp/3t/QJ6230352510.jpg");
        list.add("http://dpic.tiankong.com/37/ha/QJ6534800776.jpg");
        list.add("http://dpic.tiankong.com/5p/33/QJ6215180920.jpg");
        MediaPicker.getInstance().startImageViewer(this, list);
    }
}
