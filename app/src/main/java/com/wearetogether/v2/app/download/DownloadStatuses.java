package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Status;
import com.wearetogether.v2.smodel.SStatus;

import java.util.List;

public class DownloadStatuses implements Download {
    private List<SStatus> statuses;

    public DownloadStatuses(List<SStatus> statuses) {
        this.statuses = statuses;
    }

    @Override
    public void Execute(Context context, String url_base) {
        if (statuses.size() > 0) {
            System.out.println("statuses " + statuses);
            for (SStatus sStatus : statuses) {
                System.out.println("status " + sStatus);
                Status status = App.Database.daoStatus().getById(Integer.parseInt(sStatus.id));
                if (status == null) {
                    status = new Status();
                    status.id = Integer.parseInt(sStatus.id);
                    status.title = sStatus.title;
                    App.Database.daoStatus().insert(status);
                } else {
                    status.title = sStatus.title;
                    App.Database.daoStatus().update(status);
                }
            }
        }
    }
}
