package com.wearetogether.v2.app.room;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.RealTimeMessageData;
import com.wearetogether.v2.app.model.RealTimeRoomData;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.services.GCMSenderService;
import com.wearetogether.v2.smodel.SRoomParticipant;
import com.wearetogether.v2.ui.activities.FormRoomActivity;
import com.wearetogether.v2.ui.activities.RoomsActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class Add {
    private static String error = "";
    private static List<Long> longList = new ArrayList<>();

    public static void Start(FormRoomActivity activity, Context context) {
        if (hasCreate(activity)) {
            try {
                createRoom(activity);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.Short(activity, e.getMessage());
            }
        } else {
            showDialogError(activity, context);
        }
    }

    private static boolean hasCreate(FormRoomActivity activity) {
        if(App.SUser == null) {
            error = "Error user";
            return false;
        }
        HashMap<Long, Boolean> mapSelected = activity.getViewModel().selectedMutableLiveData.getValue();
        if (mapSelected == null) {
            error = "Error selected";
            return false;
        } else {
            for (Long unic : mapSelected.keySet()) {
                if (mapSelected.get(unic)) {
                    longList.add(unic);
                }
            }
            if (longList.size() == 0) {
                error = "Error selected";
                return false;
            }
        }
        return true;
    }

    private static void createRoom(FormRoomActivity activity) throws Exception {
        if(App.SUser == null) return;
        final String avatar = App.SUser.avatar;
        final String title = App.SUser.name;
        final Long user_unic = Long.valueOf(App.SUser.unic);
        longList.add(user_unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> tokens = new ArrayList<>();
                for (Long unic : longList) {
                    if(!unic.equals(user_unic)) {
                        User user = App.Database.daoUser().get(unic);
                        if (user != null) {
                            tokens.add(user.token);
                        }
                    }
                }
                final Room room = new Room();
                room.unic = Calendar.getInstance().getTimeInMillis();
                room.owner = user_unic;
                room.avatar = avatar;
                room.title = title;
                App.Database.daoRoom().insert(room);

                HashMap<String, Boolean> roomParticipants = new HashMap<>();
                RoomParticipant item = null;
                final List<SRoomParticipant> sRoomParticipants = new ArrayList<>();
                for (Long longParticipant : longList) {
                    item = new RoomParticipant();
                    item.room_unic = room.unic;
                    item.user_unic = longParticipant;
                    App.Database.daoRoomParticipant().insert(item);
                    roomParticipants.put(String.valueOf(longParticipant), false);
                    sRoomParticipants.add(item.get());
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference roomRef = database.getReference().child(String.valueOf(room.unic));
                HashMap<String, RealTimeMessageData> messages = new HashMap<>();
                roomRef.setValue(new RealTimeRoomData(room, messages, roomParticipants));

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        DataJsonMessagingServiceAddRoom json = new DataJsonMessagingServiceAddRoom();
//
//                        json.user = App.SUser;
//                        json.room = room.get();
//                        json.room.is_owner = "0";
//                        json.roomParticipants = sRoomParticipants;
//
//                        DataMessagingServiceAddRoom data = new DataMessagingServiceAddRoom();
//                        data.action = Consts.NEW_ROOM;
//                        data.json = json;
//                        RootMessagingServiceAddRoom rootMessagingServiceAddRoom = new RootMessagingServiceAddRoom();
//                        rootMessagingServiceAddRoom.registration_ids = new ArrayList<>();
//                        rootMessagingServiceAddRoom.registration_ids.addAll(tokens);
//                        rootMessagingServiceAddRoom.data = data;
//                        Gson gson = new Gson();
//                        String json_string = gson.toJson(rootMessagingServiceAddRoom, RootMessagingServiceAddRoom.class);
//                        GCMSenderService.StartServer(activity, json_string);
                        App.IsUpdate = true;
                        activity.back();
                    }
                });
            }
        }).start();
    }

    private static void showDialogError(FragmentActivity activity, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(error);
        builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
