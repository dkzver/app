package com.wearetogether.v2.app.place;

import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.PlaceActivity;
import com.wearetogether.v2.ui.holders.group.HolderPlaceGroup;
import com.wearetogether.v2.utils.ToastUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CheckVisit {
    public static void Start(final HolderPlaceGroup holder, Long place_unic, int visit) {
        if(place_unic == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = sendDataFromServer(place_unic, holder.activity.getString(R.string.url_visit));
                holder.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("1")) {
                            holder.setVisit(visit);
                        } else {
                            if (result.equals("0")) {
                                PlaceActivity.Limit = true;
                                ToastUtils.Short(holder.activity.getApplicationContext(), holder.activity.getString(R.string.limit_count_participant));
                            } else {
                                ToastUtils.Short(holder.activity.getApplicationContext(), holder.activity.getString(R.string.error_count_participant));
                            }
                            holder.OnVisitUser(false);
                        }
                    }
                });
            }
        }).start();
    }

    private static String sendDataFromServer(long unic, String url) {
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
            entity.addPart(Consts.UNIC, new StringBody(String.valueOf(unic)));
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
