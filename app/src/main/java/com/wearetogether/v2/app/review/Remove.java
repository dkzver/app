package com.wearetogether.v2.app.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.ui.activities.ReviewsActivity;
import com.wearetogether.v2.utils.ToastUtils;

import java.util.Calendar;

public class Remove {
    public static void Start(ReviewsActivity activity, Context context, long unic, String key, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(activity.getString(R.string.remove_review));
        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ItemLog log = App.Database.daoLog().getLog(unic, Consts.LOG_ACTION_INSERT_COMMENT);
                        if(log != null) {
                            App.Database.daoLog().delete(log);
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(log != null) {
                                    activity.adapterGroup.removeComment(unic, Long.parseLong(key), position);
                                } else {
                                    ToastUtils.Short(context.getApplicationContext(), activity.getString(R.string.error_remove_review));
                                }
                            }
                        });
                    }
                }).start();
                dialog.dismiss();
            }
        });
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
