package com.wearetogether.v2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.app.user.DataResultLogin;
import com.wearetogether.v2.ui.activities.EditProfileActivity;
import com.wearetogether.v2.ui.activities.SettingsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.smodel.SUser;

public class PreferenceUtils {
//    public static void SaveUserLogin(Context context, DataResultLogin.User _user) {
//        SUser user = new SUser();
//        user.unic = _user.getUnic();
//        user.id = _user.getId();
//        user.social_id = _user.getSocialId();
//        user.email = _user.getEmail();
//        user.name = _user.getName();
//        user.avatar = _user.getAvatar();
//        user.latitude = _user.getLatitude();
//        user.longitude = _user.getLongitude();
//        user.location = _user.getLocation();
//        user.country = _user.getCountry();
//        user.city = _user.getCity();
//        user.sex = _user.getSex();
//        user.show_sex = _user.getShowSex();
//        user.status = _user.getStatus();
//        user.custom_status = _user.getCustomStatus();
//        user.show_in_map = _user.getShowInMap();
//        user.show_age = _user.getShowAge();
//        SaveUserLogin(context, user);
//    }

    public static void SaveUserLogin(Context context, SUser user) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.UNIC, user.unic);
        editor.putString(Consts.USER_ID, user.id);
        editor.putString(Consts.SOCIAL_ID, user.social_id);
        editor.putString(Consts.EMAIL, user.email);
        editor.putString(Consts.NAME, user.name);
        editor.putString(Consts.AVATAR, user.avatar);
        editor.putString(Consts.LATITUDE, user.latitude);
        editor.putString(Consts.LONGITUDE, user.longitude);
        editor.putString(Consts.LOCATION, user.location);
        editor.putString(Consts.COUNTRY, user.country);
        editor.putString(Consts.CITY, user.city);
        editor.putString(Consts.SEX, user.sex);
        editor.putString(Consts.SHOW_SEX, user.show_sex);
        editor.putString(Consts.STATUS, user.status);
        editor.putString(Consts.CUSTOM_STATUS, user.custom_status);
        editor.putString(Consts.SHOW_IN_MAP, user.show_in_map);
        editor.putString(Consts.DATE_BIRTH, user.date_birth);
        editor.putString(Consts.SHOW_AGE, user.show_age);
        System.out.println("login user");
        System.out.println(user);
        System.out.println("unic " + user.unic);
        System.out.println("id " + user.id);
        System.out.println("name " + user.name);
        System.out.println("avatar " + user.avatar);
        System.out.println("location " + user.location);
        System.out.println("latitude " + user.latitude);
        System.out.println("longitude " + user.longitude);
        editor.apply();
    }

    public static void SaveUserAvatar(Context context, String path) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.AVATAR, path);
        editor.apply();
    }

    public static SUser GetUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Consts.USER_ID)) {
            SUser user = new SUser();
            user.id = sharedPreferences.getString(Consts.USER_ID, "");
            user.unic = sharedPreferences.getString(Consts.UNIC, "");
            user.latitude = sharedPreferences.getString(Consts.LATITUDE, "0");
            user.longitude = sharedPreferences.getString(Consts.LONGITUDE, "0");
            user.location = sharedPreferences.getString(Consts.LOCATION, "");
            user.country = sharedPreferences.getString(Consts.COUNTRY, "");
            user.city = sharedPreferences.getString(Consts.CITY, "");
            user.version = sharedPreferences.getString(Consts.VERSION, "-1");
            user.social_id = sharedPreferences.getString(Consts.SOCIAL_ID, "");
            user.name = sharedPreferences.getString(Consts.NAME, "");
            user.email = sharedPreferences.getString(Consts.EMAIL, "");
            user.avatar = sharedPreferences.getString(Consts.AVATAR, "");
            user.sex = sharedPreferences.getString(Consts.SEX, "0");
            user.show_sex = sharedPreferences.getString(Consts.SHOW_SEX, "1");
            user.status = sharedPreferences.getString(Consts.STATUS, "1");
            user.custom_status = sharedPreferences.getString(Consts.CUSTOM_STATUS, "");
            user.show_in_map = sharedPreferences.getString(Consts.SHOW_IN_MAP, "0");
            user.date_birth = sharedPreferences.getString(Consts.DATE_BIRTH, "");
            user.show_age = sharedPreferences.getString(Consts.SHOW_AGE, "1");
            user.show_anonymous_visits = sharedPreferences.getString(Consts.SHOW_ANONYMOUS_VISITS, "1");
            user.show_visits = sharedPreferences.getString(Consts.SHOW_VISITS, "1");
            System.out.println("get pref user");
            System.out.println(user);
            return user;
        } else return null;
    }

    public static void SetContinue(Context context, boolean state) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CONTINUE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("continue", state);
        editor.commit();
    }

    public static boolean IsContinue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CONTINUE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("continue", false);
    }

    public static Boolean GetLog(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.LOG_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("log", false);
    }

    public static void SaveLog(Context context, boolean log) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.LOG_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("log", log);
        editor.commit();
    }

    public static int GetSquareSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.wearetogether.v2.SQUARE_SIZE", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("size", 300);
    }

    public static void SaveGetSquareSize(FragmentActivity activity) {
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences("com.wearetogether.v2.SQUARE_SIZE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("size", FileUtils.GetSquareSize(activity));
        editor.commit();

    }

    public static String GetDateFormat() {
        return "dd MMM yyyy";
    }

    public static void SaveCommentText(Context context, String title) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.COMMENT_TEXT_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.TITLE, title);
        editor.apply();
    }

    public static String GetCommentText(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.COMMENT_TEXT_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Consts.TITLE, "");
    }

    public static void SaveCommentUnic(Context context, long unic) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.COMMENT_UNIC_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Consts.UNIC, unic);
        editor.apply();
    }

    public static Long GetCommentUnic(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.COMMENT_UNIC_PREFERENCES, Context.MODE_PRIVATE);
        long unic = sharedPreferences.getLong(Consts.UNIC, 0);
        return unic == 0 ? null : unic;
    }

    public static void SaveCommentParent(Context context, long unic) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.COMMENT_PARENT_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Consts.UNIC, unic);
        editor.apply();
    }

    public static Long GetCommentParent(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.COMMENT_PARENT_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(Consts.UNIC, 0);
    }

    public static void SaveCameraBounds(Context context, LatLngBounds bounds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CAMERA_BOUNDS_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.NORTHEAST_LATITUDE, String.valueOf(bounds.northeast.latitude));
        editor.putString(Consts.NORTHEAST_LONGITUDE, String.valueOf(bounds.northeast.longitude));
        editor.putString(Consts.SOUTTHWEST_LATITUDE, String.valueOf(bounds.southwest.latitude));
        editor.putString(Consts.SOUTTHWEST_LONGITUDE, String.valueOf(bounds.southwest.longitude));
        editor.apply();
    }

    public static LatLngBounds GetCameraBounds(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CAMERA_BOUNDS_PREFERENCES, Context.MODE_PRIVATE);
        String northeast_latitude = sharedPreferences.getString(Consts.NORTHEAST_LATITUDE, "");
        String northeast_longitude = sharedPreferences.getString(Consts.NORTHEAST_LONGITUDE, "");
        String southwest_latitude = sharedPreferences.getString(Consts.SOUTTHWEST_LATITUDE, "");
        String southwest_longitude = sharedPreferences.getString(Consts.SOUTTHWEST_LONGITUDE, "");
        if (!northeast_latitude.equals("") && !northeast_longitude.equals("") && !southwest_latitude.equals("") && !southwest_longitude.equals("")) {
            LatLng northeast = new LatLng(Double.parseDouble(northeast_latitude), Double.parseDouble(northeast_latitude));
            LatLng southwest = new LatLng(Double.parseDouble(northeast_latitude), Double.parseDouble(northeast_latitude));
            return new LatLngBounds(northeast, southwest);
        }
        return null;
    }

    public static void SaveCloudMessageToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CLOUD_MESSAGE_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String GetCloudMessageToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CLOUD_MESSAGE_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    public static void SaveCameraPosition(Context context, LatLng latLng) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CAMERA_POSITION_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.LATITUDE, String.valueOf(latLng.latitude));
        editor.putString(Consts.LONGITUDE, String.valueOf(latLng.longitude));
        editor.apply();
    }

    public static LatLng GetCameraPosition(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CAMERA_POSITION_PREFERENCES, Context.MODE_PRIVATE);
        String latitude = sharedPreferences.getString(Consts.LATITUDE, "");
        String longitude = sharedPreferences.getString(Consts.LONGITUDE, "");
        if (!latitude.equals("") && !longitude.equals("")) {
            return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
        return null;
    }

    public static void SaveCameraZoom(Context context, float zoom) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.ZOOM_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.ZOOM, String.valueOf(zoom));
        editor.apply();
    }

    public static float GetCameraZoom(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.ZOOM_PREFERENCES, Context.MODE_PRIVATE);
        String zoom = sharedPreferences.getString(Consts.ZOOM, "");
        if (!zoom.equals("")) {
            return Float.parseFloat(zoom);
        }
        return 16;
    }

    public static void SetRegister(Context context, boolean is) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.REGISTER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is", is);
        editor.apply();
    }

    public static boolean GetRegister(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.REGISTER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is", false);
    }

    public static void SaveUserLocation(Context context, double latitude, double longitude, String location, String country, String city) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.USER_LOCATION_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.LATITUDE, String.valueOf(latitude));
        editor.putString(Consts.LONGITUDE, String.valueOf(longitude));
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (latitude != 0) editor.putString(Consts.LATITUDE, String.valueOf(latitude));
        if (longitude != 0) editor.putString(Consts.LONGITUDE, String.valueOf(longitude));
        if (location != null) editor.putString(Consts.LOCATION, location);
        if (country != null) editor.putString(Consts.COUNTRY, country);
        if (city != null) editor.putString(Consts.CITY, city);
        editor.apply();
    }

    public static Double[] GetUserLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.USER_LOCATION_PREFERENCES, Context.MODE_PRIVATE);
        String latitude = sharedPreferences.getString(Consts.LATITUDE, "");
        String longitude = sharedPreferences.getString(Consts.LONGITUDE, "");
        if (!latitude.equals("") && !longitude.equals("")) {
            return new Double[]{Double.parseDouble(latitude), Double.parseDouble(longitude)};
        }
        return null;
    }

    public static void Clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.BOTTOM_SHEET_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.VERSION_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.IMAGES_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.SETTINGS_MAP_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.CAMERA_POSITION_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.REGISTER_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences(Consts.DOWNLOAD_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        sharedPreferences = context.getSharedPreferences("continue", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void SaveSettings(SettingsActivity activity) {
        String show_sex = activity.checkbox_show_sex.isChecked() ? "1" : "0";
        String show_in_map = (String) activity.spinner_show_in_map.getTag();
        String show_age = activity.checkbox_show_age.isChecked() ? "1" : "0";
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.SHOW_SEX, show_sex);
        editor.putString(Consts.SHOW_IN_MAP, show_in_map);
        editor.putString(Consts.SHOW_AGE, show_age);
        editor.commit();
    }

    public static void SaveProfile(EditProfileActivity activity) {
        String name = App.CapitalizeString(activity.edit_text_login.getText().toString());
//        String first_name = App.CapitalizeString(activity.edit_text_first_name.getText().toString());
//        String last_name = App.CapitalizeString(activity.edit_text_last_name.getText().toString());
        String sex_id = activity.view_sex.getTag() == null ? "0" : (String) activity.view_sex.getTag();
        String status_id = activity.view_status.getTag() == null ? "0" : (String) activity.view_status.getTag();
        String custom_status = activity.edit_text_custom_status.getText().toString();
        String date_birth = activity.view_date_birth.getTag() == null ? "" : (String) activity.view_date_birth.getTag();
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.NAME, name);
        editor.putString(Consts.SEX, sex_id);
        editor.putString(Consts.STATUS, status_id);
        editor.putString(Consts.CUSTOM_STATUS, custom_status);
        editor.putString(Consts.DATE_BIRTH, date_birth);
        editor.commit();
    }

    public static void SetDownload(Context context, boolean state) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.DOWNLOAD_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("download", state);
        editor.commit();
    }

    public static boolean IsDownload(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.DOWNLOAD_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("download", false);
    }

    public static void SaveTab(int tab, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tab", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tab", tab);
        editor.commit();
    }

    public static int GetTab(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tab", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("tab", 0);
    }

    public static boolean getEnableSoundNotification(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("enable_sound_notification", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("enable_sound_notification", true);
    }

    public static void SaveEnableSoundNotification(boolean enable, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("enable_sound_notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("enable_sound_notification", enable);
        editor.commit();
    }
}
