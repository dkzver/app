package com.wearetogether.v2.database.model;

import android.graphics.Bitmap;
import android.location.Location;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.app.download.*;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "place_version")
public class PlaceVersion implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "unic")
    public long unic = 0;

    @ColumnInfo(name = "version")
    public int version = 0;

    @ColumnInfo(name = "version_vote")
    public int version_vote;

    @ColumnInfo(name = "version_visit")
    public int version_visit;

    @ColumnInfo(name = "version_comment")
    public int version_comment;

    @ColumnInfo(name = "version_author")
    public int version_author;

    @ColumnInfo(name = "version_images")
    public int version_images;

    public static Place Execute(long unic, PlaceActivity activity, String url_base) {
        HashMap<String, String> dataToServer = new HashMap<>();
        dataToServer.put("place_unic", String.valueOf(unic));
        PlaceVersion placeVersion = App.Database.daoVersion().getPlace(unic);
//        Place place = App.Database.daoPlace().get(unic);
//        if(place != null) {
//            UserVersion userVersion = App.Database.daoVersion().getUser(place.user_unic);
//            if(userVersion != null) {
//                dataToServer.put("version_author", String.valueOf(userVersion.version));
//            }
//        }
        if (placeVersion != null) {
            dataToServer.put("version", String.valueOf(placeVersion.version));
            dataToServer.put("version_vote", String.valueOf(placeVersion.version_vote));
            dataToServer.put("version_visit", String.valueOf(placeVersion.version_visit));
            dataToServer.put("version_comment", String.valueOf(placeVersion.version_comment));
            dataToServer.put("version_author", String.valueOf(placeVersion.version_author));
            dataToServer.put("version_images", String.valueOf(placeVersion.version_images));
        }
        System.out.println("dataToServer");
        System.out.println(dataToServer);
        System.out.println(dataToServer.keySet());
        System.out.println(dataToServer.values());
        System.out.println("dataToServer");
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
            for(String key : dataToServer.keySet()) {
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
        if(data == null) {
            return null;
        }
        System.out.println("data");
        System.out.println(data.toString());
        if(data.error != 0) {
            return null;
        }
        if(data.place_version != null) {
            boolean isUpdate = false;
            if(placeVersion != null) {
                if (data.place_version.version > placeVersion.version) {
                    placeVersion.version = data.place_version.version;
                    isUpdate = true;
                } else if (data.place_version.version_vote > placeVersion.version_vote) {
                    placeVersion.version_vote = data.place_version.version_vote;
                    isUpdate = true;
                } else if (data.place_version.version_visit > placeVersion.version_visit) {
                    placeVersion.version_visit = data.place_version.version_visit;
                    isUpdate = true;
                } else if (data.place_version.version_comment > placeVersion.version_comment) {
                    placeVersion.version_comment = data.place_version.version_comment;
                    isUpdate = true;
                } else if (data.place_version.version_author > placeVersion.version_author) {
                    placeVersion.version_author = data.place_version.version_author;
                    isUpdate = true;
                } else if (data.place_version.version_images > placeVersion.version_images) {
                    placeVersion.version_images = data.place_version.version_images;
                    isUpdate = true;
                }
            } else {
                data.place_version.id = App.Database.daoVersion().getPlaces().size() + 1;
                App.Database.daoVersion().insert(data.place_version);
            }
            if(isUpdate) {
                App.Database.daoVersion().update(placeVersion);
            }
        }
        if(data.place != null) {
            DownloadPlaces.Download(data.place, url_base);
        }
        if(data.author != null) {
            DownloadUsers.Download(data.author, url_base);
        }
        if(data.votes != null && data.votes.size() > 0) {
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
        Place place = App.Database.daoPlace().get(unic);
        System.out.println("place in db");
        System.out.println(place);
//                if(place != null) {
//                    if(data.user_version != null) {
//                        if (data.user_version.version > placeVersion.version_author) {
//                            UserVersion userVersion = App.Database.daoVersion().getUser(place.user_unic);
//                            if(userVersion != null) {
//                                userVersion.version = data.user_version.version;
//                                userVersion.version_interests = data.user_version.version_interests;
//                                userVersion.version_friends = data.user_version.version_friends;
//                                userVersion.unic = data.user_version.unic;
//                                App.Database.daoVersion().update(userVersion);
//                            } else {
//                                App.Database.daoVersion().insert(data.user_version);
//                            }
//                        }
//                    }
//                }
        System.out.println(data.place_version);
        return place;
    }

    static class Data {
        public int error = 0;
        public PlaceVersion place_version;
        public UserVersion user_version;
        public SPlace place;
        public SUser author;
        public List<SComment> comments = new ArrayList<>();
        public List<SVisit> visits = new ArrayList<>();
        public List<SVote> votes = new ArrayList<>();
        public List<SMediaItem> images = new ArrayList<>();
    }
}
