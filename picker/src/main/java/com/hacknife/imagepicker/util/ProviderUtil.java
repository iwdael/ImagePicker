package com.hacknife.imagepicker.util;

import android.content.Context;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".ImagePickerFileProvider";
    }
}
