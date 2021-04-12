package com.wearetogether.v2.app.review;

import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.ui.activities.ReviewsActivity;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Save {
    public static void Start(ReviewsActivity activity, String review, long parent, String author_name, long author_unic) {
        if(App.SUser == null) return;
        Long item_unic = activity.getReviewsViewModel().itemUnicMutableLiveData.getValue();
        if(item_unic == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Comment comment = new Comment();
                comment.unic = Calendar.getInstance().getTimeInMillis();
                comment.parent = parent;
                comment.item_unic = item_unic;
                comment.user_name = App.SUser.name;
                comment.user_avatar = App.SUser.avatar;
                comment.text = App.CapitalizeString(review);
                comment.replay_user_name = author_name;
                comment.replay_user_unic = author_unic;
                Date date = new Date();
                String comment_date = "";
                String comment_datetime = "";
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    comment_date = simpleDateFormat.format(date);
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    comment_datetime = simpleDateFormat.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                comment.comment_date = comment_date;
                comment.comment_datetime = comment_datetime;
                Integer type = activity.getReviewsViewModel().typeMutableLiveData.getValue();
                if(type != null) {
                    comment.type = type;
                }


                comment.user_unic = Long.parseLong(App.SUser.unic);
                System.out.println("Review save " + comment.item_unic);
                System.out.println("Review save " + comment.user_unic);
                System.out.println("Review save " + comment);


                App.Database.daoComment().insert(comment);

                ItemLog log = new ItemLog();
                log.unic = Calendar.getInstance().getTimeInMillis();
                log.action = Consts.LOG_ACTION_INSERT_COMMENT;
                log.user_id = App.SUser.getUserId();
                log.item_unic = comment.unic;
                App.Database.daoLog().insert(log);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                        activity.add(parent, comment);
                    }
                });
            }
        }).start();
    }
}
