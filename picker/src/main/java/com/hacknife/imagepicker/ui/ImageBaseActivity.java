package com.hacknife.imagepicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hacknife.imagepicker.ImagePicker;
import com.hacknife.immersive.Immersive;


/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public abstract class ImageBaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Immersive.setContentView(this, attachLayoutRes(), attachImmersiveColorRes(), attachImmersiveColorRes(), attachStatusEmbed(), attachNavigationEmbed());
    }

    protected abstract boolean attachNavigationEmbed();

    protected abstract boolean attachStatusEmbed();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ImagePicker.getInstance().saveInstanceState(outState);
    }

    protected abstract int attachImmersiveColorRes();

    protected abstract int attachTopBarRes();

    protected abstract boolean attachImmersiveLightMode();

    protected abstract int attachLayoutRes();


    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ImagePicker.getInstance().restoreInstanceState(savedInstanceState);
    }
}
