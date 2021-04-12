package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.smodel.SInterest;

import java.util.List;

public class DownloadInterest implements Download {

    private List<SInterest> interests;

    public DownloadInterest(List<SInterest> interests) {
        this.interests = interests;
    }

    @Override
    public void Execute(Context context, String url_base) {
        if(interests.size() > 0) {
            for (SInterest sInterest : interests) {
                Interest interest = App.Database.daoInterest().getById(Integer.parseInt(sInterest.id));
                if(interest == null) {
                    interest = new Interest();
                    interest.id = Integer.parseInt(sInterest.id);
                    interest.title = sInterest.title;
                    App.Database.daoInterest().insert(interest);
                    System.out.println("insert interest");
                    System.out.println(interest);
                    System.out.println("insert interest");
                } else {
                    interest.title = sInterest.title;
                    App.Database.daoInterest().Update(interest);
                }
            }
        }
    }
}
