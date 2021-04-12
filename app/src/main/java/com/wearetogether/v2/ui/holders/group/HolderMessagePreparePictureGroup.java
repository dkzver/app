package com.wearetogether.v2.ui.holders.group;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class HolderMessagePreparePictureGroup extends HolderBaseGroup {
    private final View layoutChat;
    private final ImageView image_view_preloader;
    private final ProgressBar progress_bar;
    private String message_unic;

    public HolderMessagePreparePictureGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        layoutChat = itemView.findViewById(R.id.layoutChat);
        image_view_preloader = itemView.findViewById(R.id.image_view_preloader);
        progress_bar = itemView.findViewById(R.id.progress_bar);

    }

    @Override
    public void bind(DataGroup item) {
        this.message_unic = item.message_unic;
        if(item.icon != null && !item.icon.equals("")) {
            if(!item.icon.contains("upload")) {
                image_view_preloader.setImageBitmap(FileUtils.GetBitmap(item.icon));
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = FileUtils.GetBitmap(adapterGroup.url_base+item.icon);
                        adapterGroup.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(bitmap != null) {
                                    image_view_preloader.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }
}
