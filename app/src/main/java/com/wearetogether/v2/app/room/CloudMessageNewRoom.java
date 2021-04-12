package com.wearetogether.v2.app.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.*;
import com.wearetogether.v2.database.model.NotificationItem;
import com.wearetogether.v2.datacloudmessaging.CloudMessage;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.NotificationUtils;
import com.google.gson.Gson;

import java.util.Map;

public class CloudMessageNewRoom extends CloudMessage {

    public CloudMessageNewRoom(Context context) {
        super(context);
    }

    private Integer notification_id = null;

    @Override
    public void execute(Map<String, String> data) {
        final String json = data.get("json");
        System.out.println("CloudMessageNewRoom " + data);
        System.out.println("data " + data);
        System.out.println("data " + data);
        System.out.println("CloudMessageNewRoom " + data);
        Gson gson = new Gson();
        final DataResponseNewRoomCloudMessaging dataResponseNewRoomCloudMessaging = gson.fromJson(json, DataResponseNewRoomCloudMessaging.class);
        System.out.println(dataResponseNewRoomCloudMessaging);
        if (dataResponseNewRoomCloudMessaging == null) return;
        final String title = String.format(context.getString(R.string.title_notofication_new_room), dataResponseNewRoomCloudMessaging.room.title);
        final String text = String.format(context.getString(R.string.text_notofication_new_room), dataResponseNewRoomCloudMessaging.user.name);
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = null;
                try {
                    if (dataResponseNewRoomCloudMessaging.user != null) {
                        DownloadUsers.Download(dataResponseNewRoomCloudMessaging.user, url_base);
                        DownloadRooms.Download(dataResponseNewRoomCloudMessaging.room);
                        DownloadRoomParticipants.Download(dataResponseNewRoomCloudMessaging.roomParticipants);

//                        Long author_unic = Long.valueOf(dataResponseNewMessageCloudMessaging.user.unic);
//                        ItemLog itemLog = App.Database.daoLog().getLog(author_unic, Consts.LOG_ACTION_RECEIVED_MESSAGE);
//                        if(itemLog == null) {
//                            itemLog = new ItemLog();
//                            itemLog.item_unic = author_unic;
//                            itemLog.action = Consts.LOG_ACTION_RECEIVED_MESSAGE;
//                            App.Database.daoLog().insert(itemLog);
//                        }


                        if (!dataResponseNewRoomCloudMessaging.user.avatar.equals("")) {
                            bitmap = FileUtils.GetBitmap(dataResponseNewRoomCloudMessaging.user.avatar);
                        }
                        long room_unic = Long.parseLong(dataResponseNewRoomCloudMessaging.room.unic);
                        NotificationItem notificationItem = App.Database.daoNotification().getByStatusItemUnicType(room_unic, Consts.NOTIFICATION_TYPE_ROOM);
                        if(notificationItem == null) {
                            notification_id = App.Database.daoNotification().getAll().size() + 1;
                            notificationItem = new NotificationItem();
                            notificationItem.id = notification_id;
                            notificationItem.title = title;
                            notificationItem.content = text;
                            notificationItem.status = 0;
                            notificationItem.item_unic = room_unic;
                            notificationItem.type = Consts.NOTIFICATION_TYPE_ROOM;
                            notificationItem.channelId = NotificationUtils.CHANNEL_ID_BASE;
                            App.Database.daoNotification().insert(notificationItem);
                        } else {
                            notification_id = notificationItem.id;
                            notificationItem.title = title+", " + dataResponseNewRoomCloudMessaging.room.title;
                            App.Database.daoNotification().update(notificationItem);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                sendNotification(title, text, notification_id, bitmap, getRoomIntent(dataResponseNewRoomCloudMessaging.room.unic, notification_id, Consts.NEW_ROOM));
            }
        }.execute();
    }
}
