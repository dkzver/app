package com.wearetogether.v2.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.util.DisplayMetrics;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.SMediaItem;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.activities.ProfileActivity;
import com.wearetogether.v2.utils.*;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileViewModel extends ViewModel {
    public MutableLiveData<List<DataGroup>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData<>();

    public void bind(FragmentActivity activity) {
        if (App.SUser != null) {
            final List<DataGroup> dataGroupsPlaces = new ArrayList<>();
            final long user_unic = Long.parseLong(App.SUser.unic);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Category> listCategories = App.Database.daoCategory().getAll();
                    final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                    App.Categories = ListUtils.GetCategories(App.Database.daoCategory().getAll(), activity.getApplicationContext());
                    final List<Place> placeList = App.Database.daoPlace().getByUserUnic(user_unic, 0);
                    List<UserInterest> userInterests = App.Database.daoUserInterest().get(user_unic);
                    List<Interest> interestList = new ArrayList<>();
                    for (UserInterest userInterest : userInterests) {
                        interestList.add(App.Database.daoUserInterest().get(userInterest.interest_id));
                    }
                    final String[] arrayIntrests = ListUtils.GetInterests(interestList, activity.getApplicationContext());
                    if (placeList != null) {
                        if (placeList.size() > 0) {
                            for (Place place : placeList) {
                                dataGroupsPlaces.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                            }
                        }
                    }
                    try {
                        Bitmap avatar = FileUtils.GetBitmap(App.SUser.avatar);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayCategoriesMutableLiveData.setValue(array_categories);
                                List<DataGroup> dataGroups = new ArrayList<>();
                                dataGroups.add(new DataGroup().Profile(App.SUser.getUser(), avatar, arrayIntrests));
                                if(dataGroupsPlaces.size() == 0) {
                                    dataGroups.add(new DataGroup(activity.getString(R.string.not_places), DataGroup.TYPE_TEXT));
                                } else {
                                    dataGroups.add(new DataGroup(activity.getString(R.string.places_user), DataGroup.TYPE_HEADER));
                                    dataGroups.addAll(dataGroupsPlaces);
                                }
                                mutableLiveData.setValue(dataGroups);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
            String location = sharedPreferences.getString(Consts.LOCATION, "");
            String country = sharedPreferences.getString(Consts.COUNTRY, "");
            String city = sharedPreferences.getString(Consts.CITY, "");
            List<DataGroup> dataGroups = new ArrayList<>();
            dataGroups.add(new DataGroup().Profile(country, city, location));
            mutableLiveData.setValue(dataGroups);
        }
    }
}
