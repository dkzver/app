package com.wearetogether.v2.viewmodel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.MediaItem;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class AlbumViewModel extends ViewModel {
    public MutableLiveData<List<MediaItem>> mutableLiveData = new MutableLiveData<>();

    public void bind(final FragmentActivity activity, final long item_unic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<MediaItem> mediaItems = App.Database.daoMediaItem().getListOrderByPositionStar(item_unic);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mutableLiveData.setValue(mediaItems);
                    }
                });
            }
        }).start();
    }

    public void setup() {
        mutableLiveData.setValue(new ArrayList<>());
    }
}
