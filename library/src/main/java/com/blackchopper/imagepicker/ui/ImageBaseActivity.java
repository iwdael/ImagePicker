package com.blackchopper.imagepicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Toast;

import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.R;
import com.blackchopper.imagepicker.util.ImmersiveHelper;
import com.blackchopper.imagepicker.view.SystemBarTintManager;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
 */
public abstract class ImageBaseActivity extends AppCompatActivity {

    protected SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置虚拟导航栏为透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.ip_color_primary_dark);  //设置上方状态栏的颜色
        setContentView(attachLayoutRes());
        ImmersiveHelper.setOrChangeTranslucentColor(this, (Toolbar) findViewById(attachTopBarRes()), null, getResources().getColor(attachImmersiveColorRes()));
        if (attachImmersiveLightMode())
            ImmersiveHelper.setStatusBarLightMode(getWindow());
    }

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
