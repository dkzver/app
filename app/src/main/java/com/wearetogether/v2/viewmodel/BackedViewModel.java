package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.SMediaItem;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.ui.activities.BackedActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.ObjectUtils;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class BackedViewModel extends ViewModel {
    public MutableLiveData<List<DataGroup>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData<>();

    public void bind(FragmentActivity activity, final long user_unic) {
        final List<DataGroup> dataGroupsPlaces = new ArrayList<>();
        int sizeImage = DimensionUtils.Transform(58, activity.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> listCategories = App.Database.daoCategory().getAll();
                final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                App.Categories = ListUtils.GetCategories(App.Database.daoCategory().getAll(), activity.getApplicationContext());
                final List<Place> placeList = App.Database.daoPlace().getByUserUnic(user_unic, 1);
                if (placeList != null) {
                    if (placeList.size() > 0) {
                        for (Place place : placeList) {
                            dataGroupsPlaces.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                        }
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayCategoriesMutableLiveData.setValue(array_categories);
                        List<DataGroup> dataGroups = new ArrayList<>();
                        dataGroups.add(new DataGroup().Appbar(activity.getString(R.string.backed), BackedActivity.class));
                        dataGroups.add(new DataGroup(activity.getString(R.string.places_user), DataGroup.TYPE_HEADER));
                        if (dataGroupsPlaces.size() == 0) {
                            dataGroups.add(new DataGroup(activity.getString(R.string.not_places), DataGroup.TYPE_TEXT));
                        } else {
                            dataGroups.addAll(dataGroupsPlaces);
                        }
                        mutableLiveData.setValue(dataGroups);
                    }
                });
            }
        }).start();
    }
}
