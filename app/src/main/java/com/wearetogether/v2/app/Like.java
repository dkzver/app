package com.wearetogether.v2.app;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.Visit;
import com.wearetogether.v2.database.model.Vote;
import com.wearetogether.v2.ui.holders.group.HolderPlaceGroup;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.utils.ToastUtils;

import java.util.Calendar;
import java.util.List;

public class Like {
    public static Integer result;

    public static void Start(final HolderPlaceGroup holder, final Long place_unic, final int type) {
        if(App.SUser == null) return;
        if(place_unic == null) return;
        final long user_unic = Long.parseLong(App.SUser.unic);
        System.out.println("Like " + place_unic);
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Vote> voteList = App.Database.daoVote().getAll(place_unic, user_unic);
                if(voteList.size() > 1) {
                    for(int x = 1; x < voteList.size(); x++) {
                        App.Database.daoVote().delete(voteList.get(x));
                    }
                }
                Vote log_vote = App.Database.daoVote().get(place_unic, user_unic);
                System.out.println("log_vote " + log_vote);
                int user_id = App.SUser.getUserId();
                if (log_vote == null) {
                    result = 1;
                    log_vote = new Vote();
                    log_vote.unic = Calendar.getInstance().getTimeInMillis();
                    log_vote.vote = result;
                    log_vote.item_unic = place_unic;
                    log_vote.user_unic = user_unic;
                    App.Database.daoVote().insert(log_vote);
                } else {
                    if(log_vote.vote == 1) {
                        result = 0;
                    } else {
                        result = 1;
                    }
                    log_vote.vote = result;
                    log_vote.item_unic = place_unic;
                    log_vote.user_unic = user_unic;
                    App.Database.daoVote().update(log_vote);
                }
                System.out.println("result " + result);
                ItemLog log = App.Database.daoLog().getLog(place_unic, Consts.LOG_ACTION_VOTE);
                if (log == null) {
                    log = new ItemLog();
                    log.unic = Calendar.getInstance().getTimeInMillis();
                    log.value = result;
                    log.item_unic = place_unic;
                    log.user_id = user_id;
                    log.action = Consts.LOG_ACTION_VOTE;
                    log.type = type;
                    App.Database.daoLog().insert(log);
                } else {
                    log.value = result;
                    log.item_unic = place_unic;
                    log.user_id = user_id;
                    log.action = Consts.LOG_ACTION_VOTE;
                    App.Database.daoLog().update(log);
                }


                holder.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("result " + result);
                        PreferenceUtils.SaveLog(holder.activity.getApplicationContext(), true);
                        if (result == 0) {
                            holder.OnUnLike();
                        } else {
                            holder.OnLike();
                        }
                        ToastUtils.Short(holder.activity.getApplicationContext(), holder.activity.getString(R.string.you_vote));
                    }
                });
            }
        }).start();
    }
}
