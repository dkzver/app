package com.wearetogether.v2.app.message;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.message.data.DataFromServer;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.RealTimeMessageData;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.services.GCMSenderService;
import com.wearetogether.v2.smodel.SMessage;
import com.wearetogether.v2.smodel.SRoom;
import com.wearetogether.v2.smodel.SRoomParticipant;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Add {

    static class PictureData {
        String original;
        String small;
        String icon;
        HashMap<String, String> map = new HashMap<>();

        public PictureData(String original, String small, String icon) {
            this.original = original;
            this.small = small;
            this.icon = icon;
            map.put("original", original);
            map.put("small", small);
            map.put("icon", icon);
        }

        public String getJson() {
            Gson gson = new Gson();
            return gson.toJson(this, PictureData.class);
        }

        public HashMap<String, String> getMap() {
            return map;
        }

        public String get(String key) {
            return map.get(key);
        }

        public Set<String> getKeySet() {
            return map.keySet();
        }
    }

    public static void Voice(final String file, final long message_unic, final String url, final FragmentActivity activity) {

    }

    public static void Picture(final String original, final String small, final String picture_icon, final RoomActivity activity, long message_unic, final String url) {
        if (App.SUser == null) return;
        final String str_user_unic = App.SUser.unic;
        final String str_message_unic = String.valueOf(message_unic);
        final Long user_unic = Long.parseLong(str_user_unic);
        int index = activity.adapterGroup.prepareMessage(picture_icon, str_message_unic, user_unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PictureData pictureData = new PictureData(original, small, picture_icon);
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
                    File file;
                    for (String key : pictureData.getKeySet()) {
                        file = new File(pictureData.get(key));
                        if (file.exists()) {
                            entity.addPart(key + "_" + str_message_unic, new FileBody(file));
                        }
                    }
                    entity.addPart("unic", new StringBody(str_user_unic, App.ContentTypeUTF8));
                    entity.addPart("message_unic", new StringBody(str_message_unic, App.ContentTypeUTF8));
                    entity.addPart("room_unic", new StringBody(String.valueOf(App.RoomUnic), App.ContentTypeUTF8));
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
                File file;
                for (String key : pictureData.getKeySet()) {
                    if(key.equals("icon")) continue;
                    file = new File(pictureData.get(key));
                    if (file.exists()) {
                        file.delete();
                    }
                }

                DataFromServer dataFromServer = DataFromServer.Parse(responseString);
                if (dataFromServer != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.unic = message_unic;
                            message.room_unic = App.RoomUnic;
                            message.user_unic = user_unic;
                            message.content = dataFromServer.data.getJson();
                            message.type = Consts.MESSAGE_TYPE_PICTURE;
                            message.is_read = "0";

                            activity.getViewModel().write(message);
                            activity.adapterGroup.updatePicture(index, str_message_unic, str_user_unic, picture_icon, small, original);
                        }
                    });
                }
            }
        }).start();
    }

    public static class Data {
        public List<String> registration_ids;
        public List<String> user_unics;
        public Content data;
        public String unic;
    }

    public static class Content {
        public String action;
        public Json json;
    }

    public static class Json {
        public SUser user;
        public SRoom room;
        public List<SRoomParticipant> roomParticipants;
        public int message_type;
        public String message_content;
        public String log_unic;
    }

    public static void Text(final RoomActivity activity, Long room_unic) {
        activity.component_write_message.setEnabled(false);
        if (App.SUser == null) return;
        App.RoomUnic = room_unic;
        try {
            String text = activity.component_write_message.getText();
            if (text.length() < 1) {
                throw new Exception("Error text len");
            }
            final Long user_unic = Long.parseLong(App.SUser.unic);
            final Long message_unic = Calendar.getInstance().getTimeInMillis();
            Message message = new Message();
            message.unic = message_unic;
            message.room_unic = App.RoomUnic;
            message.user_unic = user_unic;
            message.content = text;
            message.type = Consts.MESSAGE_TYPE_TEXT;
            message.is_read = "0";

            activity.getViewModel().write(message);
            activity.component_write_message.setText("");
            App.HideKeyboard(activity.getApplicationContext(), new View[]{activity.component_write_message.edit_text});
            activity.component_write_message.setEnabled(true);
            activity.scrollToBottom();

            HashMap<String, RealTimeMessageData> mapMessages = activity.getViewModel().mapMessagesMutableLiveData.getValue();
            if (mapMessages == null) {
                mapMessages = new HashMap<>();
            }
            final int sizeMapMessages = mapMessages.size();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isSend = false;
                    boolean isFirstSend = false;
                    List<Long> longList = new ArrayList<>();
                    List<RoomParticipant> roomParticipantList = new ArrayList<>();
                    List<String> tokens = new ArrayList<>();
                    List<String> unics = new ArrayList<>();
                    final List<SRoomParticipant> sRoomParticipants = new ArrayList<>();

                    Room room = App.Database.daoRoom().get(App.RoomUnic);


                    if (room != null) {
                        if (sizeMapMessages == 0) {
                            isFirstSend = true;
                        }
                        roomParticipantList = App.Database.daoRoomParticipant().get(room.unic);
                        isSend = true;
                    }
                    if (isSend) {
                        for (RoomParticipant roomParticipant : roomParticipantList) {
                            longList.add(roomParticipant.user_unic);
                            if (!roomParticipant.user_unic.equals(user_unic)) {
                                User user = App.Database.daoUser().get(roomParticipant.user_unic);
                                if (user != null) {
                                    tokens.add(user.token);
                                    unics.add(String.valueOf(user.unic));
                                }
                            }
                            sRoomParticipants.add(roomParticipant.get());
                        }


                        Json json = new Json();

                        json.user = App.SUser;
                        json.room = room.get();
                        json.roomParticipants = sRoomParticipants;
                        json.message_type = message.type;
                        json.message_content = message.content;
                        String log_unic = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        json.log_unic = log_unic;

                        Content content = new Content();
                        content.action = Consts.ACTION_SEND_MESSAGE;
                        content.json = json;
                        Data data = new Data();
                        data.registration_ids = new ArrayList<>();
                        data.registration_ids.addAll(tokens);
                        data.user_unics = new ArrayList<>();
                        data.user_unics.addAll(unics);
                        data.data = content;
                        data.unic = log_unic;
                        Gson gson = new Gson();
                        String json_string = gson.toJson(data, Data.class);

                        if (isFirstSend) {
                            String responce = sendDataFromServer(
                                    json_string,
                                    activity.getString(R.string.url_send_message));
                        } else {
                            GCMSenderService.StartServer(activity, json_string);
                        }

                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            activity.component_write_message.setEnabled(true);
        }
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
