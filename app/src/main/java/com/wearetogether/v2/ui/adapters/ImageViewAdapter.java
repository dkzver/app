package com.wearetogether.v2.ui.adapters;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import com.wearetogether.v2.App;
import com.wearetogether.v2.ui.components.photoview.PhotoView;
import com.wearetogether.v2.ui.listeners.PreviewListener;
import com.wearetogether.v2.utils.FileUtils;

public class ImageViewAdapter  extends PagerAdapter {

    private PreviewListener listener;
    private FragmentActivity activity;

    public ImageViewAdapter(FragmentActivity activity) {

        this.activity = activity;
        if(activity instanceof PreviewListener) {
            this.listener = (PreviewListener) activity;
        }
    }

    @Override
    public int getCount() {
        return listener.getList().size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        if(App.MapCache == null) {
            App.InitCache();
        }
        final PhotoView photoView = new PhotoView(container.getContext());
        final String original = listener.getList().get(position).original;
        Bitmap bitmapOriginal = App.MapCache.get(original);
        listener.showProgressBar(true);
        if (original.contains("http")) {
            final String small = listener.getList().get(position).small;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = FileUtils.GetBitmap(small);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
//                                                ToastUtils.Short(activity.getApplicationContext(), "small: " + small);
                                photoView.setImageBitmap(bitmap);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final Bitmap bitmap = FileUtils.GetBitmap(original);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (bitmap != null) {
//                                                                ToastUtils.Short(activity.getApplicationContext(), "original: " + original);
                                                photoView.setImageBitmap(bitmap);
                                                listener.showProgressBar(false);
                                                App.MapCache.put(original, bitmap);
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                }
            }).start();
        } else {
            System.out.println("Zoom");
            System.out.println(original);
            if (bitmapOriginal == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = FileUtils.GetBitmap(original);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(bitmap != null) {
                                    photoView.setImageBitmap(bitmap);
                                    listener.showProgressBar(false);
                                    App.MapCache.put(original, bitmap);
                                }
                            }
                        });
                    }
                }).start();
            } else {
                photoView.setImageBitmap(bitmapOriginal);
            }
        }
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    public void update() {
        notifyDataSetChanged();
    }
}

