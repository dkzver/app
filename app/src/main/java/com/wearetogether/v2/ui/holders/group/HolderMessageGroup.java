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
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ReadTimerTask;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.wearetogether.v2.ui.activities.RoomsActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.viewmodel.RoomViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class HolderMessageGroup extends HolderBaseGroup {
    private final View layoutChat;
    private final TextView text_view;
    private final TextView text_view_date;
    private final ImageView view_read;
    private String message_unic;
    private Timer mTimer;
    private ReadTimerTask readTimerTask;

    public HolderMessageGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        layoutChat = itemView.findViewById(R.id.layoutChat);
        text_view = itemView.findViewById(R.id.text_view);
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
        text_view.setText(item.message_text);//item.selected + " - " +
//        text_view_date.setText(item.message_datetime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.message_date);
        text_view_date.setText(DateUtils.formatDateTime(activity.getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
//        Bitmap bitmap = activity.getViewModel().getAvatar(item.message_user_unic, activity.getApplicationContext());
//        if(bitmap != null) {
//            image_view.setImageBitmap(bitmap);
//            image_view.setVisibility(View.VISIBLE);
//        } else {
//            image_view.setVisibility(View.GONE);
//        }
    }
}
