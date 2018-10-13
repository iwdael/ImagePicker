package com.hacknife.demoimagepicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.Briefness;
import com.hacknife.imagepicker.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

@BindLayout(R.layout.activity_sample_nine)
public class SampleNineActivity extends AppCompatActivity {
    SampleNineActivityBriefnessor briefnessor;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_nine);
        briefnessor = (SampleNineActivityBriefnessor) Briefness.bind(this);
        imageAdapter = new ImageAdapter(this);
        imageAdapter.setRecyclerView(briefnessor.rc_view);
        imageAdapter.setImageSize(20, 0, 0);
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
        imageAdapter.bindData(list);
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        imageAdapter.onActivityReenter(resultCode, data);
    }
}
