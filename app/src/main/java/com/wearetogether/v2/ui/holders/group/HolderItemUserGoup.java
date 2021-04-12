package com.wearetogether.v2.ui.holders.group;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.activities.OptionActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.holders.group.HolderBaseGroup;
import org.jetbrains.annotations.NotNull;

public class HolderItemUserGoup extends HolderBaseGroup implements View.OnClickListener {

    private final View view_avatar;
    private final View view_description;
    private final ImageView image_view_avatar;
    private final ImageView image_view_menu;
    private final TextView text_view_name;
    private final TextView text_view_description;
    private final TextView text_view_like;
    private final TextView text_view_location;
    private Class<?> cls;
    private int sizeAvatar;
    private String title;

    public HolderItemUserGoup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup, int sizeAvatar, Class<?> cls) {
        super(itemView, adapterGroup);
        view_avatar = itemView.findViewById(R.id.view_avatar);
        view_description = itemView.findViewById(R.id.view_description);

        image_view_avatar = itemView.findViewById(R.id.image_view_avatar);
        image_view_menu = itemView.findViewById(R.id.image_view_menu);

        text_view_name = itemView.findViewById(R.id.text_view_name);
        text_view_description = itemView.findViewById(R.id.text_view_description);
        text_view_like = itemView.findViewById(R.id.text_view_like);
        text_view_location = itemView.findViewById(R.id.text_view_location);
        this.cls = cls;

        image_view_menu.setOnClickListener(this);

        this.sizeAvatar = sizeAvatar;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long user_unic = (long) v.getTag();
                App.GoToUser(activity, user_unic, MainActivity.class);
            }
        });
    }

    @Override
    public void bind(DataGroup item) {
        this.title = item.user_name;
        itemView.setTag(item.unic);
        image_view_menu.setTag(item.unic);
        view_avatar.setVisibility(item.bitmapAvatar == null ? View.GONE : View.VISIBLE);
        text_view_name.setText(item.user_name);
        if (item.description == null || item.description.equals("")) {
            view_description.setVisibility(View.GONE);
        } else {
            text_view_description.setText(item.description);
            view_description.setVisibility(View.VISIBLE);
        }
        text_view_like.setText(String.valueOf(item.rating));

        if (item.bitmapAvatar != null) {
            Bitmap scaledBitmapAvatar = Bitmap.createScaledBitmap(item.bitmapAvatar, sizeAvatar, sizeAvatar, false);
            view_avatar.setVisibility(View.VISIBLE);
            image_view_avatar.setImageBitmap(App.GetRoundedCornerBitmap(scaledBitmapAvatar));
        } else {
            view_avatar.setVisibility(View.GONE);
        }
        text_view_location.setText(String.format(activity.getString(R.string.format_city), item.country, item.city));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_view_menu) {
            if (activity instanceof OptionActivity) {
                long user_unic = (long) v.getTag();
                OptionActivity optionListener = (OptionActivity) activity;
                Bundle bundle = new Bundle();
                bundle.putInt("type", Consts.TYPE_USER);
                bundle.putLong("place_unic", user_unic);
                bundle.putLong("user_unic", user_unic);
                bundle.putInt("position", getAdapterPosition());
                optionListener.showMenu(bundle, cls);
            }
        }
    }
}
