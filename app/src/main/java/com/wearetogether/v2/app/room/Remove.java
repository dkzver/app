package com.wearetogether.v2.app.room;

import androidx.fragment.app.FragmentActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wearetogether.v2.App;
import com.wearetogether.v2.ui.activities.RoomsActivity;

public class Remove {
    public static void Start(final FragmentActivity activity, final long unic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.Database.daoRoom().removeByUnic(unic);
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference roomRef = database.getReference().child(String.valueOf(unic));
//                roomRef.removeValue();
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        App.GoToRooms(activity, RoomsActivity.class);
//                    }
//                });
            }
        }).start();
    }
}
