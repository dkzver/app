package com.wearetogether.v2.ui.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.app.download.DownloadPlaces;
import com.wearetogether.v2.app.download.DownloadUsers;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.ClickToOptions;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.listeners.VoiceListener;
import com.wearetogether.v2.utils.ObjectUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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

public class SearchViewComponent extends LinearLayout implements TextWatcher, VoiceListener {

    private EditText edit_text;
    public View button_back;
    public View button_search;
    private View button_voice;
    private View button_clear_text;
    private ProgressBar progress_bar;
    private View button_options;
    private View buttons_right;
    private MainActivity activity;
    public View view_container;

    public SearchViewComponent(Context context) {
        super(context);
        initComponent();
    }

    public SearchViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_view_search, this);

        view_container = findViewById(R.id.view_container);
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.bottom_sheet_serach.setState(BottomSheetBehavior.STATE_COLLAPSED);
                edit_text.setText("");
                button_back.setVisibility(GONE);
                button_search.setVisibility(VISIBLE);
            }
        });
        button_search = findViewById(R.id.button_search);
        edit_text = findViewById(R.id.edit_text);
        button_voice = findViewById(R.id.button_voice);
        button_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice.Voice(activity, Consts.REQUEST_VOICE_SEARCH);
            }
        });
        button_clear_text = findViewById(R.id.button_clear_text);
        button_clear_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_text.setText("");
            }
        });
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(GONE);
        button_options = findViewById(R.id.button_options);
        buttons_right = findViewById(R.id.buttons_right);
        button_back.setVisibility(GONE);
        button_search.setVisibility(VISIBLE);
        button_voice.setVisibility(VISIBLE);
        button_clear_text.setVisibility(GONE);
        button_options.setVisibility(VISIBLE);

        edit_text.addTextChangedListener(this);

    }

    public void setup(MainActivity activity) {
        this.activity = activity;
        button_options.setOnClickListener(new ClickToOptions(activity));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (activity == null) return;
        if (edit_text.getText() == null) return;
        progress_bar.setVisibility(GONE);
        String text = String.valueOf(edit_text.getText());
        int length = text.length();
        if (length != 0) {
//            toggleButtonBack(true);
            if(button_back.getVisibility() == View.GONE) {
                showView(button_back, Gravity.RIGHT);
            }
            if(button_search.getVisibility() == View.VISIBLE) {
                hideView(button_search, Gravity.RIGHT);
            }
            if(button_voice.getVisibility() == View.VISIBLE) {
                hideView(button_voice, Gravity.RIGHT);
            }
            if(button_clear_text.getVisibility() == View.GONE) {
                showView(button_clear_text, Gravity.RIGHT);
            }
            if(button_options.getVisibility() == View.VISIBLE) {
                hideView(button_options, Gravity.RIGHT);
            }
            onSearchTextChanged(text);
            view_container.setBackgroundResource(R.drawable.serach_background_active);
        } else {
//            toggleButtonBack(false);
            if(activity.bottom_sheet_serach.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                if(button_back.getVisibility() == View.VISIBLE) {
                    hideView(button_back, Gravity.RIGHT);
                }
                if(button_search.getVisibility() == View.GONE) {
                    showView(button_search, Gravity.RIGHT);
                }
            } else {
                if(button_back.getVisibility() == View.GONE) {
                    showView(button_back, Gravity.RIGHT);
                }
                if(button_search.getVisibility() == View.VISIBLE) {
                    hideView(button_search, Gravity.RIGHT);
                }
            }
            if(button_voice.getVisibility() == View.GONE) {
                showView(button_voice, Gravity.RIGHT);
            }
            if(button_clear_text.getVisibility() == View.VISIBLE) {
                hideView(button_clear_text, Gravity.RIGHT);
            }
            if(button_options.getVisibility() == View.GONE) {
                showView(button_options, Gravity.RIGHT);
            }
            view_container.setBackgroundResource(R.drawable.serach_background);
        }
    }

    private void onSearchTextChanged(final String text) {
        if (text.length() < 3) return;
        if (App.SUser == null) return;
        progress_bar.setVisibility(VISIBLE);
        final String url_base = activity.getString(R.string.url_base);
        long user_unic = Long.parseLong(App.SUser.unic);
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Place> placeList = App.Database.daoPlace().getSearch(text/*,
                        100,
                        App.SUser.latitude,
                        App.SUser.longitude*/);

                List<User> userList = App.Database.daoUser().getSearch(text/*,
                        100,
                        App.SUser.latitude,
                        App.SUser.longitude*/);
                HashMap<Long, Integer> mapPlaces = new HashMap<>();
                HashMap<Long, Integer> mapUsers = new HashMap<>();
                for (Place place : placeList) {
                    System.out.println("place search " + place);
                    System.out.println("place search " + place.title);
                    System.out.println("place search " + place.description);
                    mapPlaces.put(place.unic, place.version);
                }
                for (User user : userList) {
                    System.out.println("user search " + user);
                    System.out.println("user search " + user.name);
                    System.out.println("user search " + user.email);
                    mapUsers.put(user.unic, user.version);
                }
                DataJson dataJson = new DataJson();
                dataJson.text = text;
                dataJson.distance = 3;
                dataJson.latitude = App.SUser.latitude;
                dataJson.longitude = App.SUser.longitude;
                dataJson.mapPlaces = mapPlaces;
                dataJson.mapUsers = mapUsers;
                Gson gson = new Gson();
                final String responseString = sendDataFromServer(
                        gson.toJson(dataJson, DataJson.class),
                        activity.getString(R.string.url_search));
                Data data = gson.fromJson(responseString, Data.class);
                List<DataGroup> dataGroupsPlaces = new ArrayList<>();
                List<DataGroup> dataGroupsUsers = new ArrayList<>();
                if (data.places.size() > 0) {
                    for (Place place : data.places) {
                        dataGroupsPlaces.add(new DataGroup().Place(ObjectUtils.Build(DownloadPlaces.Download(place, url_base), user_unic), user_unic));
                    }
//                    placeList = App.Database.daoPlace().getSearch(text/*,
//                            100,
//                            App.SUser.latitude,
//                            App.SUser.longitude*/);
                }
                if (data.users.size() > 0) {
                    for (User user : data.users) {
                        dataGroupsUsers.add(new DataGroup().User(ObjectUtils.Build(DownloadUsers.Download(user, url_base), user_unic), user_unic));
                    }
//                    userList = App.Database.daoUser().getSearch(text/*,
//                            100,
//                            App.SUser.latitude,
//                            App.SUser.longitude*/);
                }



                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress_bar.setVisibility(GONE);
                        if (data.error != 0) {
                            ToastUtils.Short(activity.getApplicationContext(), data.getError());
                        }
                        List<DataGroup> dataGroups = new ArrayList<>();
                        dataGroups.add(new DataGroup(activity.getString(R.string.places_nearby), DataGroup.TYPE_HEADER));
                        if (dataGroupsPlaces.size() == 0) {
                            dataGroups.add(new DataGroup(activity.getString(R.string.not_places), DataGroup.TYPE_TEXT));
                        } else {
                            dataGroups.addAll(dataGroupsPlaces);
                        }
                        dataGroups.add(new DataGroup(activity.getString(R.string.users_nearby), DataGroup.TYPE_HEADER));
                        if (dataGroupsUsers.size() == 0) {
                            dataGroups.add(new DataGroup(activity.getString(R.string.not_users), DataGroup.TYPE_TEXT));
                        } else {
                            dataGroups.addAll(dataGroupsUsers);
                        }
                        activity.adapterGroupSerach.update(dataGroups);
                        activity.view_bottom_sheet_serach.setVisibility(VISIBLE);
                        activity.bottom_sheet_serach.setState(BottomSheetBehavior.STATE_EXPANDED);
                        System.out.println("response string login");
                        System.out.println(responseString);
                        System.out.println("response string login");
                        System.out.println(responseString);
                    }
                });
            }
        }).start();
    }

    @Override
    public void OnSetVoice(int code, String spokenText) {
        if (activity == null) return;
        if (edit_text == null) return;
        if (code != Consts.REQUEST_VOICE_SEARCH) return;
        edit_text.setText(spokenText);
    }

    class Data {
        public int error;
        public String text;
        public List<User> users;
        public List<Place> places;

        public String getError() {
            String message = "";
            switch (error) {
                case 1:
                    message = "Error text";
                    break;
            }
            return message;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class DataJson {
        public String text;
        public int distance;
        public String latitude;
        public String longitude;
        public HashMap<Long, Integer> mapPlaces;
        public HashMap<Long, Integer> mapUsers;
    }

    private static String sendDataFromServer(final String json,
                                             final String url) {
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
            entity.addPart("json_string", new StringBody(json, App.ContentTypeUTF8));
            httppost.setEntity(entity);
            System.out.println("entity " + entity);
            Log.d("entity", entity.toString());
            System.out.println(entity.toString());
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
        System.out.println("responseString login" + responseString);
        System.out.println(responseString);
        System.out.println("responseString " + responseString);
        return responseString;
    }


    public void showView(View view, int slideEdge) {
        if (view != null) {
            ViewGroup parent = findViewById(R.id.view_container);

            Transition transition = new Slide(slideEdge);
            transition.setDuration(200);
            transition.addTarget(view);

            TransitionManager.beginDelayedTransition(parent, transition);
            view.setVisibility(View.VISIBLE);
        }
    }

    public void hideView(View view, int slideEdge) {
        if (view != null) {
            ViewGroup parent = findViewById(R.id.view_container);

            Transition transition = new Slide(slideEdge);
            transition.setDuration(200);
            transition.addTarget(view);

            TransitionManager.beginDelayedTransition(parent, transition);
            view.setVisibility(View.GONE);
        }
    }
}
