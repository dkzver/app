package com.wearetogether.v2.app.message;

import android.app.Dialog;
import android.content.DialogInterface;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Remove {
    public static void Start(final RoomActivity activity, final List<Long> longList, final Long room_unic, final DialogInterface dialog) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference roomsRef = database.getReference().child(String.valueOf(room_unic));
                DatabaseReference messagesRef = roomsRef.child("messages");
                for(Long unic : longList) {
                    messagesRef.child(String.valueOf(unic)).removeValue();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("remove");
                        System.out.println(longList);
                        System.out.println("remove");

                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
