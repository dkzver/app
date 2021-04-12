package com.wearetogether.v2.app.message;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.RealTimeMessageData;
import com.wearetogether.v2.app.model.RealTimeRoomData;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ListUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Create {
    public static void Start(final FragmentActivity activity, final Long user_unic, final Class<?> cls) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Room room = App.Database.daoRoom().getByParticipant(user_unic);
                if (room != null) {
                    final Long room_unic = room.unic;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.GoToRoom(activity, room_unic, cls);
                        }
                    });
                } else {
                    User user = App.Database.daoUser().get(user_unic);
                    if (user == null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                            }
                        });
                    } else {
                        room = new Room();
                        room.unic = Calendar.getInstance().getTimeInMillis();
                        room.owner = user_unic;
                        room.avatar = user.avatar;
                        room.title = user.name;
                        App.Database.daoRoom().insert(room);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference roomRef = database.getReference().child(String.valueOf(room.unic));
                        List<Long> longList = new ArrayList<>();
                        longList.add(user_unic);
                        longList.add(Long.parseLong(App.SUser.unic));

                        HashMap<String, Boolean> roomParticipants = new HashMap<>();
                        RoomParticipant item = null;
                        for (Long longParticipant : longList) {
                            item = new RoomParticipant();
                            item.room_unic = room.unic;
                            item.user_unic = longParticipant;
                            App.Database.daoRoomParticipant().insert(item);
                            roomParticipants.put(String.valueOf(longParticipant), false);
                        }
                        HashMap<String, RealTimeMessageData> messages = new HashMap<>();
                        roomRef.setValue(new RealTimeRoomData(room, messages, roomParticipants));

                        final Long room_unic = room.unic;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                App.GoToRoom(activity, room_unic, cls);
                            }
                        });
                    }
                }


            }
        }).start();
    }
}
