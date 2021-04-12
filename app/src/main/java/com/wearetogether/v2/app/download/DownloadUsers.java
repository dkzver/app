package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.smodel.SUser;

import java.util.List;

public class DownloadUsers implements Download {
    private List<SUser> userList;
    private String url_base;

    public DownloadUsers(List<SUser> userList, String url_base) {
        this.userList = userList;
        this.url_base = url_base;
    }

    public static void Download(List<SUser> userList, String url_base) {
        for (int x = 0; x < userList.size(); x++) {
            Download(userList.get(x), url_base);
        }
    }

    public static void Download(SUser sUser, String url_base) {
        if(!sUser.avatar.contains("http")) sUser.avatar = url_base + sUser.avatar;
        Download(sUser.getUser(), url_base);
    }

    public static User Download(User user, String url_base) {
        if(!user.avatar.contains("http")) user.avatar = url_base + user.avatar;
        try {
            User old_user = App.Database.daoUser().get(user.unic);
            if(old_user == null) {
                App.Database.daoUser().insert(user);
            } else {
                if(old_user.version < user.version) {
                }
                App.Database.daoUser().update(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(userList, url_base);
    }
}
