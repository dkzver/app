package com.wearetogether.v2.viewmodel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.*;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.utils.FileUtils;

import java.util.HashMap;
import java.util.List;

public class FormRoomViewModel extends ViewModel {
    public MutableLiveData<HashMap<Long, Boolean>> selectedMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Room> mutableLiveData = new MutableLiveData<>();

    public void bind(final FragmentActivity activity, final Long unic) {
        if(App.SUser == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<User> users = App.Database.daoUser().getAll(Long.parseLong(App.SUser.unic));
                for(User user : users) {
                    user.bitmap = FileUtils.GetBitmap(user.avatar);
                }
                final Room room = unic == null ? null : App.Database.daoRoom().get(unic);
                final HashMap<Long, Boolean> mapSelected = new HashMap<>();
                if(room != null) {
                    List<RoomParticipant> roomParticipantList = App.Database.daoRoomParticipant().get(room.unic);
                    if(roomParticipantList != null) {
                        for(RoomParticipant roomParticipant : roomParticipantList) {
                            mapSelected.put(roomParticipant.user_unic, true);
                        }
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectedMutableLiveData.setValue(mapSelected);
                        usersMutableLiveData.setValue(users);
                        mutableLiveData.setValue(room);
                    }
                });
            }
        }).start();
    }
}
