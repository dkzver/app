package com.wearetogether.v2.viewmodel;

import android.content.Context;
import android.text.format.DateUtils;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.DateValidatorUsingDateFormat;
import com.wearetogether.v2.database.model.Status;
import com.wearetogether.v2.ui.activities.EditProfileActivity;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditProfileViewModel extends ViewModel {
    public MutableLiveData<String[]> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Calendar> calendarMutableLiveData = new MutableLiveData<>();

    public void bind(final EditProfileActivity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Status> listStatuses = App.Database.daoStatus().getAll();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String date_birth = App.SUser.date_birth;
                        DateValidatorUsingDateFormat dateValidatorUsingDateFormat = new DateValidatorUsingDateFormat("yyyy-MM-dd");
                        if(!dateValidatorUsingDateFormat.isValid(date_birth)) {//date_birth == null || (date_birth.equals("") || date_birth.equals("0000-00-00"))
                            calendarMutableLiveData.setValue(null);
                        } else {
                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = simpleDateFormat.parse(date_birth);
                                if(date != null) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    calendarMutableLiveData.setValue(calendar);
                                }
                            } catch (Exception e) {
                                ToastUtils.Short(activity.getApplicationContext(), e.getMessage());
                            }
                        }
                        String[] status_array = ListUtils.GetStatuses(listStatuses, activity.getApplicationContext());
                        mutableLiveData.setValue(status_array);
                    }
                });
            }
        }).start();
    }
}
