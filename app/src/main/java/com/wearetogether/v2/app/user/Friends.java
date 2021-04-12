package com.wearetogether.v2.app.user;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.ui.activities.UserActivity;
import com.wearetogether.v2.ui.listeners.FriendListener;
import com.wearetogether.v2.utils.PreferenceUtils;
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
import java.util.Calendar;

public class Friends {

    public static class Data {

        private long user_unic;
        private long target_unic;
        private int type;
        private long log_unic;

        public Data() {

        }

        public Data(long user_unic, long target_unic, int type, long log_unic) {
            this.user_unic = user_unic;
            this.target_unic = target_unic;
            this.type = type;
            this.log_unic = log_unic;
        }
    }

    public static void Start(final UserActivity activity,
                             final long user_unic,
                             final int type) {
        if (App.SUser == null) return;
        final long uUnic = Long.parseLong(App.SUser.unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("user_unic: " + user_unic);
                System.out.println("type: " + type);
                Friend friend = App.Database.daoFriends().findFriend(uUnic, user_unic);
                Data data = new Data(uUnic, user_unic, type, Calendar.getInstance().getTimeInMillis());
                Gson gson = new Gson();
                String json_string = gson.toJson(data, Data.class);
                String responce = "";
                String url = "";
                if (friend != null && (type == Friend.ACCEPT_FRIEND || type == Friend.REJECT_FRIEND || type == Friend.REMOVE_FRIEND || type == Friend.CANCEL_FRIEND)) {
                    switch (type) {
                        case Friend.ACCEPT_FRIEND:
                            //Подверждение
                            url = activity.getString(R.string.url_send_accept_friend);
                            break;
                        case Friend.REJECT_FRIEND:
                            //Отклонение
                            url = activity.getString(R.string.url_send_reject_friend);
                            break;
                        case Friend.REMOVE_FRIEND:
                            //Удаление
                            url = activity.getString(R.string.url_send_delete_friend);
                            break;
                        case Friend.CANCEL_FRIEND:
                            //Отмена запроса
                            url = activity.getString(R.string.url_send_cancel_friend);
                            break;
                    }
                } else {
                    //апрос на дружбу
                    url = activity.getString(R.string.url_send_add_friend);
                }
                if(url.equals("")) return;
                if(json_string.equals("")) return;
                responce = sendDataFromServer(
                        json_string,
                        url);
                System.out.println("responce " + responce);
                if (responce.equals("Success")) {
                    if(type == Friend.ACCEPT_FRIEND && friend != null) {
                        friend.type = Friend.FRIEND;
                        App.Database.daoFriends().update(friend);
                    } else if ((type == Friend.REMOVE_FRIEND || type == Friend.REJECT_FRIEND) && friend != null) {
                        friend.type = Friend.SUBSCRIBES;
                        App.Database.daoFriends().update(friend);
                    } else if(type == Friend.SEND_FRIEND && friend == null) {
                        friend = new Friend();
                        friend.user_unic = uUnic;
                        friend.target_unic = user_unic;
                        friend.type = Friend.SEND_REQUEST_FRIEND;
                        App.Database.daoFriends().insert(friend);
                    } else if(friend != null) {
                        App.Database.daoFriends().delete(friend);
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                        activity.changeFriendState(type);
                    }
                });
            }
        }).start();
    }

    private static String sendDataFromServer(
            String json_string,
            String url) {
        System.out.println("json_string: " + json_string);
        System.out.println(json_string);
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
            entity.addPart("json_string", new StringBody(json_string, App.ContentTypeUTF8));
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
        System.out.println("responseString " + responseString);
        System.out.println(responseString);
        System.out.println("responseString " + responseString);
        return responseString;
    }
}
