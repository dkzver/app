package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.FriendsActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendsViewModel extends ViewModel {
    public MutableLiveData<List<DataGroup>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedMutableLiveData = new MutableLiveData<>();

    public void bind(final FriendsActivity activity, final long user_unic) {
        final List<DataGroup> dataGroupsUsers = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> friends = new ArrayList<>();
                for (User user : App.Database.daoFriends().getFriends(Friend.REQUEST_FRIEND)) {
                    user.friend = Friend.REQUEST_FRIEND;
                    friends.add(user);
                }
                for (User user : App.Database.daoFriends().getFriends(Friend.SEND_REQUEST_FRIEND)) {
                    user.friend = Friend.SEND_REQUEST_FRIEND;
                    friends.add(user);
                }
                for (User user : App.Database.daoFriends().getFriends(Friend.FRIEND)) {
                    user.friend = Friend.FRIEND;
                    friends.add(user);
                }
                for (int x = 0; x < friends.size(); x++) {
                    dataGroupsUsers.add(new DataGroup().User(ObjectUtils.Build(friends.get(x), user_unic), user_unic));
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mutableLiveData.setValue(dataGroupsUsers);
                        selectedMutableLiveData.setValue(0);
                    }
                });
            }
        }).start();
    }
}
