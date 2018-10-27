package com.hacknife.mediapicker.util;

import android.content.Context;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 */
public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".MediaPickerFileProvider";
    }
}
