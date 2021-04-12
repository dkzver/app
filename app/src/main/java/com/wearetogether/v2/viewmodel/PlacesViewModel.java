package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.ui.activities.PlacesActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.ObjectUtils;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class PlacesViewModel extends ViewModel {
    public MutableLiveData<List<DataGroup>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData();

    public void bind(PlacesActivity activity, final Long author_unic, final Long user_unic) {
        final List<DataGroup> dataGroups = new ArrayList<>();
        dataGroups.add(new DataGroup().Appbar(activity.getString(R.string.title_places), PlacesActivity.class));
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> listCategories = App.Database.daoCategory().getAll();
                final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                final List<Place> placeList = App.Database.daoPlace().getByUserUnic(author_unic, 0);
                if(placeList == null || placeList.size() == 0) {
                    dataGroups.add(new DataGroup(activity.getString(R.string.not_places), DataGroup.TYPE_TEXT));
                } else {
                    for(Place place : placeList) {
                        dataGroups.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayCategoriesMutableLiveData.setValue(array_categories);
                        mutableLiveData.setValue(dataGroups);
                    }
                });
            }
        }).start();
    }
}
