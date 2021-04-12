package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Vote;
import com.wearetogether.v2.smodel.SVote;

import java.util.Calendar;
import java.util.List;

public class DownloadVotes implements Download {
    private List<SVote> votes;
    private String url_base;

    public DownloadVotes(List<SVote> votes, String url_base) {
        this.votes = votes;
        this.url_base = url_base;
    }

    @Override
    public void Execute(Context context, String url_base) {
        for (SVote sVote : votes) {
            Vote vote = App.Database.daoVote().get(Long.parseLong(sVote.item_unic));
            if (vote == null) {
                vote = new Vote();
                vote.unic = Calendar.getInstance().getTimeInMillis();
                vote.item_unic = Long.parseLong(sVote.item_unic);
                vote.user_unic = Long.parseLong(sVote.user_unic);
                vote.vote = Integer.parseInt(sVote.vote);
                App.Database.daoVote().insert(vote);
            } else {
                vote.item_unic = Long.parseLong(sVote.item_unic);
                vote.user_unic = Long.parseLong(sVote.user_unic);
                vote.vote = Integer.parseInt(sVote.vote);
                App.Database.daoVote().update(vote);
            }
        }
    }
}
