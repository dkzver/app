package com.wearetogether.v2.ui.holders.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.*;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.dialogs.DialogAttachImage;
import com.wearetogether.v2.ui.holders.group.HolderBaseGroup;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class HolderUserProfileGroup extends HolderBaseGroup {

    private final ImageView image_view_avatar;
    private final TextView text_view_name;
    private final TextView text_view_location;
    private final TextView text_view_not_interests;
    private final TextView text_view_interests;
    private final LinearLayout view_interests;
    private final View image_view_menu;
    private final View image_view_settings;
    private final View text_view_sign;

    public HolderUserProfileGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        image_view_avatar = (ImageView) itemView.findViewById(R.id.image_view_avatar);
        text_view_name = (TextView) itemView.findViewById(R.id.text_view_name);
        text_view_location = (TextView) itemView.findViewById(R.id.text_view_location);
        this.context = activity.getApplicationContext();
        itemView.findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) activity;
                    profileActivity.onBackPressed();
                }
            }
        });
        image_view_settings = itemView.findViewById(R.id.image_view_settings);
        image_view_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity.getApplicationContext(), SettingsActivity.class));
            }
        });
        image_view_menu = itemView.findViewById(R.id.image_view_menu);
        image_view_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) activity;
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Consts.TYPE_PROFILE);
                    profileActivity.showMenu(bundle, ProfileActivity.class);
                }
            }
        });
        itemView.findViewById(R.id.fab_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.SUser != null) {
                    DialogAttachImage dialogAttachImage = new DialogAttachImage();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("create", true);
                    bundle.putBoolean("gallery", true);
                    bundle.putBoolean("album", true);
                    dialogAttachImage.setArguments(bundle);
                    dialogAttachImage.show(activity.getSupportFragmentManager(), "attach_image");
                }
            }
        });
        itemView.findViewById(R.id.view_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), AlbumActivity.class);
                activity.startActivity(intent);
            }
        });
        text_view_sign = itemView.findViewById(R.id.text_view_sign);
        text_view_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                activity.startActivity(intent);
            }
        });
        view_interests = itemView.findViewById(R.id.view_interests);
        text_view_not_interests = itemView.findViewById(R.id.text_view_not_interests);
        text_view_interests = itemView.findViewById(R.id.text_view_interests);
    }

    @Override
    public void bind(DataGroup item) {
        if(App.SUser == null) {
            image_view_menu.setVisibility(View.GONE);
            image_view_settings.setVisibility(View.GONE);
            text_view_name.setVisibility(View.GONE);
            text_view_location.setText(String.format(context.getString(R.string.format_city), item.country, item.city));
            view_interests.setVisibility(View.GONE);
            text_view_interests.setVisibility(View.GONE);
            text_view_not_interests.setVisibility(View.GONE);
            text_view_sign.setVisibility(View.VISIBLE);
        } else {
            text_view_sign.setVisibility(View.GONE);
            image_view_menu.setVisibility(View.VISIBLE);
            image_view_settings.setVisibility(View.VISIBLE);
            if (item.bitmapAvatar != null) {
                item.bitmapAvatar = Bitmap.createScaledBitmap(item.bitmapAvatar, adapterGroup.sizeProfileAvatar, adapterGroup.sizeProfileAvatar, false);
                item.bitmapAvatar = App.GetRoundedCornerBitmap(item.bitmapAvatar);
                image_view_avatar.setImageBitmap(item.bitmapAvatar);
            } else {
                image_view_avatar.setBackgroundResource(R.drawable.baseline_account_circle_black_18dp);
            }
            text_view_name.setVisibility(View.VISIBLE);
            text_view_name.setText(item.user_name);
            text_view_location.setText(String.format(context.getString(R.string.format_city), item.country, item.city));
            if(item.array_interests != null) {
                if (item.array_interests.length > 0) {
                    text_view_not_interests.setVisibility(View.GONE);
                    text_view_interests.setVisibility(View.VISIBLE);
                    view_interests.setVisibility(View.VISIBLE);
                    view_interests.removeAllViews();
                    TextView textView;
                    for (int x = 0; x < item.array_interests.length; x++) {
                        textView = new TextView(context);
                        textView.setText(item.array_interests[x]);
                        textView.setTypeface(null, Typeface.ITALIC);
                        view_interests.addView(textView);
                    }
                } else {
                    hide_view_interests();
                }
            } else {
                hide_view_interests();
            }
        }
    }

    private void hide_view_interests() {
        view_interests.setVisibility(View.GONE);
        text_view_interests.setVisibility(View.GONE);
        text_view_not_interests.setVisibility(View.VISIBLE);
    }
}
