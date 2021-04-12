package com.wearetogether.v2.ui.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ColorUtils;
import com.wearetogether.v2.utils.PreferenceUtils;

public class TabViewComponent extends LinearLayout implements View.OnClickListener {
    private View view_places;
    private View view_users;
    private View view_messages;
//    private View view_add;
    private View view_profile;
    private MainActivity activity;
    private Context context;

    public TabViewComponent(Context context) {
        super(context);
        initComponent();
    }

    public TabViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_view_tab, this);

        view_places = findViewById(R.id.view_places);
        view_users = findViewById(R.id.view_users);
        view_messages = findViewById(R.id.view_messages);
//        view_add = findViewById(R.id.view_add);
        view_profile = findViewById(R.id.view_profile);

        view_places.setOnClickListener(this);
        view_users.setOnClickListener(this);
        view_messages.setOnClickListener(this);
//        view_add.setOnClickListener(this);
        view_profile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        activity.getViewModel().disableRequestMutableLiveData.setValue(false);
        switch (v.getId()) {
            case R.id.view_places:
                selected(R.id.view_places);
                break;
            case R.id.view_users:
                selected(R.id.view_users);
                break;
            case R.id.view_messages:
                selected(R.id.view_messages);
                break;
            case R.id.view_add:
                selected(R.id.view_add);
                break;
            case R.id.view_profile:
                selected(R.id.view_profile);
                break;
        }
    }

    private void selected(int id) {
        switch (id) {
            case R.id.view_places:
                PreferenceUtils.SaveTab(0, activity.getApplicationContext());
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                activity.onTabSelected(0);
                break;
            case R.id.view_users:
                PreferenceUtils.SaveTab(1, activity.getApplicationContext());
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                activity.onTabSelected(1);
                break;
            case R.id.view_messages:
                PreferenceUtils.SaveTab(2, activity.getApplicationContext());
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                App.GoToRooms(activity, MainActivity.class);
                break;
            case R.id.view_add:
                PreferenceUtils.SaveTab(3, activity.getApplicationContext());
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                if (App.SUser != null) {
                    App.GoToFormPlace(activity, null, MainActivity.class);
                } else {
                    App.ShowDialogGuest(activity, context);
                }
                break;
            case R.id.view_profile:
                PreferenceUtils.SaveTab(4, activity.getApplicationContext());
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                App.GoToProfile(activity);
                break;
        }
    }

    public void setup(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        int tab = PreferenceUtils.GetTab(activity.getApplicationContext());
        switch (tab) {
            case 0:
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                activity.onTabSelected(0);
                break;
            case 1:
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                activity.onTabSelected(1);
                break;
            case 2:
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                break;
            case 3:
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                break;
            case 4:
                ((ImageView) findViewById(R.id.image_view_places)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_users)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_messages)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
//                ((ImageView) findViewById(R.id.image_view_add)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorBlack));
                ((ImageView) findViewById(R.id.image_view_profile)).setColorFilter(ColorUtils.GetColor(getContext(), R.color.colorOrange));
                break;
        }
    }

    public void update(Context context) {
        view_profile.setOnClickListener(this);
//        findViewById(R.id.image_view_profile).setVisibility(VISIBLE);
    }
}
