package com.wearetogether.v2.app.data;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.*;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.PlaceVersion;
import com.wearetogether.v2.smodel.*;
import com.wearetogether.v2.ui.activities.PlaceActivity;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataItem {
    public List<SUser> users = new ArrayList<>();
    public List<SPlace> places = new ArrayList<>();
    public List<SMediaItem> images = new ArrayList<>();
    public List<SVisit> visits = new ArrayList<>();
    public List<SVote> votes = new ArrayList<>();
    public List<SComment> comments = new ArrayList<>();
    private List<SUserInterest> user_interests = new ArrayList<>();

    public static void Download(String responseString, FragmentActivity activity) {
        Gson gson = new Gson();
        DataItem data = gson.fromJson(responseString, DataItem.class);
        if(data != null) {
            String url_base = activity.getString(R.string.url_base);
            if (data.users != null) {
                DownloadUsers downloadUsers = new DownloadUsers(data.users, url_base);
                downloadUsers.Execute(activity.getApplicationContext(), url_base);
            }
            if (data.places != null) {
                DownloadPlaces downloadPlaces = new DownloadPlaces(data.places, url_base);
                downloadPlaces.Execute(activity.getApplicationContext(), url_base);
            }
            if (data.images != null) {
                DownloadImages downloadImages = new DownloadImages(data.images, url_base);
                downloadImages.Execute(activity.getApplicationContext(), url_base);
            }
            if (data.votes != null) {
                DownloadVotes downloadVotes = new DownloadVotes(data.votes, url_base);
                downloadVotes.Execute(activity.getApplicationContext(), url_base);
            }
            if (data.visits != null) {
                DownloadVisits downloadVisits = new DownloadVisits(data.visits);
                downloadVisits.Execute(activity.getApplicationContext(), url_base);
            }
            if (data.comments != null) {
                DownloadComments downloadComments = new DownloadComments(data.comments, url_base);
                downloadComments.Execute(activity.getApplicationContext(), url_base);
            }
            if (data.user_interests != null) {
                DownloadUserInterest downloadUserInterest = new DownloadUserInterest(data.user_interests);
                downloadUserInterest.Execute(activity.getApplicationContext(), url_base);
            }
        }
    }

    public static void User(String user_unic, FragmentActivity activity) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(activity.getString(R.string.url_get_user));
        String responseString = "";
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num, int off, int len) {
                        }
                    });
            entity.addPart("user_unic", new StringBody(user_unic));
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
        DataItem.Download(responseString, activity);
    }

    public static void Place(String place_unic, PlaceActivity activity) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(activity.getString(R.string.url_get_place));
        String responseString = "";
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num, int off, int len) {
                        }
                    });
            entity.addPart("place_unic", new StringBody(place_unic));
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
        System.out.println("responseString");
        System.out.println(responseString);
        System.out.println("responseString");
//        DataItem.Download(responseString, activity);
    }
}
