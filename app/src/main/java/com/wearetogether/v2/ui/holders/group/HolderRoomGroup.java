package com.wearetogether.v2.ui.holders.group;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.RoomsActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

public class HolderRoomGroup extends HolderBaseGroup {
    private final TextView text_view;
    private int sizeAvatar;
    private final ImageView image_view;
    private Long unic;
    private boolean is_owner;

    public HolderRoomGroup(@NonNull @NotNull View itemView, int sizeAvatar, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        image_view = itemView.findViewById(R.id.image_view);
        text_view = itemView.findViewById(R.id.text_view);
        this.sizeAvatar = sizeAvatar;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unic != null) {
                    App.GoToRoom(activity, unic, null, RoomsActivity.class);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(activity instanceof RoomsActivity && unic != null && is_owner) {
                    ((RoomsActivity) activity).remove(unic);
                }
                return true;
            }
        });

    }

    @Override
    public void bind(DataGroup item) {
        this.unic = item.room_unic;
        if(App.SUser != null) {
            this.is_owner = item.room_owner.equals(Long.parseLong(App.SUser.unic));
        }
        text_view.setText(item.room_title);
        if (item.room_avatar != null && !item.room_avatar.equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = FileUtils.GetBitmap(item.room_avatar);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizeAvatar, sizeAvatar, false);
                                image_view.setImageBitmap(scaledBitmap);
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
