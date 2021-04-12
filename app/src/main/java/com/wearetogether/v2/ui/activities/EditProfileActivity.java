package com.wearetogether.v2.ui.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.user.Profile;
import com.wearetogether.v2.ui.dialogs.DialogSelect;
import com.wearetogether.v2.ui.listeners.SelectListener;
import com.wearetogether.v2.viewmodel.EditProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.*;

public class EditProfileActivity extends AppCompatActivity {

    public EditText edit_text_login;
    public EditText edit_text_first_name;
    public EditText edit_text_last_name;
    public View view_sex;
    private TextView text_view_sex;
    public View view_status;
    private TextView text_view_status;
    public View view_custom_status;
    public EditText edit_text_custom_status;
    public View view_date_birth;
    private EditProfileViewModel editProfileViewModel;
    private TextView text_view_date_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if(App.SUser == null) {
            back();
        }
        findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edit_text_login = (EditText) findViewById(R.id.edit_text_login);
        edit_text_first_name = (EditText) findViewById(R.id.edit_text_first_name);
        edit_text_last_name = (EditText) findViewById(R.id.edit_text_last_name);
        view_sex = findViewById(R.id.view_sex);
        text_view_sex = (TextView) findViewById(R.id.text_view_sex);
        view_status = findViewById(R.id.view_status);
        text_view_status = (TextView) findViewById(R.id.text_view_status);
        view_custom_status = findViewById(R.id.view_custom_status);
        edit_text_custom_status = (EditText) findViewById(R.id.edit_text_custom_status);
        view_date_birth = findViewById(R.id.view_date_birth);
        text_view_date_birth = findViewById(R.id.text_view_date_birth);
        view_date_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar_date_birth = editProfileViewModel.calendarMutableLiveData.getValue();
                if(calendar_date_birth == null) {
                    calendar_date_birth = Calendar.getInstance();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar_date_birth = editProfileViewModel.calendarMutableLiveData.getValue();
                        if(calendar_date_birth == null) {
                            calendar_date_birth = Calendar.getInstance();
                        }
                        calendar_date_birth.set(Calendar.YEAR, year);
                        calendar_date_birth.set(Calendar.MONTH, month);
                        calendar_date_birth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDateText(calendar_date_birth);
                    }
                },
                        calendar_date_birth.get(Calendar.YEAR),
                        calendar_date_birth.get(Calendar.MONTH),
                        calendar_date_birth.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        setDateText(null);
                    }
                });
                datePickerDialog.show();
            }
        });

        editProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        if (savedInstanceState == null) {
            editProfileViewModel.bind(this);
        }
        editProfileViewModel.calendarMutableLiveData.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                setDateText(calendar);
            }
        });
        editProfileViewModel.mutableLiveData.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] status_array) {
                edit_text_login.setText(App.SUser.name);
                view_sex.setTag(App.SUser.sex);
                String[] sex_array = getResources().getStringArray(R.array.sex_array);
                view_sex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogSelect(sex_array, v, "sex");
                    }
                });
                text_view_sex.setText(getString(R.string.label_sex) + " " + sex_array[Integer.parseInt(App.SUser.sex)]);

                view_status.setTag(App.SUser.status);
                view_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogSelect(status_array, v, "status");
                    }
                });
                if (App.SUser.status.equals("0")) {
                    view_custom_status.setVisibility(View.VISIBLE);
                    edit_text_custom_status.setText(App.SUser.custom_status);
                } else {
                    view_custom_status.setVisibility(View.GONE);
                }
                int status_id = Integer.parseInt(App.SUser.status);
                if(status_id < status_array.length) {
                    text_view_status.setText(getString(R.string.label_status) + " " +  status_array[status_id]);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setDateText(Calendar calendar) {
        if(calendar == null) {
            text_view_date_birth.setText(getString(R.string.label_date_birth) + " " + getString(R.string.select));
            text_view_date_birth.setTag(null);
        } else {
            String text = DateUtils.formatDateTime(getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
            text_view_date_birth.setText(getString(R.string.label_date_birth) + " " + text);
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Consts.Format_yyyy_MM_dd);//yyyy-MM-dd
                text = simpleDateFormat.format(calendar.getTime());
                view_date_birth.setTag(text);
            } catch (Exception e) {
                e.printStackTrace();
                view_date_birth.setTag(null);
            }
        }
    }

    private void showDialogSelect(String[] array, View v, String key) {
        DialogSelect dialogSelect = new DialogSelect();
        dialogSelect.listener = new SelectListener() {
            @Override
            public void onSelect(int position) {
                switch (key) {
                    case "sex":
                        view_sex.setTag(String.valueOf(position));
                        String[] sex_array = getResources().getStringArray(R.array.sex_array);
                        text_view_sex.setText(getString(R.string.label_sex) + " " + sex_array[position]);
                        break;
                    case "status":
                        if(position == 0) {
                            view_custom_status.setVisibility(View.VISIBLE);
                        } else {
                            view_custom_status.setVisibility(View.GONE);
                        }
                        if(editProfileViewModel == null) return;
                        if(editProfileViewModel.mutableLiveData == null) return;
                        String[] array = editProfileViewModel.mutableLiveData.getValue();
                        if(array != null) {
                            view_status.setTag(String.valueOf(position));
                            text_view_status.setText(getString(R.string.label_status) + " " + array[position]);
                        }
                        break;
                }
            }
        };
        Bundle bundle = new Bundle();
        bundle.putStringArray("options", array);
        if(v.getTag() != null) {
            bundle.putInt("selected", Integer.parseInt((String) v.getTag()));
        }
        dialogSelect.setArguments(bundle);
        dialogSelect.show(getSupportFragmentManager(), key);
    }

    @Override
    public void onBackPressed() {
        Profile.Start(this);
    }

    public void back() {
        super.onBackPressed();
    }
}