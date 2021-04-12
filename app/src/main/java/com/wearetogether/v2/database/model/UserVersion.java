package com.wearetogether.v2.database.model;

import androidx.fragment.app.FragmentActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.*;
import com.wearetogether.v2.smodel.*;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "user_version")
public class UserVersion implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "unic")
    public long unic = 0;

    @ColumnInfo(name = "version")
    public int version = 0;

    @ColumnInfo(name = "version_interests")
    public int version_interests = 0;

    @ColumnInfo(name = "version_friends")
    public int version_friends = 0;

    @ColumnInfo(name = "version_vote")
    public int version_vote;

    @ColumnInfo(name = "version_visit")
    public int version_visit;

    @ColumnInfo(name = "version_comment")
    public int version_comment;

    @ColumnInfo(name = "version_places")
    public int version_places;

    @ColumnInfo(name = "version_images")
    public int version_images;

    public static User Execute(long unic, FragmentActivity activity, String url_base) {
        HashMap<String, String> dataToServer = new HashMap<>();
        dataToServer.put("user_unic", String.valueOf(unic));
        UserVersion userVersion = App.Database.daoVersion().getUser(unic);
        if (userVersion != null) {
            dataToServer.put("version", String.valueOf(userVersion.version));
            dataToServer.put("version_friends", String.valueOf(userVersion.version_friends));
            dataToServer.put("version_vote", String.valueOf(userVersion.version_vote));
            dataToServer.put("version_visit", String.valueOf(userVersion.version_visit));
            dataToServer.put("version_interests", String.valueOf(userVersion.version_interests));
            dataToServer.put("version_images", String.valueOf(userVersion.version_images));
            dataToServer.put("version_places", String.valueOf(userVersion.version_places));
            dataToServer.put("version_comment", String.valueOf(userVersion.version_comment));
        }
        System.out.println("dataToServer");
        System.out.println(dataToServer);
        System.out.println(dataToServer.keySet());
        System.out.println(dataToServer.values());
        System.out.println("dataToServer");
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
            for (String key : dataToServer.keySet()) {
                System.out.println("key:  " + key + " value: " + dataToServer.get(key));
                entity.addPart(key, new StringBody(dataToServer.get(key), App.ContentTypeUTF8));
            }
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
        Gson gson = new Gson();
        Data data = null;
        try {
            data = gson.fromJson(responseString, Data.class);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        if (data == null) {
            return null;
        }
        System.out.println("data");
        System.out.println(data.toString());
        if (data.error != 0) {
            return null;
        }
        if (data.user_version != null) {
            boolean isUpdate = false;
            if (userVersion != null) {
                if (data.user_version.version > userVersion.version) {
                    userVersion.version = data.user_version.version;
                    isUpdate = true;
                } else if (data.user_version.version_friends > userVersion.version_friends) {
                    userVersion.version_friends = data.user_version.version_friends;
                    isUpdate = true;
                } else if (data.user_version.version_interests > userVersion.version_interests) {
                    userVersion.version_interests = data.user_version.version_interests;
                    isUpdate = true;
                }  else if (data.user_version.version_vote > userVersion.version_vote) {
                    userVersion.version_vote = data.user_version.version_vote;
                    isUpdate = true;
                }  else if (data.user_version.version_visit > userVersion.version_visit) {
                    userVersion.version_visit = data.user_version.version_visit;
                    isUpdate = true;
                } else if (data.user_version.version_images > userVersion.version_images) {
                    userVersion.version_images = data.user_version.version_images;
                    isUpdate = true;
                } else if (data.user_version.version_comment > userVersion.version_comment) {
                    userVersion.version_comment = data.user_version.version_comment;
                    isUpdate = true;
                } else if (data.user_version.version_places > userVersion.version_places) {
                    userVersion.version_places = data.user_version.version_places;
                    isUpdate = true;
                }
            } else {
                data.user_version.id = App.Database.daoVersion().getUsers().size() + 1;
                App.Database.daoVersion().insert(data.user_version);
            }
            if (isUpdate) {
                App.Database.daoVersion().update(userVersion);
            }
        }
        if (data.user != null) {
            DownloadUsers.Download(data.user, url_base);
        }
        if (data.votes != null && data.votes.size() > 0) {
            DownloadVotes downloadVotes = new DownloadVotes(data.votes, url_base);
            downloadVotes.Execute(activity.getApplicationContext(), url_base);
        }
        if (data.visits != null && data.visits.size() > 0) {
            DownloadVisits downloadVisits = new DownloadVisits(data.visits);
            downloadVisits.Execute(activity.getApplicationContext(), url_base);
        }
        if (data.comments != null && data.comments.size() > 0) {
            DownloadComments downloadComments = new DownloadComments(data.comments, url_base);
            downloadComments.Execute(activity.getApplicationContext(), url_base);
        }
        if (data.images != null && data.images.size() > 0) {
            DownloadImages downloadImages = new DownloadImages(data.images, url_base);
            downloadImages.Execute(activity.getApplicationContext(), url_base);
        }
        if (data.places != null) {
            DownloadPlaces downloadPlaces = new DownloadPlaces(data.places, url_base);
            downloadPlaces.Execute(activity.getApplicationContext(), url_base);
        }
        if (data.friends != null) {
            DownloadFriends downloadFriends = new DownloadFriends(data.friends, url_base);
            downloadFriends.Execute(activity.getApplicationContext(), url_base);
        }
        User user = App.Database.daoUser().get(unic);
        System.out.println("user in db");
        System.out.println(user);
        return user;
    }

    static class Data {
        public int error = 0;
        public UserVersion user_version;
        public SUser user;
        public List<SPlace> places;
        public List<SComment> comments = new ArrayList<>();
        public List<SVisit> visits = new ArrayList<>();
        public List<SVote> votes = new ArrayList<>();
        public List<SFriend> friends = new ArrayList<>();
        public List<SMediaItem> images = new ArrayList<>();
    }
}
