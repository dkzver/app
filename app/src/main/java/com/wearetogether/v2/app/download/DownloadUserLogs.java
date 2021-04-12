package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.database.model.UserLog;
import com.wearetogether.v2.smodel.SFriendRequestLog;

import java.util.List;

public class DownloadUserLogs implements Download {
    private final List<SFriendRequestLog> userLogs;

    public DownloadUserLogs(List<SFriendRequestLog> userLogs) {

        this.userLogs = userLogs;
    }

    @Override
    public void Execute(Context context, String url_base) {
        for(SFriendRequestLog sUserLog : userLogs) {
//            UserLog userLog = new UserLog(sUserLog);
//            if(App.Database.daoUserLog().get(userLog.value, userLog.action) == null) {
//                boolean is = false;
//                if(userLog.action == Friend.REQUEST_FRIEND) {
//                    Friend friend = new Friend(sUserLog);
//                    if(App.Database.daoFriends().getByUser(friend.target_unic) == null) {
//                        App.Database.daoFriends().insert(friend);
//                        is = true;
//                    }
//                } else if(userLog.action == Message.NEW_MESSAGE) {
//                    is = true;
//                }
//                if(is) {
//                    App.Database.daoUserLog().insert(userLog);
//                }
//            }
        }
    }
}
