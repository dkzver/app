package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.smodel.SPlace;

import java.util.List;

public class DownloadPlaces implements Download {
    private List<SPlace> placeList;
    private String url_base;

    public DownloadPlaces(List<SPlace> placeList, String url_base) {
        this.placeList = placeList;
        this.url_base = url_base;
    }

    public static void Download(List<SPlace> placeList, String url_base) {
        for (int x = 0; x < placeList.size(); x++) {
            Download(placeList.get(x), url_base);
        }
    }
    public static void Download(SPlace sPlace, String url_base) {
        if(!sPlace.icon.equals("")) {
            if(!sPlace.icon.contains("http")) sPlace.icon = url_base + sPlace.icon;
        }
        Download(sPlace.getPlace(), url_base);
    }

    public static Place Download(Place place, String url_base) {
        if(place.icon != null && !place.icon.equals("")) {
            if(!place.icon.contains("http")) place.icon = url_base + place.icon;
        }
        try {
            Place old_place = null;
            old_place = App.Database.daoPlace().get(place.unic);
            if (old_place == null) {
                App.Database.daoPlace().Insert(place);
            } else {
                if (old_place.version < place.version) {
                }
                App.Database.daoPlace().update(place);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return place;
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(placeList, url_base);
    }
}
