package com.hacknife.mediapicker.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hacknife.mediapicker.MediaPicker;
import com.hacknife.mediapicker.R;
import com.hacknife.mediapicker.ui.ImageViewerActivity;
import com.hacknife.mediapicker.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MediaPicker
 * 单独提供的适配器
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    int mImageSize;
    List<String> data = new ArrayList<>();
    int interval;
    int marginLeft;
    int marginRight;
    int column;
    RecyclerView.LayoutManager manager;
    Activity activity;
    int exitPosition;
    int enterPosition;
    RecyclerView recyclerView;

    @SuppressLint("NewApi")
    public ImageAdapter(Activity activity) {
        this.activity = activity;
    }

    public RecyclerView.LayoutManager getManager() {
        return manager;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(new AppCompatImageView(activity));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        MediaPicker.getInstance().getImageLoader().displayUserImage(holder.imageView, data.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                enterPosition = position;
                Intent intent = new Intent(activity, MediaPicker.getInstance().getImageLoader().displayFullImageClass() == null ? ImageViewerActivity.class : MediaPicker.getInstance().getImageLoader().displayFullImageClass());
                intent.putExtra(MediaPicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                MediaPicker.getInstance().viewerItem(data);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && MediaPicker.getInstance().isShareView()) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(v, v.getTransitionName()));
                    ActivityCompat.startActivity(activity, intent, compat.toBundle());
                    setExitSharedElementCallback();
                } else {
                    activity.startActivity(intent);
                }
            }
        });

        String name = holder.imageView.getContext().getResources().getString(R.string.share_view_photo) + position;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            holder.imageView.setTransitionName(name);
    }

    @Override
    public int getItemCount() {
        return data.size() > 9 ? 9 : data.size();
    }


    public void setImageSize(int interval, int marginLeft, int marginRight) {
        this.interval = interval;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
    }

    public void bindData(List<String> list) {
        if (MediaPicker.getInstance().getImageLoader() == null) {
            throw new RuntimeException("MediaLoader is null !");
        }
        if (list != null) {
            computeImageSize(activity, interval, marginLeft, marginRight, list.size());
            data.clear();
            data.addAll(list);
            resetLayoutManager();
            notifyDataSetChanged();
        }
    }

    private void computeImageSize(Activity activity, int interval, int marginLeft, int marginRight, int size) {
        if (size == 0) return;
        int widthPixels = Utils.getScreenPix(activity).widthPixels;
        if (size == 1) {
            column = 1;
            manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
        } else if (size <= 4) {
            column = 2;
            manager = new GridLayoutManager(activity, 2) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
        } else {
            column = 3;
            manager = new GridLayoutManager(activity, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
        }
        int width = widthPixels - marginLeft - marginRight - (column) * interval;
        mImageSize = width / column;

    }

    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            exitPosition = data.getIntExtra(MediaPicker.EXTRA_EXIT_POSITION, enterPosition);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setExitSharedElementCallback() {

        activity.setExitSharedElementCallback(new android.app.SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (exitPosition != enterPosition && names.size() > 0 && exitPosition < data.size()) {
                    names.clear();
                    sharedElements.clear();
                    View view = manager.findViewByPosition(exitPosition);
                    names.add(view.getTransitionName());
                    sharedElements.put(view.getTransitionName(), view);
                }
                activity.setExitSharedElementCallback(null);
            }
        });
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

    }

    private void resetLayoutManager() {
        recyclerView.setLayoutManager(getManager());
        recyclerView.setAdapter(this);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;

            LinearLayout.MarginLayoutParams layoutParams;
            if (column == 1)
                layoutParams = new LinearLayout.MarginLayoutParams((int) (mImageSize * (2 / 3.0f)), (int) (mImageSize * (2 / 3.0f)));
            else
                layoutParams = new LinearLayout.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize);
            layoutParams.setMargins(interval / 2, interval / 2, interval / 2, interval / 2);
            itemView.setLayoutParams(layoutParams);
            if (column == 1)
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            else
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
