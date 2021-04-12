package com.wearetogether.v2.app;

import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import org.json.JSONObject;

import java.net.URL;

public class FBHelper {
    private AccessToken accessToken;
    private HelperListener listener;

    public FBHelper(AccessToken accessToken, HelperListener listener) {

        this.accessToken = accessToken;
        this.listener = listener;

        String token = this.accessToken.getToken();
    }

    public void execute() {

        if (accessToken != null && !accessToken.isExpired()) {

            //Just set User ID
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    getDataFb(object, listener);
                }
            });

            //Request Graph API
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, email, location, birthday, friends");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void getDataFb(JSONObject object, HelperListener listener) {
        System.out.println(object);
        try {
            URL profile_picture
                    = new URL("https://graph.facebook.com/"
                    + object.getString("id")
                    + "/picture?width=250&height=250");
            System.out.println(profile_picture);
            listener.OnResult(object.getString("email"), object.getString("id"), profile_picture.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        LoginManager.getInstance().logOut();
    }
}
