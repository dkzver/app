package com.wearetogether.v2.utils;

import android.app.*;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.NotificationItem;
import com.wearetogether.v2.ui.activities.MainActivity;

public class NotificationUtils extends ContextWrapper {
    public static final String CHANNEL_ID_BASE = "com.wearetogether.v2.CHANNEL_ID_BASE";
    public static final String CHANNEL_2 = "com.wearetogether.v2.CHANNEL_2";
    private NotificationManager notificationManager;

    public NotificationUtils(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels(context);
        }
    }

    public static void Start(final MainActivity activity, final Intent intent) {
        String action = intent.getStringExtra(Consts.ACTION);
        String unic = intent.getStringExtra(Consts.UNIC);
        Integer notification_id = intent.getIntExtra(Consts.NOTIFICATION_ID, -1);
        if (notification_id == null) return;
        if (action == null || action.equals("")) return;
        if (unic == null || unic.equals("")) return;
        if (notification_id == -1) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationItem notificationItem = App.Database.daoNotification().getById(notification_id);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!notificationItem.action.equals(action)) {
                                throw new Exception("Error notification action");
                            }
                            if (action.equals(Consts.NOTIFICATION_ACTION_REQUEST_FRIEND) ||
                                    action.equals(Consts.NOTIFICATION_ACTION_ASSEPT_FRIEND)) {
                                App.GoToUser(activity, Long.parseLong(unic), MainActivity.class);
                            } else if (action.equals("FRIEND")) {
                                App.GoToUser(activity, Long.parseLong(unic), MainActivity.class);
                            } else if (action.equals(Consts.ACTION_SEND_MESSAGE)) {
                                App.GoToRoom(activity, Long.parseLong(unic), MainActivity.class);
                            } else if (action.equals(Consts.NEW_ROOM)) {
                                App.GoToRoom(activity, Long.parseLong(unic), MainActivity.class);
                            } else if (action.equals(Consts.NOTIFICATION_ACTION_VISITED)) {
                                App.GoToPlace(activity, Long.parseLong(unic), MainActivity.class);
                            } else if (action.equals(Consts.NOTIFICATION_ACTION_LIKED)) {
                                App.GoToPlace(activity, Long.parseLong(unic), MainActivity.class);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    notificationItem.status = NotificationItem.STATUS_READ;
                                    App.Database.daoNotification().update(notificationItem);
                                }
                            }).start();
                        } catch (Exception e) {
                            System.out.println("Error notifications " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels(Context context) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_BASE,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(defaultSoundUri, null);
        getManager().createNotificationChannel(channel);


        NotificationChannel channel2 = new NotificationChannel(CHANNEL_2,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW);
        channel.setSound(null, null);
        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public Notification.Builder getNotification(NotificationItem notification, Context context, boolean enableSoundNotification) {
        if (notification == null) return null;
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = CHANNEL_2;
            if(enableSoundNotification) {
                if(notification.channelId != null && !notification.channelId.equals("")) {
                    channelId = notification.channelId;
                } else {
                    channelId = CHANNEL_ID_BASE;
                }
                System.out.println("channelId " + channelId);
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        if (notification.bitmap != null) {
            builder.setLargeIcon(notification.bitmap);
        }
        builder.setSmallIcon(R.drawable.app_logo);
        if (notification.title != null && !notification.title.equals("")) {
            builder.setContentTitle(notification.id+" - "+notification.title);
        }
        if (notification.content != null && !notification.content.equals("")) {
            builder.setContentText(notification.content);
        }
        builder.setAutoCancel(true);
        if (notification.action != null && !notification.action.equals("")) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Consts.ACTION, notification.action);
            intent.putExtra(Consts.UNIC, String.valueOf(notification.item_unic));
            intent.putExtra(Consts.NOTIFICATION_ID, notification.id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(notification.id, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }
        return builder;
    }

    public void showNotification(NotificationItem notificationItem, Context context, boolean enableSoundNotification) {
        NotificationManager notificationManager = getManager();
        if (notificationManager != null) {
            Notification.Builder notification = getNotification(notificationItem, context, enableSoundNotification);
            notificationManager.notify(notificationItem.id, notification.build());
        }
    }
}
