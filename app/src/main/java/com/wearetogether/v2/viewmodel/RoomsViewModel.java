package com.wearetogether.v2.viewmodel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.database.model.Vote;
import com.wearetogether.v2.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomsViewModel extends ViewModel {

    public MutableLiveData<List<Room>> mutableLiveData = new MutableLiveData<>();

    public void bind(final FragmentActivity activity) {
        if(App.SUser == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Room> rooms = App.Database.daoRoom().getAll();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mutableLiveData.setValue(rooms);
                    }
                });
            }
        }).start();
    }
}
