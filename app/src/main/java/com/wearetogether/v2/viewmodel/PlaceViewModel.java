package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.data.DataItem;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.ui.activities.PlaceActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    public MutableLiveData<Place> mutableLiveData = new MutableLiveData<>();
    //    public MutableLiveData<String> countryMutableLiveData = new MutableLiveData<>();
//    public MutableLiveData<String> cityMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> unicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> showPhotoMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData<>();

    public PlaceViewModel() {
        showPhotoMutableLiveData.setValue(false);
    }

    public void bind(PlaceActivity activity, final long unic) {
        final long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
        unicMutableLiveData.setValue(unic);
        final String url_base = activity.getString(R.string.url_base);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Place place = PlaceVersion.Execute(unic, activity, url_base);
                if(place == null) return;
                System.out.println("place");
                System.out.println(place);

                if (App.MapCache == null) {
                    App.InitCache();
                }
                List<Place> placeList = App.Database.daoPlace().getByUserUnic(place.user_unic);
                place.bitmapAvatar = App.MapCache.get(place.user_avatar);
                System.out.println("place_user_avatar " + place.user_avatar);
                System.out.println("place_bitmapAvatar " + place.bitmapAvatar);
                if (place.bitmapAvatar == null) {
                    place.bitmapAvatar = FileUtils.GetBitmap(place.user_avatar);
                    if (place.bitmapAvatar != null) App.MapCache.put(place.user_avatar, place.bitmapAvatar);
                }
                place.mediaItemList = App.Database.daoMediaItem().getListOrderByPositionStar(unic);
                final List<Category> listCategories = App.Database.daoCategory().getAll();


                List<User> userVisiters = App.Database.daoVisit().getUsersByPlace(place.unic, 1);
                place.visiters = new ArrayList<>();
                if (userVisiters.size() > 0) {
                    Bitmap bitmap;
                    for (User visiter : userVisiters) {
                        bitmap = App.MapCache.get(visiter.avatar);
                        if (bitmap == null) {
                            bitmap = FileUtils.GetBitmap(visiter.avatar);
                            if(bitmap != null) App.MapCache.put(visiter.avatar, bitmap);
                        }
                        place.visiters.add(new DataUser(bitmap, visiter.name, visiter.unic));
                    }
                }
                final Visit visit = App.Database.daoVisit().get(place.unic, user_unic);
                final Vote vote = App.Database.daoVote().get(place.unic, user_unic);
                final Book book = App.Database.daoBook().get(place.unic);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (placeList != null) {
                            place.count_places = placeList.size();
                        } else {
                            place.count_places = 0;
                        }
                        place.is_author = user_unic == place.user_unic;
                        if (visit != null) {
                            place.visit = visit.visit;
                        } else {
                            place.visit = 0;
                        }
                        if (vote != null) {
                            place.vote = vote.vote;
                        } else {
                            place.vote = 0;
                        }
                        place.save = book == null ? 0 : 1;
                        mutableLiveData.setValue(place);
                        arrayCategoriesMutableLiveData.setValue(ListUtils.GetCategories(listCategories, activity.getApplicationContext()));
                    }
                });
            }
        }).start();
    }
}
