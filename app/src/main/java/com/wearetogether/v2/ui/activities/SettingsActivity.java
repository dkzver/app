package com.wearetogether.v2.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.user.Logout;
import com.wearetogether.v2.app.user.Settings;
import com.wearetogether.v2.utils.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {

    public CheckBox checkbox_show_sex;
    public Spinner spinner_show_in_map;
    public CheckBox checkbox_show_age;
    public CheckBox checkbox_enable_sound_notification;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(App.SUser == null) {
            back();
        } else {
            final FragmentActivity activity = this;
            findViewById(R.id.view_logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logout.showDialog(activity, SettingsActivity.this);
                }
            });
            findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            checkbox_enable_sound_notification = (CheckBox) findViewById(R.id.checkbox_enable_sound_notification);
            checkbox_enable_sound_notification.setChecked(PreferenceUtils.getEnableSoundNotification(getApplicationContext()));
            checkbox_show_sex = (CheckBox) findViewById(R.id.checkbox_show_sex);
//            checkbox_show_in_map = (CheckBox) findViewById(R.id.checkbox_show_in_map);
            checkbox_show_age = (CheckBox) findViewById(R.id.checkbox_show_age);
            checkbox_show_sex.setChecked(App.SUser.show_sex != null && App.SUser.show_sex.equals("1"));
//            checkbox_show_in_map.setChecked(App.SUser.show_in_map != null && App.SUser.show_in_map.equals("1"));
            spinner_show_in_map = findViewById(R.id.spinner_show_in_map);
            final String[] options = getResources().getStringArray(R.array.options_show_in_map);
            adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_show_in_map.setAdapter(adapter);
            spinner_show_in_map.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position < options.length) {
                        spinner_show_in_map.setTag(String.valueOf(position));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            int show_in_map = App.SUser.show_in_map != null ? Integer.parseInt(App.SUser.show_in_map) : 0;
            spinner_show_in_map.setTag(String.valueOf(show_in_map));
            spinner_show_in_map.setSelection(show_in_map);
            checkbox_enable_sound_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PreferenceUtils.SaveEnableSoundNotification(isChecked, getApplicationContext());
                }
            });
            checkbox_show_age.setChecked(App.SUser.show_age != null && App.SUser.show_age.equals("1"));

            findViewById(R.id.button_edit_interests).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity.getApplicationContext(), InterestsActivity.class));
                }
            });
            findViewById(R.id.button_edit_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity.getApplicationContext(), EditProfileActivity.class));
                }
            });
//            findViewById(R.id.button_add_place).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    App.GoToFormPlace(activity, null, SettingsActivity.class);
//                }
//            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Settings.Start(this);
    }

    public void back() {
        super.onBackPressed();
    }
}