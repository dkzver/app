package com.wearetogether.v2.ui.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.FormRoomActivity;
import com.wearetogether.v2.ui.listeners.EditorPhotoListener;
import com.wearetogether.v2.ui.listeners.PreviewListener;
import com.wearetogether.v2.utils.FileUtils;

import java.util.HashMap;

public class HolderCheckedUser extends RecyclerView.ViewHolder {
    private FormRoomActivity activity;
    private final ImageView image_view;
    private final CheckBox checkbox;
    private long unic;

    public HolderCheckedUser(@NonNull View itemView, FormRoomActivity activity) {
        super(itemView);
        this.activity = activity;
        image_view = itemView.findViewById(R.id.image_view);
        checkbox = itemView.findViewById(R.id.checkbox);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox.setSelected(!checkbox.isSelected());
                activity.selected(unic, checkbox.isChecked());
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.selected(unic, isChecked);
            }
        });
    }

    public void bind(User user) {
        this.unic = user.unic;
        checkbox.setText(user.name);
        if(user.bitmap != null) {
            image_view.setImageBitmap(user.bitmap);
        } else {
            image_view.setBackgroundResource(R.drawable.baseline_photo_black_18dp);
        }
        HashMap<Long, Boolean> map = activity.getViewModel().selectedMutableLiveData.getValue();
        if(map != null) {
            if(map.containsKey(unic)) {
                checkbox.setChecked(map.get(unic));
            }
        }
    }
}
