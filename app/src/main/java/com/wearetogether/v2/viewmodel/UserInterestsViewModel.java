package com.wearetogether.v2.viewmodel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.UserInterest;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;

public class UserInterestsViewModel extends ViewModel {
    public MutableLiveData<List<Interest>> interestsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<HashMap<Integer, Boolean>> selectedMutableLiveData = new MutableLiveData<>();

    public void bind(final FragmentActivity activity) {
        if(App.SUser == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Interest> interests = App.Database.daoInterest().getAll();
                final List<UserInterest> userInterests = App.Database.daoUserInterest().get(Long.parseLong(App.SUser.unic));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<Integer, Boolean> map = new HashMap<>();
                        for(UserInterest userInterest : userInterests) {
                            map.put(userInterest.interest_id, true);
                        }
                        selectedMutableLiveData.setValue(map);
                        interestsMutableLiveData.setValue(interests);
                    }
                });
            }
        }).start();
    }
}
