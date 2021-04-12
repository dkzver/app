package com.wearetogether.v2.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GCMSenderService extends IntentService {
    public static void StartServer(FragmentActivity activity, String data) {
        Intent intent = new Intent(activity.getApplicationContext(), GCMSenderService.class);
        intent.putExtra("data", data);
        activity.startService(intent);
    }

    //Empty constructor
    public GCMSenderService() {
        super("GCM_Sender");
    }

    //Processes gcm send messages
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("Action Service", "GCM_Sender Service Started");
        //Get message from intent
        try {
            String data = intent.getStringExtra("data");
            if (!data.equals("")) {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "key=" + getString(R.string.api_key));
                urlConnection.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(data);
                out.close();
                int responseCode = urlConnection.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("GCM_Sender Service finish");
                System.out.println("responseCode: " + responseCode);
                System.out.println("responseCode: " + responseCode);
                System.out.println("responseCode: " + responseCode);
                System.out.println("responseCode: " + responseCode);
                System.out.println("responseCode: " + responseCode);
                System.out.println("GCM_Sender Service finish");
            } else {
                Log.d("GCM_Sender:", "Field REGISTRATION_TABLE is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
