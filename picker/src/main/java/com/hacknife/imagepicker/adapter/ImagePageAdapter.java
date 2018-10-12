package com.hacknife.imagepicker.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.hacknife.imagepicker.ImagePicker;
import com.hacknife.imagepicker.R;
import com.hacknife.imagepicker.photo.OnOutsidePhotoTapListener;
import com.hacknife.imagepicker.photo.OnPhotoTapListener;
import com.hacknife.imagepicker.photo.PhotoView;
import com.hacknife.imagepicker.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : ImagePicker
 */
public class ImagePageAdapter extends PagerAdapter {

    public PhotoViewClickListener listener;
    private int mPosition;
    private ImagePicker imagePicker;
    private List<String> images = new ArrayList<>();
    private AppCompatActivity mActivity;
    private boolean mIsFromViewr = false;

    public ImagePageAdapter(AppCompatActivity activity, List<String> images, int position) {
        this.mActivity = activity;
        this.images = images;
        imagePicker = ImagePicker.getInstance();
        mPosition = position;
        mIsFromViewr = true;
    }

    public ImagePageAdapter(AppCompatActivity activity, List<String> images) {
        this.mActivity = activity;
        this.images = images;
        imagePicker = ImagePicker.getInstance();
    }

    public void setData(List<String> images) {
        this.images = images;
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);

        String image = images.get(position);

        if (mIsFromViewr)
            imagePicker.getImageLoader().displayNetImage(photoView, image);
        else
            imagePicker.getImageLoader().displayFileImage(photoView, image);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (listener != null) listener.OnPhotoTapListener(view, x, y);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mIsFromViewr) {
            String name = mActivity.getString(R.string.share_view_photo) + position;
            photoView.setTransitionName(name);
            photoView.setTag(name);
            if (position == mPosition)
                setStartPostTransition(photoView);
        }
        photoView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                mActivity.onBackPressed();
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void setStartPostTransition(final View sharedView) {
        sharedView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onPreDraw() {
                        sharedView.getViewTreeObserver().removeOnPreDrawListener(this);
                        mActivity.startPostponedEnterTransition();
                        return false;
                    }
                });
    }


    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
