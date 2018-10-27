package com.hacknife.mediapicker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 */
public class ImageFolder implements Parcelable {

    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public ImageItem cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<ImageItem> images;  //当前文件夹下所有图片的集合

    public ImageFolder() {
    }

    protected ImageFolder(Parcel in) {
        name = in.readString();
        path = in.readString();
        cover = in.readParcelable(ImageItem.class.getClassLoader());
        images = in.createTypedArrayList(ImageItem.CREATOR);
    }

    public static final Creator<ImageFolder> CREATOR = new Creator<ImageFolder>() {
        @Override
        public ImageFolder createFromParcel(Parcel in) {
            return new ImageFolder(in);
        }

        @Override
        public ImageFolder[] newArray(int size) {
            return new ImageFolder[size];
        }
    };

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeParcelable(cover, flags);
        dest.writeTypedList(images);
    }


}
