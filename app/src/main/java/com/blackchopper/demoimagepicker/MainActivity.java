package com.blackchopper.demoimagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.bean.ImageItem;
import com.blackchopper.imagepicker.view.CropImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    private ImagePicker imagePicker;

    private RadioButton rb_glide;
    private RadioButton rb_single_select;
    private RadioButton rb_muti_select;
    private RadioButton rb_crop_square;
    private RadioButton rb_crop_circle;
    private TextView tv_select_limit;

    private EditText et_crop_width;
    private EditText et_crop_height;
    private EditText et_crop_radius;
    private EditText et_outputx;
    private EditText et_outputy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePicker = ImagePicker.getInstance().imageLoader(new GlideImageLoader());

        rb_glide = (RadioButton) findViewById(R.id.rb_glide);
        rb_single_select = (RadioButton) findViewById(R.id.rb_single_select);
        rb_muti_select = (RadioButton) findViewById(R.id.rb_muti_select);
        rb_crop_square = (RadioButton) findViewById(R.id.rb_crop_square);
        rb_crop_circle = (RadioButton) findViewById(R.id.rb_crop_circle);
        rb_glide.setChecked(true);
        rb_muti_select.setChecked(true);
        rb_crop_square.setChecked(true);

        et_crop_width = (EditText) findViewById(R.id.et_crop_width);
        et_crop_width.setText("280");
        et_crop_height = (EditText) findViewById(R.id.et_crop_height);
        et_crop_height.setText("280");
        et_crop_radius = (EditText) findViewById(R.id.et_crop_radius);
        et_crop_radius.setText("140");
        et_outputx = (EditText) findViewById(R.id.et_outputx);
        et_outputx.setText("800");
        et_outputy = (EditText) findViewById(R.id.et_outputy);
        et_outputy.setText("800");

        tv_select_limit = (TextView) findViewById(R.id.tv_select_limit);
        SeekBar sb_select_limit = (SeekBar) findViewById(R.id.sb_select_limit);
        sb_select_limit.setMax(15);
        sb_select_limit.setOnSeekBarChangeListener(this);
        sb_select_limit.setProgress(9);

        CheckBox cb_show_camera = (CheckBox) findViewById(R.id.cb_show_camera);
        cb_show_camera.setOnCheckedChangeListener(this);
        cb_show_camera.setChecked(true);
        CheckBox cb_crop = (CheckBox) findViewById(R.id.cb_crop);
        cb_crop.setOnCheckedChangeListener(this);
        cb_crop.setChecked(true);
        CheckBox cb_isSaveRectangle = (CheckBox) findViewById(R.id.cb_isSaveRectangle);
        cb_isSaveRectangle.setOnCheckedChangeListener(this);
        cb_isSaveRectangle.setChecked(true);

        Button btn_open_gallery = (Button) findViewById(R.id.btn_open_gallery);
        btn_open_gallery.setOnClickListener(this);
        findViewById(R.id.btn_open_camera).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_gallery:
                if (rb_single_select.isChecked()) imagePicker.multiMode(false);
                else if (rb_muti_select.isChecked()) imagePicker.multiMode(true);

                if (rb_crop_square.isChecked()) {
                    imagePicker.style(CropImageView.Style.RECTANGLE);
                    Integer width = Integer.valueOf(et_crop_width.getText().toString());
                    Integer height = Integer.valueOf(et_crop_height.getText().toString());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
                    imagePicker.focusWidth(width);
                    imagePicker.focusHeight(height);
                } else if (rb_crop_circle.isChecked()) {
                    imagePicker.style(CropImageView.Style.CIRCLE);
                    Integer radius = Integer.valueOf(et_crop_radius.getText().toString());
                    radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());
                    imagePicker.focusWidth(radius * 2);
                    imagePicker.focusHeight(radius * 2);
                }

                imagePicker.outPutX(Integer.valueOf(et_outputx.getText().toString()));
                imagePicker.outPutY(Integer.valueOf(et_outputy.getText().toString()));
                imagePicker.imageSelectedListener(new ImagePicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        Log.i("TAG", "onImageSelected: " + items.toString());
                    }
                }).startImagePicker(MainActivity.this, GridActivity.class, null);

                break;
            case R.id.btn_open_camera:
                imagePicker.startPhotoPicker(MainActivity.this, GridActivity.class);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_select_limit.setText(String.valueOf(progress));
        imagePicker.selectLimit(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_show_camera:
                imagePicker.showCamera(isChecked);
                break;
            case R.id.cb_crop:
                imagePicker.crop(isChecked);
                break;
            case R.id.cb_isSaveRectangle:
                imagePicker.saveRectangle(isChecked);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(requestCode, resultCode, data);
    }
}
