package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.UserInterest;
import com.wearetogether.v2.smodel.SUserInterest;

import java.util.List;

public class DownloadUserInterest implements Download {

    private List<SUserInterest> user_interests;

    public DownloadUserInterest(List<SUserInterest> user_interests) {
        this.user_interests = user_interests;
    }

    @Override
    public void Execute(Context context, String url_base) {
        if(user_interests.size() > 0) {
            for (SUserInterest sUserInterest : user_interests) {
                UserInterest userInterest = App.Database.daoUserInterest().get(Long.parseLong(sUserInterest.user_unic), Integer.parseInt(sUserInterest.interest_id));
                if(userInterest == null) {
                    userInterest = new UserInterest();
                    userInterest.interest_id = Integer.parseInt(sUserInterest.interest_id);
                    userInterest.user_unic = Long.parseLong(sUserInterest.user_unic);
                    App.Database.daoUserInterest().insert(userInterest);
                } else {
                    userInterest.interest_id = Integer.parseInt(sUserInterest.interest_id);
                    userInterest.user_unic = Long.parseLong(sUserInterest.user_unic);
                    App.Database.daoUserInterest().Update(userInterest);
                }
            }
        }
    }
}
