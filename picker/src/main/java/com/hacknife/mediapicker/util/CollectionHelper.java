package com.hacknife.mediapicker.util;

import com.hacknife.mediapicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 */
public class CollectionHelper {
    public static List<String> imageItem2String(ArrayList<ImageItem> mImageItems) {
        List<String> result = new ArrayList<>(mImageItems.size());
        int length = mImageItems.size();
        for (int i = 0; i < length; i++) {
            result.add(mImageItems.get(i).getImageUrl());
        }
        return result;
    }
}
