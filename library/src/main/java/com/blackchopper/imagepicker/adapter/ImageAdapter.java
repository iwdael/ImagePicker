package com.blackchopper.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blackchopper.imagepicker.ImagePicker;
import com.blackchopper.imagepicker.R;
import com.blackchopper.imagepicker.ui.ImageViewerActivity;
import com.blackchopper.imagepicker.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : ImagePicker
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    @SuppressLint("NewApi")
    public ImageAdapter(Activity activity) {
        this.activity = activity;
    }

    public RecyclerView.LayoutManager getManager() {
        return manager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(new ImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImagePicker.getInstance().getImageLoader().displayImage(data.get(position), (ImageView) holder.itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                enterPosition = position;
                Intent intent = new Intent(activity, ImageViewerActivity.class);
                intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                ImagePicker.getInstance().viewerItem(data);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && ImagePicker.getInstance().isShareView()) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(v, v.getTransitionName()));
                    ActivityCompat.startActivity(activity, intent, compat.toBundle());
                    setExitSharedElementCallback();
                } else {
                    activity.startActivity(intent);
                }
            }
        });

        String name = holder.itemView.getContext().getResources().getString(R.string.share_view_photo) + position;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            holder.itemView.setTransitionName(name);
    }

    public void setImageSize(int interval, int marginLeft, int marginRight) {
        this.interval = interval;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
    }


    @Override
    public int getItemCount() {
        return data.size() > 9 ? 9 : data.size();
    }

    public void bindData(List<String> list) {
        if (ImagePicker.getInstance().getImageLoader() == null) {
            throw new RuntimeException("ImageLoader is null !");
        }
        if (list != null) {
            computeImageSize(activity, interval, marginLeft, marginRight, list.size());
            data.clear();
            data.addAll(list);
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
            exitPosition = data.getIntExtra(ImagePicker.EXTRA_EXIT_POSITION, enterPosition);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setExitSharedElementCallback() {

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
        recyclerView.setLayoutManager(getManager());
        recyclerView.setAdapter(this);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.setMargins(40, 0, 40, 0);
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageViewHolder(View itemView) {
            super(itemView);
            ViewGroup.MarginLayoutParams layoutParams;
            if (column == 1)
                layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize);
            else
                layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize);
            layoutParams.setMargins(interval / 2, interval / 2, interval / 2, interval / 2);
            itemView.setLayoutParams(layoutParams);
            ((ImageView) itemView).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
