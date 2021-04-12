package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Visit;
import com.wearetogether.v2.smodel.SVisit;

import java.util.Calendar;
import java.util.List;

public class DownloadVisits implements Download {
    private final List<SVisit> visits;

    public DownloadVisits(List<SVisit> visits) {

        this.visits = visits;
    }

    public static void Download(List<SVisit> visits) {
        for (SVisit sVisit : visits) {
            Visit visit = App.Database.daoVisit().get(Long.parseLong(sVisit.item_unic), Long.parseLong(sVisit.user_unic));
            if (visit == null) {
                visit = new Visit();
                visit.unic = Calendar.getInstance().getTimeInMillis();
                visit.user_unic = Long.parseLong(sVisit.user_unic);
                visit.place_unic = Long.parseLong(sVisit.item_unic);
                visit.visit = Integer.parseInt(sVisit.visit);
                App.Database.daoVisit().insert(visit);
            } else {
                visit.user_unic = Long.parseLong(sVisit.user_unic);
                visit.place_unic = Long.parseLong(sVisit.item_unic);
                visit.visit = Integer.parseInt(sVisit.visit);
                App.Database.daoVisit().update(visit);
            }
        }
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(visits);
    }
}
