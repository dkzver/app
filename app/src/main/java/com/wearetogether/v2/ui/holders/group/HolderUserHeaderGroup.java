package com.wearetogether.v2.ui.holders.group;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.*;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.ToastUtils;

public class HolderUserHeaderGroup extends HolderUserGroup {

    private final View view_count_places;
    private final View view_avatar;
    private final TextView text_view_count_place;
    private final TextView text_view_description;
    private final ImageView image_view_avatar;
    private final ImageView image_view_snapshot;
    private final View view_menu;
    private final View view_buttons;
    private final ImageView image_view_back;
    private final ImageView image_view_menu;
    private Long user_unic;
    private Long unic;
    private View appbar;
    private Button button_friend;
    private TextView text_view_title;
    private final TextView text_view_not_interests;
    private final TextView text_view_interests;
    private final LinearLayout view_interests;
    private String[] array_interests;

    public HolderUserHeaderGroup(View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        appbar = itemView.findViewById(R.id.appbar);
        view_interests = itemView.findViewById(R.id.view_interests);
        text_view_not_interests = itemView.findViewById(R.id.text_view_not_interests);
        text_view_interests = itemView.findViewById(R.id.text_view_interests);
        button_friend = itemView.findViewById(R.id.button_friend);
        text_view_title = itemView.findViewById(R.id.text_view_title);
        view_count_places = itemView.findViewById(R.id.view_count_places);
        view_avatar = itemView.findViewById(R.id.view_avatar);
        view_count_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.GoToUserPlaces(activity, user_unic, PlaceActivity.class);
            }
        });
        text_view_description = (TextView) itemView.findViewById(R.id.text_view_description);
        text_view_count_place = itemView.findViewById(R.id.text_view_count_place);
        image_view_avatar = itemView.findViewById(R.id.image_view_avatar);
        image_view_snapshot = itemView.findViewById(R.id.image_view_snapshot);
        image_view_back = itemView.findViewById(R.id.image_view_back);
        image_view_menu = itemView.findViewById(R.id.image_view_menu);
        view_menu = itemView.findViewById(R.id.view_menu);
        view_buttons = itemView.findViewById(R.id.view_buttons);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof UserActivity) {
                    UserActivity userActivity = (UserActivity) activity;
                    userActivity.onBackPressed();
                }
            }
        });
        view_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof OptionActivity) {
                    OptionActivity optionListener = (OptionActivity) activity;
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Consts.TYPE_USER_HEADER);
                    bundle.putLong("place_unic", user_unic);
                    bundle.putLong("user_unic", user_unic);
                    bundle.putInt("position", getAdapterPosition());
                    optionListener.showMenu(bundle, adapterGroup.cls);
                }
            }
        });
        button_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof UserActivity) {
                    ((UserActivity) activity).friend(user_unic);
                }
            }
        });
        itemView.findViewById(R.id.button_write_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.GoToRoom(activity, null, user_unic, adapterGroup.cls);
            }
        });
    }

    @Override
    public void bind(DataGroup item) {
        user_unic = item.user_unic;
        unic = item.unic;
        array_interests = item.array_interests;

        if(activity instanceof UserActivity) {
            button_friend.setText(((UserActivity) activity).getTextFriendButton());
        }

        if(App.SUser != null) {
            if(item.user_unic == Long.parseLong(App.SUser.unic)) {
                view_buttons.setVisibility(View.GONE);
                view_menu.setVisibility(View.GONE);
//                view_count_places.setVisibility(View.GONE);
            } else {
                view_buttons.setVisibility(View.VISIBLE);
                view_menu.setVisibility(View.VISIBLE);
            }
        }

        text_view_count_place.setText(String.valueOf(item.count_place));
        Spanned description = BuildUserDescription(activity, item);

        if (description == null) {
            text_view_description.setVisibility(View.GONE);
        } else {
            text_view_description.setText(description);
            text_view_description.setVisibility(View.VISIBLE);
        }

        if (item.array_interests.length > 0) {
            text_view_not_interests.setVisibility(View.GONE);
            text_view_interests.setVisibility(View.VISIBLE);
            view_interests.setVisibility(View.VISIBLE);
            view_interests.removeAllViews();
            String interest;
            TextView textView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int x = 0; x < item.array_interests.length; x++) {
                interest = item.array_interests[x];
                textView = new TextView(context);
                if(x < item.array_interests.length - 1) {
                    interest+=",";
                } else {
                    interest+=".";
                }
                textView.setText(interest);
//                textView.setTypeface(null, Typeface.BOLD_ITALIC);
                params.setMargins(0,0,adapterGroup.rightOffsetInterest,0);
                textView.setLayoutParams(params);
                textView.setTag(x);
                textView.setOnClickListener(clickToInterest());
                view_interests.addView(textView);
            }
        } else {
            view_interests.setVisibility(View.GONE);
            text_view_interests.setVisibility(View.GONE);
            text_view_not_interests.setVisibility(View.VISIBLE);
        }


        Bitmap bitmap = null;
        if (item.bitmapAvatar != null) {
            bitmap = item.bitmapAvatar;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(item.bitmapAvatar, adapterGroup.sizeAvatar, adapterGroup.sizeAvatar, false);
            if(scaledBitmap != null) {
                bitmap = scaledBitmap;
                Bitmap roundedBitmap = App.GetRoundedCornerBitmap(scaledBitmap);
                if(roundedBitmap != null) {
                    bitmap = roundedBitmap;
                }
            }
        }
        if(bitmap != null) {
            image_view_avatar.setImageBitmap(bitmap);
        } else {
            image_view_avatar.setBackgroundResource(R.drawable.baseline_account_circle_black_18dp);
        }
        image_view_snapshot.setImageBitmap(item.bitmapMap);

        text_view_title.setText(item.title);
        float start = item.colorAppbar ? 0.0f : 1.0f;
        float end = item.colorAppbar ? 1.0f : 0.0f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(350);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float fractionAnim = (float) animation.getAnimatedValue();

                appbar.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor("#FFFFFF")
                        , Color.parseColor("#75000000")
                        , fractionAnim));

                text_view_title.setTextColor(ColorUtils.blendARGB(Color.parseColor("#75000000")
                        , Color.parseColor("#FFFFFF")
                        , fractionAnim));
                if (item.colorAppbar) {
                    image_view_back.setBackgroundResource(R.drawable.baseline_arrow_back_white_18dp);
                    image_view_menu.setBackgroundResource(R.drawable.baseline_more_vert_white_18dp);
                } else {
                    image_view_back.setBackgroundResource(R.drawable.baseline_arrow_back_black_18dp);
                    image_view_menu.setBackgroundResource(R.drawable.baseline_more_vert_black_18dp);
                }
            }
        });
        valueAnimator.start();
    }

    private View.OnClickListener clickToInterest() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) v.getTag();
                String interest = array_interests[id];
                ToastUtils.Short(adapterGroup.activity, interest);
            }
        };
    }
}
