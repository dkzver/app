package com.wearetogether.v2.app;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.vk.api.sdk.auth.VKAccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class VKHelper extends AsyncTask<Void, Void, String> {

    private final String url;
    private final String version = "5.122";
    private HelperListener listener;
    private VKAccessToken vkAccessToken;

    public VKHelper(VKAccessToken vkAccessToken, HelperListener listener) {
        this.vkAccessToken = vkAccessToken;
        url = "https://api.vk.com/method/users.get?owner_id="+vkAccessToken.getUserId()+"&fields=photo_200,sex,bdate,city,country&access_token=" + vkAccessToken.getAccessToken()+"&v="+version;
        System.out.println("url " + url);
        System.out.println(url);
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String responseString = "";
        try {
            HttpResponse response = httpclient.execute(httpGet);
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
        if (responseString == null) return null;
        if (responseString.equals("")) return null;
        System.out.println("responce " + responseString);
        System.out.println(responseString);
        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println("responseString");
        System.out.println(s);
        System.out.println("responseString");
        try {
            Gson gson = new Gson();
            DataResponce dataResponce = gson.fromJson(s, DataResponce.class);
            if(dataResponce != null) {
                System.out.println(dataResponce.response);
                VKUser user = dataResponce.response.get(0);
                System.out.println(user.first_name);
                System.out.println(user.last_name);
                System.out.println(user.photo_200);
                listener.OnResult(vkAccessToken.getEmail(), user.id, user.first_name, user.last_name, user.sex, user.getDateBirth(), user.photo_200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DataResponce {
        List<VKUser> response;
    }

    class VKUser {
        String id;
        String first_name;
        String last_name;
        String photo_200;
        int sex;
        String bdate;
//        VKItem city;
//        VKItem country;

        public String getDateBirth() {
            String date_birth = bdate;
            String[] temp = bdate.split("[.]");
            if(temp.length == 3) {
                String day = temp[0].length() == 2 ? temp[0] : "0"+temp[0];
                String month = temp[1].length() == 2 ? temp[1] : "0"+temp[1];
                return temp[2]+"-"+month+"-"+day;
            } else {
                return date_birth;
            }
        }
    }

    class VKItem {
        int id;
        String title;
    }
}


