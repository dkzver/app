package com.wearetogether.v2.ui.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.ui.listeners.EditorPhotoListener;
import com.wearetogether.v2.ui.listeners.PreviewListener;
import com.wearetogether.v2.utils.FileUtils;

public class HolderPhoto extends RecyclerView.ViewHolder {
    private final View view_item;
    private FragmentActivity activity;
    private final ImageView image_view_photo;
    private final TextView text_view_hint;
    private String original;
    private String icon;
    private String hint;
    private long unic;
    private int star;

    public HolderPhoto(@NonNull View itemView, FragmentActivity activity) {
        super(itemView);
        this.activity = activity;
        view_item = itemView.findViewById(R.id.view_item);
        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof PreviewListener) {
                    PreviewListener listener = (PreviewListener) activity;
                    listener.showPhoto(unic, getPosition(), original, icon);
                }
            }
        });
        view_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(activity instanceof EditorPhotoListener) {
                    EditorPhotoListener listener = (EditorPhotoListener) activity;
                    listener.showMenu(unic, getPosition(), hint, star, original, icon);
                }
                return true;
            }
        });
        image_view_photo = itemView.findViewById(R.id.image_view_photo);
        text_view_hint = itemView.findViewById(R.id.text_view_hint);
    }

    public void bind(MediaItem data) {
        if(data.icon != null && !data.icon.equals("")) {
            this.hint = data.hint;
            this.unic = data.unic;
            this.star = data.star;
            this.original = data.original;
            this.icon = data.icon;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = FileUtils.GetBitmap(data.icon);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap != null) {
                                image_view_photo.setImageBitmap(bitmap);
                            } else {
                                image_view_photo.setBackgroundResource(R.drawable.baseline_photo_black_18dp);
                            }
                        }
                    });
                }
            }).start();
        }
        if(!data.hint.equals("")) {
            text_view_hint.setText(data.hint);
            text_view_hint.setVisibility(View.VISIBLE);
        } else {
            text_view_hint.setVisibility(View.GONE);
        }
//        text_view_hint.setText(data.icon+"\n"+data.original+"\n"+data.star);
    }
}
