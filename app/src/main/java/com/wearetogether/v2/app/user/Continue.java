package com.wearetogether.v2.app.user;

import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.*;
import com.wearetogether.v2.smodel.SCategory;
import com.wearetogether.v2.smodel.SInterest;
import com.wearetogether.v2.smodel.SStatus;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Continue {

    private static String[] categories;

    private static String sendDataFromServer(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseString = "";
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num, int off, int len) {

                        }
                    });
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;
    }

    public static void Start(final MainActivity activity) {

        boolean isDownloadData = PreferenceUtils.IsDownload(activity.getApplicationContext());
        System.out.println("isDownloadData: "+isDownloadData);
        if(isDownloadData) {
            callContinue(activity);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String responseString = sendDataFromServer(activity.getString(R.string.url_continue));
                    if (responseString != null) {
                        Gson gson = new Gson();
                        Data data = gson.fromJson(responseString, Data.class);
                        if (data != null) {
                            System.out.println("categories");
                            System.out.println(data.categories);
                            System.out.println("statuses");
                            System.out.println(data.statuses);
                            System.out.println("interests");
                            System.out.println(data.interests);
                            DownloadManager downloadManager = new DownloadManager(activity.getApplicationContext(), activity.getString(R.string.url_base));
                            downloadManager.add(new DownloadStatuses(data.statuses));
                            downloadManager.add(new DownloadCategories(data.categories));
                            downloadManager.add(new DownloadInterest(data.interests));
                            downloadManager.execute();
                        }
                        App.Categories = ListUtils.GetCategories(App.Database.daoCategory().getAll(), activity.getApplicationContext());
                        App.Interests = ListUtils.GetInterests(App.Database.daoInterest().getAll(), activity.getApplicationContext());
                        App.Statuses = ListUtils.GetStatuses(App.Database.daoStatus().getAll(), activity.getApplicationContext());
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PreferenceUtils.SetDownload(activity.getApplicationContext(), true);
                            callContinue(activity);
                        }
                    });
                }
            }).start();
        }
    }

    private static void callContinue(MainActivity activity) {
        PreferenceUtils.SetContinue(activity.getApplicationContext(), true);
        activity.hideLoginView();
    }

    static class Data {
        List<SCategory> categories = new ArrayList<>();
        List<SStatus> statuses = new ArrayList<>();
        List<SInterest> interests = new ArrayList<>();
    }
}
