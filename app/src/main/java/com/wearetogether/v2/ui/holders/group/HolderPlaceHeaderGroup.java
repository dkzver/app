package com.wearetogether.v2.ui.holders.group;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.OptionActivity;
import com.wearetogether.v2.ui.activities.PlaceActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;

public class HolderPlaceHeaderGroup extends HolderPlaceGroup {

    private final View view_count_places;
    private final View view_avatar;
    private final ImageView image_view_avatar;
    private final ImageView image_view_snapshot;
    private final TextView text_view_description;
    private final TextView text_view_count_place;
    private final View view_menu;
    private final ImageView image_view_back;
    private final ImageView image_view_menu;
    private Long user_unic;
    private View appbar;
    private TextView text_view_title;
    private int disable_comments;

    public HolderPlaceHeaderGroup(View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        appbar = itemView.findViewById(R.id.appbar);
        view_visiters = itemView.findViewById(R.id.view_visiters);
        text_view_title = itemView.findViewById(R.id.text_view_title);
        view_count_places = itemView.findViewById(R.id.view_count_places);
        view_avatar = itemView.findViewById(R.id.view_avatar);
        view_count_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.GoToUserPlaces(activity, user_unic, PlaceActivity.class);
            }
        });
        view_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.GoToUser(activity, user_unic, PlaceActivity.class);
            }
        });
        image_view_avatar = itemView.findViewById(R.id.image_view_avatar);
        image_view_snapshot = itemView.findViewById(R.id.image_view_snapshot);
        text_view_description = itemView.findViewById(R.id.text_view_description);
        text_view_count_place = itemView.findViewById(R.id.text_view_count_place);
        image_view_back = itemView.findViewById(R.id.image_view_back);
        image_view_menu = itemView.findViewById(R.id.image_view_menu);
        view_menu = itemView.findViewById(R.id.view_menu);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof PlaceActivity) {
                    PlaceActivity placeActivity = (PlaceActivity) activity;
                    placeActivity.onBackPressed();
                }
            }
        });
        view_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof OptionActivity) {
                    OptionActivity optionListener = (OptionActivity) activity;
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Consts.TYPE_PLACE_HEADER);
                    bundle.putInt("disable_comments", disable_comments);
                    bundle.putLong("place_unic", place_unic);
                    bundle.putLong("user_unic", user_unic);
                    bundle.putInt("position", getAdapterPosition());
                    optionListener.showMenu(bundle, adapterGroup.cls);
                }
            }
        });

    }

    @Override
    public void bind(DataGroup item) {
        super.bind(item);
        user_unic = item.user_unic;
        place_unic = item.unic;
        disable_comments = item.disable_comments;




        text_view_count_place.setText(String.valueOf(item.count_place));
        Spanned description = BuildPlaceDescription(activity, item);

        if (description == null) {
            text_view_description.setVisibility(View.GONE);
        } else {
            text_view_description.setText(description);
            text_view_description.setVisibility(View.VISIBLE);
        }
        Bitmap bitmap = null;
        if (item.bitmapAvatar != null) {
            bitmap = item.bitmapAvatar;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(item.bitmapAvatar, adapterGroup.sizeAvatar, adapterGroup.sizeAvatar, false);
            if (scaledBitmap != null) {
                bitmap = scaledBitmap;
                Bitmap roundedBitmap = App.GetRoundedCornerBitmap(scaledBitmap);
                if (roundedBitmap != null) {
                    bitmap = roundedBitmap;
                }
            }
        }
        if (bitmap != null) {
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

    private void setInfo(String value, TextView textView, String label) {
        if (value == null || value.equals("")) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(Html.fromHtml("<b>" + label + "</b>") + " " + value);
            textView.setVisibility(View.VISIBLE);
        }
    }
}
