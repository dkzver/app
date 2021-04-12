package com.wearetogether.v2.ui.holders.group;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ReadTimerTask;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Timer;

public class HolderMessagePictureGroup extends HolderBaseGroup {
    private final View layoutChat;
    private final ImageView image_view;
    private final TextView text_view_date;
    private final ImageView view_read;
    private String message_unic;
    private Timer mTimer;
    private ReadTimerTask readTimerTask;

    public HolderMessagePictureGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        layoutChat = itemView.findViewById(R.id.layoutChat);
        image_view = itemView.findViewById(R.id.image_view);
        text_view_date = itemView.findViewById(R.id.text_view_date);
        view_read = itemView.findViewById(R.id.view_read);
        if (activity instanceof RoomActivity) {
            RoomActivity roomActivity = (RoomActivity) activity;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    roomActivity.selected(message_unic);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean hasMode = roomActivity.getViewModel().hasModeMutableLiveData.getValue();
                    if (hasMode != null && hasMode) {
                        roomActivity.selected(message_unic);
                    }
                }
            });
        }

    }

    @Override
    public void bind(DataGroup item) {
        this.message_unic = item.message_unic;
        if (item.selected == 1) {
            layoutChat.setBackgroundColor(Color.parseColor("#75000000"));
        } else {
            layoutChat.setBackgroundColor(Color.parseColor("#00000000"));
        }
        if (view_read != null && item.is_read != null) {
            if (item.is_read.equals("1")) {
                view_read.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                view_read.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00BFFF")));
            } else {
                view_read.setImageTintList(null);
            }
        }
        if(item.icon != null && !item.icon.equals("")) {
            if(!item.icon.contains("upload")) {
                image_view.setImageBitmap(FileUtils.GetBitmap(item.icon));
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = FileUtils.GetBitmap(adapterGroup.url_base+item.icon);
                        adapterGroup.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(bitmap != null) {
                                    image_view.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                }).start();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.message_date);
        text_view_date.setText(DateUtils.formatDateTime(activity.getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
        text_view_date.setText("icon");
    }
}
