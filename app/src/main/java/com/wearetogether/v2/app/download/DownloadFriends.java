package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.smodel.SFriend;

import java.util.List;

public class DownloadFriends implements Download {
    private final List<SFriend> friends;
    private String url_base;

    public DownloadFriends(List<SFriend> friends, String url_base) {

        this.friends = friends;
        this.url_base = url_base;
    }

    @Override
    public void Execute(Context context, String url_base) {
        for(SFriend sFriend : friends) {
            System.out.println("download friend");
            System.out.println(sFriend);
            Friend friend = new Friend(sFriend);
            Friend old_friend = App.Database.daoFriends().getByUser(friend.target_unic);
            System.out.println("old friend");
            System.out.println(old_friend);
            System.out.println("old friend");
            if(old_friend == null) {
                App.Database.daoFriends().insert(friend);
            } else {
                old_friend.type = friend.type;
                App.Database.daoFriends().update(old_friend);
            }
        }
    }
}
