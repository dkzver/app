package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.smodel.SComment;
import com.wearetogether.v2.smodel.SMessage;

import java.util.List;

public class DownloadMessages implements Download {
    private List<SMessage> messages;
    private String url_base;

    public DownloadMessages(List<SMessage> messages, String url_base) {
        this.messages = messages;
        this.url_base = url_base;
    }

    public static void Download(SMessage sMessage, String url_base) {
        Message message = App.Database.daoMessages().get(Long.parseLong(sMessage.unic), Long.parseLong(sMessage.room_unic));
        if (message == null) {
            message = new Message(sMessage);
            App.Database.daoMessages().insert(message);
        } else {
//            message.set(sMessage);
//            App.Database.daoMessages().update(message);
        }
    }
    public static void Download(List<SMessage> comments, String url_base) {
        for(SMessage sMessage : comments) {
            Download(sMessage, url_base);
        }
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(messages, url_base);
    }
}
