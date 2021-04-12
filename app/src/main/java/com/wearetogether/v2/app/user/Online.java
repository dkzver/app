package com.wearetogether.v2.app.user;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Online {

    public interface Listener {
        void OnSend();
    }
    public static void Start(final FragmentActivity activity, final int is_online, final Listener listener) {
        System.out.println("TaskOnline");
        System.out.println(is_online);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responce = sendDataFromServer(String.valueOf(App.SUser.unic), String.valueOf(App.SUser.social_id), String.valueOf(is_online), activity.getString(R.string.url_online));
                System.out.println("is online " + is_online + " " + responce);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnSend();
                    }
                });
            }
        }).start();

    }

    private static String sendDataFromServer(String unic,
                                             String social_id,
                                             String is_online, String url) {
        System.out.println("sendDataFromServer");
        System.out.println("unic " + unic);
        System.out.println("social_id " + social_id);
        System.out.println("is_online " + is_online);
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
            entity.addPart(Consts.UNIC, new StringBody(unic));
            entity.addPart(Consts.SOCIAL_ID, new StringBody(social_id));
            entity.addPart(Consts.IS_ONLINE, new StringBody(is_online));
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
