package com.wearetogether.v2.app.user;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.NotificationUtils;
import com.wearetogether.v2.utils.PreferenceUtils;

public class Logout {

    public static void Start(final FragmentActivity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.Database.ClearTable();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NotificationManager notificationManager = App.NotificationUtils.getManager();
                        if(notificationManager != null) {
                            notificationManager.cancelAll();
                        }
                        PreferenceUtils.Clear(activity.getApplicationContext());
                        FileUtils.RemoveImages(activity.getApplicationContext());
//                        final App app = (App) activity.getApplication();
//                        app.sendDataUserOnline(activity, 0, new Online.Listener() {
//                            @Override
//                            public void OnSend() {
//                            }
//                        });
                        App.SUser = null;
//                        app.isOnline = false;
                        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                });
            }
        }).start();
    }

    public static void showDialog(final FragmentActivity activity, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.
                setTitle(activity.getString(R.string.logout))
                .setMessage(activity.getString(R.string.already_logout))
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(App.SUser != null) {
                                    Logout.Start(activity);
                                } else {
                                    activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                                }
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
