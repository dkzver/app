package com.wearetogether.v2.viewmodel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Category;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.BackedActivity;
import com.wearetogether.v2.ui.activities.VisitedPlaceActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class VisitedPlaceViewModel extends ViewModel {
    public MutableLiveData<List<DataGroup>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData<>();

    public void bind(FragmentActivity activity, final long place_unic) {
        final List<DataGroup> dataGroupsPlaces = new ArrayList<>();
        int sizeImage = DimensionUtils.Transform(58, activity.getApplicationContext());
        final long user_unic = App.SUser != null ? Long.parseLong(App.SUser.unic) : 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> listCategories = App.Database.daoCategory().getAll();
                final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                App.Categories = ListUtils.GetCategories(App.Database.daoCategory().getAll(), activity.getApplicationContext());
                final List<User> userList = App.Database.daoVisit().getUsersByPlace(place_unic, 1);
                if (userList != null) {
                    if (userList.size() > 0) {
                        for (User user : userList) {
                            dataGroupsPlaces.add(new DataGroup().User(ObjectUtils.Build(user, user_unic), user_unic));
                        }
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayCategoriesMutableLiveData.setValue(array_categories);
                        List<DataGroup> dataGroups = new ArrayList<>();
                        dataGroups.add(new DataGroup().Appbar(activity.getString(R.string.visit_place), VisitedPlaceActivity.class));
                        if (dataGroupsPlaces.size() == 0) {
                            dataGroups.add(new DataGroup(activity.getString(R.string.not_visits), DataGroup.TYPE_TEXT));
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
