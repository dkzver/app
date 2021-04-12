package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
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
import com.wearetogether.v2.ui.activities.BackedActivity;
import com.wearetogether.v2.ui.activities.FriendsActivity;
import com.wearetogether.v2.ui.activities.VisitsActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VisitsViewModel extends ViewModel {
    public MutableLiveData<List<DataGroup>> visitMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DataGroup>> anonimVisitMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DataGroup>> savedMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData<>();

    public void bind(final VisitsActivity activity, final int defSelected) {
        final List<DataGroup> visitGroupsPlaces = new ArrayList<>();
        final List<DataGroup> anonimVisitGroupsPlaces = new ArrayList<>();
        final List<DataGroup> savedGroupsPlaces = new ArrayList<>();
        if(App.SUser == null) return;
        long user_unic = Long.parseLong(App.SUser.unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> listCategories = App.Database.daoCategory().getAll();
                final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                for (Place place : App.Database.daoVisit().getUserVisits(1, user_unic)) {
                    place.visit = 1;
                    place.save = 0;
                    visitGroupsPlaces.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                }
                for (Place place : App.Database.daoVisit().getUserVisits(-1, user_unic)) {
                    place.visit = -1;
                    place.save = 0;
                    anonimVisitGroupsPlaces.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                }
                List<Place> saved = App.Database.daoBook().getAll();
                for(Place place : saved) {
                    place.visit = 0;
                    place.save = 1;
                    savedGroupsPlaces.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayCategoriesMutableLiveData.setValue(array_categories);
                        visitMutableLiveData.setValue(visitGroupsPlaces);
                        anonimVisitMutableLiveData.setValue(anonimVisitGroupsPlaces);
                        savedMutableLiveData.setValue(savedGroupsPlaces);
                        selectedMutableLiveData.setValue(defSelected);
                    }
                });
            }
        }).start();
    }
}
