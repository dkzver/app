package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.smodel.SComment;

import java.util.List;

public class DownloadComments implements Download {
    private List<SComment> comments;
    private String url_base;

    public DownloadComments(List<SComment> comments, String url_base) {
        this.comments = comments;
        this.url_base = url_base;
    }

    public static void Download(SComment sComment, String url_base) {
        if(!sComment.user_avatar.contains("http")) sComment.user_avatar = url_base + sComment.user_avatar;
        Comment comment = App.Database.daoComment().get(Long.parseLong(sComment.unic));
        if (comment == null) {
            comment = new Comment(sComment);
            App.Database.daoComment().insert(comment);
        } else {
            comment.set(sComment);
            App.Database.daoComment().update(comment);
        }
    }
    public static void Download(List<SComment> comments, String url_base) {
        for(SComment sComment : comments) {
            Download(sComment, url_base);
        }
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(comments, url_base);
    }
}
