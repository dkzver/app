package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private List<Download> downloadList = new ArrayList<>();
    private Context context;
    private String url_base;

    public DownloadManager(Context context, String url_base) {
        this.context = context;
        this.url_base = url_base;
    }

    public void add(Download download) {
        downloadList.add(download);
    }

    public void execute() {
        for(Download download : downloadList) {
            download.Execute(context, url_base);
        }
    }

    public static String download(String source_snapshot, Context context) throws Exception {
        File file = FileUtils.DownloadImageFromUrl(source_snapshot, context);
        if (file != null) {
            return  file.getAbsolutePath();
        }
        return "";
    }
}
