package com.wearetogether.v2.datacloudmessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;

import java.util.Map;

public abstract class CloudMessage {

    protected final String url_base;
    protected final Context context;

    public CloudMessage(Context context) {
        this.url_base = context.getString(R.string.url_base);
        this.context = context;
    }

    public abstract void execute(Map<String, String> data);

    protected Intent getRoomIntent(String room_unic, Integer notification_id, String action) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Consts.ACTION, action);
        intent.putExtra(Consts.UNIC, room_unic);
        intent.putExtra(Consts.NOTIFICATION_ID, notification_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    protected Intent getUserIntent(String user_unic) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("action", "FRIEND");
        intent.putExtra(Consts.UNIC, user_unic);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    protected void sendNotification(String title, String text, Integer id, Bitmap bitmap, Intent intent) {
        try {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

            String channelId = context.getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context, channelId);
            if (bitmap != null) {
                notificationBuilder.setLargeIcon(bitmap);
            }
            notificationBuilder.setSmallIcon(R.drawable.app_logo);
            if (title != null) {
                notificationBuilder.setContentTitle(title);
            }
            if (text != null) {
                notificationBuilder.setContentText(text);
            }
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            if (pendingIntent != null) {
                notificationBuilder.setContentIntent(pendingIntent);
            }

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }
                notificationManager.notify(id, notificationBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
