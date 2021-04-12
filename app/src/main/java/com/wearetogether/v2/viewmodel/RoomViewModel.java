package com.wearetogether.v2.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.*;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.RealTimeMessageData;
import com.wearetogether.v2.app.model.RealTimeRoomData;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.smodel.SMessage;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.firebase.database.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class RoomViewModel extends ViewModel {
    public MutableLiveData<Room> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<HashMap<String, Bitmap>> mapAvatarMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<HashMap<String, Boolean>> selectedMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<HashMap<String, String>> mapParticipantMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<HashMap<String, RealTimeMessageData>> mapMessagesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasModeMutableLiveData = new MutableLiveData<>();

    private FirebaseDatabase database;
    private DatabaseReference roomsRef;
    private Timer mTimer;
    private ReadTimerTask readTimerTask;


    public void write(Message message) {
        System.out.println("write");
        Room room = mutableLiveData.getValue();
        if (room != null && App.SUser != null) {
            database = FirebaseDatabase.getInstance();
            roomsRef = database.getReference().child(String.valueOf(room.unic));
            DatabaseReference messagesRef = roomsRef.child("messages");

            Map<String, Object> updates = new HashMap<>();
            updates.put(String.valueOf(message.unic), new RealTimeMessageData(message, true));
//
            messagesRef.updateChildren(updates);
        }

    }

    public void bind(final RoomActivity activity, Long room_unic) {
        mapMessagesMutableLiveData.setValue(new HashMap<>());
        if (App.SUser == null) return;
        database = FirebaseDatabase.getInstance();
        roomsRef = database.getReference().child(String.valueOf(room_unic));

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Room room = App.Database.daoRoom().get(room_unic);
                final List<RoomParticipant> roomParticipants = App.Database.daoRoomParticipant().get(room_unic);
                final HashMap<String, String> mapParticipant = new HashMap<>();
                final HashMap<String, Bitmap> mapAvatar = new HashMap<>();
                User user = null;
                String user_name = "";
                for (RoomParticipant roomParticipant : roomParticipants) {
                    System.out.println("participant " + roomParticipant.user_unic);
                    user = App.Database.daoUser().get(roomParticipant.user_unic);
                    System.out.println("user participant " + user);
                    if (user != null) {
                        user_name = user.name;
                        mapAvatar.put(String.valueOf(user.unic), FileUtils.GetBitmap(user.avatar));
                    } else {
                        user_name = "not user";
                    }
                    System.out.println("name participant " + user_name);
                    mapParticipant.put(String.valueOf(roomParticipant.user_unic), user_name);
                }
                final long user_unic = App.SUser.getUser().unic;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mapAvatarMutableLiveData.setValue(mapAvatar);
                        mapParticipantMutableLiveData.setValue(mapParticipant);
                        mutableLiveData.setValue(room);


                        DatabaseReference data = roomsRef.child("roomParticipants");
                        data.addValueEventListener(roomParticipantsListener(activity));
                        Query order = roomsRef.child("messages");
                        order.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, RealTimeMessageData> messages = new HashMap<>();
                                RealTimeMessageData realTimeMessageData = null;
                                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                                    realTimeMessageData = singleSnapshot.getValue(RealTimeMessageData.class);
                                    if (realTimeMessageData != null) {
                                        messages.put(singleSnapshot.getKey(), realTimeMessageData);
                                    }
                                }
                                mapMessagesMutableLiveData.setValue(messages);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            for (String key : messages.keySet()) {
                                                Message message = App.Database.daoMessages().get(Long.parseLong(key), room_unic);
                                                if (message == null) {
                                                    if (messages.get(key) instanceof RealTimeMessageData) {
                                                        RealTimeMessageData realTimeMessageData = messages.get(key);
                                                        if (realTimeMessageData != null) {
                                                            message = realTimeMessageData.getMessage(key, true);
                                                            message.room_unic = room_unic;
                                                            App.Database.daoMessages().insert(message);
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    }
                                }).start();
                                order.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if (snapshot.getKey() == null) return;
                                        RealTimeMessageData realTimeMessageData = snapshot.getValue(RealTimeMessageData.class);
                                        if (realTimeMessageData != null) {
                                            HashMap<String, RealTimeMessageData> messages = mapMessagesMutableLiveData.getValue();
                                            if (messages == null) {
                                                messages = new HashMap<>();
                                            }
                                            if (!messages.containsKey(snapshot.getKey())) {
                                                messages.put(snapshot.getKey(), realTimeMessageData);
                                                final long message_unic = Long.parseLong(snapshot.getKey());
                                                if (mTimer != null) {
                                                    mTimer.cancel();
                                                }
                                                if(!realTimeMessageData.user_unic.equals(user_unic)) {
                                                    mTimer = new Timer();
                                                    readTimerTask = new ReadTimerTask(activity, snapshot.getKey());
                                                    mTimer.schedule(readTimerTask, 2000);
                                                }
                                                mapMessagesMutableLiveData.setValue(messages);
//                                                roomsRef.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        System.out.println("onDataChange");
//                                                        System.out.println("child messages");
//                                                        System.out.println(snapshot);
//                                                        System.out.println(snapshot.getValue());
//                                                        System.out.println(snapshot.getValue());
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//                                                        System.out.println("onCancelled");
//                                                        System.out.println("error");
//                                                        System.out.println(error);
//                                                    }
//                                                });
                                            }
                                            System.out.println("onChildAdded " + messages);

                                        }
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        System.out.println("onChildChanged " + snapshot);
                                        System.out.println("onChildChanged " + previousChildName);
                                        if (snapshot.getKey() == null) return;
                                        RealTimeMessageData realTimeMessageData = snapshot.getValue(RealTimeMessageData.class);
                                        if (realTimeMessageData != null) {
                                            activity.adapterGroup.readMessage(snapshot.getKey());
                                        }
                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                        System.out.println("onChildRemoved " + snapshot);
                                        activity.adapterGroup.removeMessage(snapshot.getKey());
                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        System.out.println("onChildMoved " + snapshot);
                                        System.out.println("onChildMoved " + previousChildName);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        ToastUtils.Short(activity, "1 onCancelled " + error.toString());
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                ToastUtils.Short(activity, "2 onCancelled " + error.toString());
                            }
                        });
                    }
                });
            }
        }).start();

    }

    public void read(FragmentActivity activity, String key) {
        Room room = mutableLiveData.getValue();
        if (room != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference roomsRef = database.getReference().child(String.valueOf(room.unic));
            DatabaseReference messagesRef = roomsRef.child("messages");

            DatabaseReference messageRef = messagesRef.child(key);
            DatabaseReference readRef = messageRef.child("is_read");
            readRef.setValue("1");
        }
    }

    private ValueEventListener roomParticipantsListener(final RoomActivity activity) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (App.SUser == null) return;
                HashMap<String, String> mapParticipant = mapParticipantMutableLiveData.getValue();
                if (mapParticipant != null) {
                    HashMap<String, Boolean> mapInput = (HashMap<String, Boolean>) snapshot.getValue();
                    if (mapInput != null) {
                        String user_name = "not name in mapParticipant";
                        for (String key : mapInput.keySet()) {
                            if (mapInput.get(key) != null && !key.equals(App.SUser.unic)) {
                                if (mapParticipant.get(key) != null) {
                                    user_name = mapParticipant.get(key);
                                }
                                activity.adapterGroup.toggleWrite(user_name, key, mapInput.get(key));
                            }
                        }
                    }
                }
                activity.scrollToBottom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("onCancelled");
                System.out.println(error);
            }
        };
    }

    public Bitmap getAvatar(String key, Context context) {
        HashMap<String, Bitmap> mapAvatar = mapAvatarMutableLiveData.getValue();
        if (mapAvatar == null) return null;
        Bitmap bitmap = mapAvatar.get(key);
        if (bitmap != null) {
            int size = DimensionUtils.Transform(36, context);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size, size, false);
            return scaled;
        }
        return bitmap;
    }

    public void setIsInput(boolean isInput) {
        Room room = mutableLiveData.getValue();
        if (room != null && App.SUser != null) {
            database = FirebaseDatabase.getInstance();
            roomsRef = database.getReference().child(String.valueOf(room.unic));
            DatabaseReference data = roomsRef.child("roomParticipants");
            data.child(App.SUser.unic).setValue(isInput);
        }
    }

    public String getDate(FragmentActivity activity, Message message) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        date.setTime(message.unic);
        calendar.setTime(date);
        return DateUtils.formatDateTime(activity.getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
    }
}
