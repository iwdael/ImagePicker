package com.blackchopper.imagepicker.util;

import com.blackchopper.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
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
