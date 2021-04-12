package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.app.DateValidatorUsingDateFormat;
import com.wearetogether.v2.database.model.Category;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.MapUtils;
import com.google.android.gms.maps.model.LatLng;

import java.net.HttpCookie;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormPlaceViewModel extends ViewModel {
    public MutableLiveData<Place> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoryMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<MediaItem>> listMediaItemMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> tempUnicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> unicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> moreMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> showMapMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> showPhotoMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<LatLng> latLngMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Calendar> calendarTimeVisitMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Calendar> calendarDateBeginMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Calendar> calendarDateEndMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> stateGalleryMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> locationMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Bitmap> snapshotMutableLiveData = new MutableLiveData<>();
    private DateValidatorUsingDateFormat dateValidatorUsingDateFormat;

    public FormPlaceViewModel() {
        moreMutableLiveData.setValue(false);
        showMapMutableLiveData.setValue(false);
        showPhotoMutableLiveData.setValue(false);
    }

    public void setLocatin(String locatin, int id) {
        System.out.println("set location " + id + " " + locatin);
        this.locationMutableLiveData.setValue(locatin);
    }

    public void bind(final FormPlaceActivity activity, final Long unic) {
        if(App.SUser == null) return;
        tempUnicMutableLiveData.setValue(Calendar.getInstance().getTimeInMillis());
        unicMutableLiveData.setValue(unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                LatLng latLng;
                String dateBegin;
                String dateEnd;
                String timeVisit;
                String address = App.SUser.location;
                final String[] categories = ListUtils.GetCategories(App.Database.daoCategory().getAll(), activity.getApplicationContext());
                final Place place = App.Database.daoPlace().get(unic);
                List<MediaItem> mediaItemList = new ArrayList<>();
                if (place != null) {
                    latLng = new LatLng(place.latitude, place.longitude);
                    mediaItemList.addAll(App.Database.daoMediaItem().getListOrderByPositionStar(unic));
                    timeVisit = place.time_visit;
                    dateBegin = place.date_begin;
                    dateEnd = place.date_end;
                    address = place.address;
                } else {
                    latLng = MapUtils.LocationToLatLng(Double.parseDouble(App.SUser.latitude), Double.parseDouble(App.SUser.longitude));
                    timeVisit = null;
                    dateBegin = null;
                    dateEnd = null;
                }
                final String location = address;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayCategoryMutableLiveData.setValue(categories);
                        latLngMutableLiveData.setValue(latLng);
                        setLocatin(location, 2);
                        attach(timeVisit, "HH:mm", "timeVisit");
                        attach(dateBegin, "yyyy-MM-dd", "dateBegin");
                        attach(dateEnd, "yyyy-MM-dd", "dateEnd");
                        mutableLiveData.setValue(place);
                        listMediaItemMutableLiveData.setValue(mediaItemList);
                    }
                });
            }
        }).start();
    }

    private void attach(String value, String format, String key) {
        Calendar calendar = null;
        dateValidatorUsingDateFormat = new DateValidatorUsingDateFormat(format);
        if (dateValidatorUsingDateFormat.isValid(value)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        switch (key) {
            case "timeVisit":
                calendarTimeVisitMutableLiveData.setValue(calendar);
                break;
            case "dateBegin":
                calendarDateBeginMutableLiveData.setValue(calendar);
                break;
            case "dateEnd":
                calendarDateEndMutableLiveData.setValue(calendar);
                break;
        }
    }
}
