package com.wearetogether.v2;

import android.location.LocationManager;

public class Consts {
    public static final int MAX_COUNT_COMMENT = 5;
    public static final int MIN_COMMENT_LEN = 1;
    public static final String MESSAGE = "message";
    public static final int MIN_DISTANCE = 150;
    public static final int QUALITY_ORIGINAL = 70;
    public static final int QUALITY_SMALL = 10;
    public static final String ZOOM = "zoom";
    public static final int MAX_COUNT_VISIT = 5;
    public static final int MAX_COUNT_FRIENDS = 1;
    public static final String EXTRA_TEXT_REPLY = "EXTRA_TEXT_REPLY";
    public static final int TYPE_USER = 1;
    public static final int TYPE_PLACE = 2;
    public static final int TYPE_COMMENT = 3;
    public static final int TYPE_PROFILE = 4;
    public static final int TYPE_BACKED = 5;
    public static final int TYPE_PLACE_HEADER = 6;
    public static final int TYPE_USER_HEADER = 7;
    public static final int SIZE_ICON = 150;
    public static final int SIZE_MAP_ICON = 110;
    public static final String EMAIL_ADMIN = "dkrstudio@gmail.com";
    public static final String NEW_ROOM = "NEW_ROOM";
    public static final String ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE";
    public static final int NOTIFICATION_TYPE_ROOM = 1;
    public static final int NOTIFICATION_TYPE_MESSAGE = 2;
    public static final int NOTIFICATION_TYPE_REQUEST_FRIEND = 3;
    public static final int NOTIFICATION_TYPE_ASSEPT_FRIEND = 4;
    public static final int NOTIFICATION_TYPE_VISITED = 5;
    public static final int NOTIFICATION_TYPE_LIKED = 6;
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final int MESSAGE_TYPE_TEXT = 1;
    public static final int MESSAGE_TYPE_PICTURE = 2;
    public static final String NOTIFICATION_ACTION_REQUEST_FRIEND = "NOTIFICATION_ACTION_REQUEST_FRIEND";
    public static final String NOTIFICATION_ACTION_ASSEPT_FRIEND = "NOTIFICATION_ACTION_ASSEPT_FRIEND";
    public static final String NOTIFICATION_ACTION_VISITED = "NOTIFICATION_ACTION_VISITED";
    public static final String NOTIFICATION_ACTION_LIKED = "NOTIFICATION_ACTION_LIKED";
    public static final String ACTION_ACCEPT_FRIEND = "ACTION_ACCEPT_FRIEND";
    public static final String ACTION_ACCEPT_FRIEND_REQUEST = "ACTION_ACCEPT_FRIEND_REQUEST";
    public static final long MINUTES_ACTIVE_USER = 2;
    public static final int CACHE_MAX_SIZE = 4194304 * 4;
    public static final String URL_MARKET_PLACE = "https://play.google.com/store/apps/details?id=com.wearetogether.v2";
    public static final String ENCRYPTION_STRING_KEY = "whereistheparty.2020";
    public static String Format_yyyy_MM_dd = "yyyy-MM-dd";
    public static String Format_HH_mm = "HH:mm";
    public static int CAMERA_MOVE_REACT_THRESHOLD_MS = 800;
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    public static final long INTERVAL_RUN_SERVICE = 30000;

    public static final int DISTANCE_CHECK_PLACE_VISIT = 100;

    public static final int REQUEST_IMAGE_CAPTURE   = 1;
    public static final int REQUEST_SIGN_IN_GOOGLE  = 2;
    public static final int REQUEST_VOICE_TITLE = 3;
    public static final int REQUEST_VOICE_DESCRIPTION = 4;
    public static final int REQUEST_VOICE_LOCATION = 5;
    public static final int REQUEST_FINE_LOCATION = 6;
    public static final int REQUEST_COARSE_LOCATION = 7;
    public static final int REQUEST_VOICE_HINT = 8;
    public static final int REQUEST_CHECK_SETTINGS_GPS = 9;
    public static final int REQUEST_ENABLE_WIFI = 10;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 11;
    public static final int REQUEST_IGNORE_BATTERY = 12;
    public static final int REQUEST_VOICE_COMMENT = 13;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 14;
    public static final int REQUEST_PERMISSIONS_LOCATION = 15;
    public static final int PERMISSION_FINE_LOCATION = 16;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE_CAMERA = 17;
    public static final int REQUEST_CAMERA_CAPTURE = 18;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE_GALLERY = 19;
    public static final int REQUEST_GALLERY_CAPTURE = 20;
    public static final int REQUEST_CROP_ORIGINAL = 21;
    public static final int REQUEST_CROP_ICON = 22;
    public static final int REQUEST_PERMISSIONS_STORAGE = 23;
    public static final int REQUEST_CODE_LOCATION = 24;
    public static final int REQUEST_VOICE_SEARCH = 25;
    public static final int PERMISSION_RECORD_AUDIO = 26;



    public static final String FRIEND_UNIC = "friend_unic";
    public static final String IS_COMMENTES = "is_commentes";
    public static final String ONLY_FOR_FRIENDS = "only_for_friends";
    public static final String PUBLIC = "public";
    public static final String NEAR = "near";
    public static final String NORTHEAST_LATITUDE = "NORTHEAST_LATITUDE";
    public static final String NORTHEAST_LONGITUDE = "NORTHEAST_LONGITUDE";
    public static final String SOUTTHWEST_LATITUDE = "SOUTTHWEST_LATITUDE";
    public static final String SOUTTHWEST_LONGITUDE = "SOUTTHWEST_LONGITUDE";
    public static final String INDEX = "INDEX";
    public static final String TAB = "TAB";
    public static final String TAB_PROFILE = "TAB_PROFILE";
    public static final String TYPE = "TYPE";
    public static final String SHOW_VISITS = "SHOW_VISITS";
    public static final String SHOW_ANONYMOUS_VISITS = "SHOW_ANONYMOUS_VISITS";
    public static final String AVATAR = "AVATAR";
    public static final String EMAIL = "EMAIL";
    public static final String SOCIAL_ID = "SOCIAL_ID";
    public static final String IS_ONLINE = "IS_ONLINE";
    public static final String USER_ID = "USER_ID";
    public static final String NAME = "NAME";
    public static final String SEX = "SEX";
    public static final String SHOW_SEX = "SHOW_SEX";
    public static final String STATUS = "STATUS";
    public static final String CUSTOM_STATUS = "CUSTOM_STATUS";
    public static final String SHOW_IN_MAP = "SHOW_IN_MAP";
    public static final String DATE_BIRTH = "DATE_BIRTH";
    public static final String SHOW_AGE = "SHOW_AGE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String southwest_latitude = "southwest_latitude";
    public static final String northeast_latitude = "northeast_latitude";
    public static final String southwest_longitude = "southwest_longitude";
    public static final String northeast_longitude = "northeast_longitude";
    public static final String VERSION = "VERSION";
    public static final String LOCATION = "LOCATION";
    public static final String COUNTRY = "COUNTRY";
    public static final String CITY = "CITY";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DATE_BEGIN = "DATE_BEGIN";
    public static final String DATE_END = "DATE_END";
    public static final String UNIC = "UNIC";
    public static final String USER_UNIC = "USER_UNIC";
    public static final String ANONYMOUS_VISIT = "ANONYMOUS_VISIT";
    public static final String TIME_VISIT = "TIME_VISIT";
    public static final String COUNT_PARTICIPANT = "COUNT_PARTICIPANT";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CAPTURE = "CAPTURE";
    public static final String PLACE = "PLACE";
    public static final String PATH = "PATH";
    public static final String USER = "USER";
    public static final String STAR = "STAR";
    public static final String REMOVE = "REMOVE";
    public static final String POSITION = "POSITION";
    public static final String HINT = "HINT";
    public static final String SNAPSHOT = "SNAPSHOT";
    public static final String FILE_UNIC = "FILE_UNIC";
    public static final String JSON = "JSON";
    public static final String SHOW_PLACES = "SHOW_PLACES";
    public static final String SHOW_USERS = "SHOW_USERS";

    public static final String CLOUD_MESSAGE_TOKEN_PREFERENCES = "com.wearetogether.v2.CLOUD_MESSAGE_TOKEN_PREFERENCES";
    public static final String CAMERA_POSITION_PREFERENCES = "com.wearetogether.v2.CAMERA_POSITION_PREFERENCES";
    public static final String CAMERA_BOUNDS_PREFERENCES = "com.wearetogether.v2.CAMERA_BOUNDS_PREFERENCES";
    public static final String COMMENT_PARENT_PREFERENCES = "com.wearetogether.v2.COMMENT_PARENT_PREFERENCES";
    public static final String COMMENT_UNIC_PREFERENCES = "com.wearetogether.v2.COMMENT_UNIC_PREFERENCES";
    public static final String COMMENT_TEXT_PREFERENCES = "com.wearetogether.v2.COMMENT_TEXT_PREFERENCES";
    public static final String BOTTOM_SHEET_PREFERENCES = "com.wearetogether.v2.SAVE_BOTTOM_SHEET";
    public static final String IMAGES_PREFERENCES = "com.wearetogether.v2.IMAGES_PREFERENCES";
    public static final String USER_PREFERENCES = "com.wearetogether.v2.USER_PREFERENCES";
    public static final String VERSION_PREFERENCES = "com.wearetogether.v2.VERSION_PREFERENCES";
    public static final String DOWNLOAD_PREFERENCES = "com.wearetogether.v2.DOWNLOAD_PREFERENCES";
    public static final String PROVIDER_PREFERENCES = "com.wearetogether.v2.PROVIDER_PREFERENCES";
    public static final String ZOOM_PREFERENCES = "com.wearetogether.v2.ZOOM_PREFERENCES";
    public static final String REGISTER_PREFERENCES = "com.wearetogether.v2.REGISTER_PREFERENCES";
    public static final String SETTINGS_MAP_PREFERENCES = "com.wearetogether.v2.SETTINGS_MAP_PREFERENCES";
    public static final String LOG_PREFERENCES = "com.wearetogether.v2.LOG_PREFERENCES";
    public static final String CONTINUE_PREFERENCES = "com.wearetogether.v2.CONTINUE_PREFERENCES";
    public static final String FORM_PLACE_PREFERENCES = "com.wearetogether.v2.FORM_PLACE_PREFERENCES";
    public static final String USER_LOCATION_PREFERENCES = "com.wearetogether.v2.USER_LOCATION_PREFERENCES";




    public static final int LOG_ACTION_INSERT_PHOTO = 1;
    public static final int LOG_ACTION_REMOVE_PHOTO = 2;
    public static final int LOG_ACTION_UPDATE_HINT_PHOTO = 3;
    public static final int LOG_ACTION_UPDATE_STAR_PHOTO = 4;
    public static final int LOG_ACTION_INSERT_PLACE = 5;
    public static final int LOG_ACTION_UPDATE_PLACE = 6;
    public static final int LOG_ACTION_REMOVE_PLACE = 7;
//    public static final int LOG_ACTION_VOTE_USER = 8;
//    public static final int LOG_ACTION_VOTE_PLACE = 9;
    public static final int LOG_ACTION_VISIT = 10;
    public static final int LOG_ACTION_UPDATE_PLACE_LOCATION = 11;
    public static final int LOG_ACTION_FRIEND = 12;
    public static final int LOG_ACTION_UPDATE_PROFILE = 15;
    public static final int LOG_ACTION_UPDATE_USER_SETTINGS = 16;
    public static final int LOG_ACTION_UPDATE_USER_LOCATION = 17;
    public static final int LOG_ACTION_UPDATE_FIND_PARAMS = 18;
    public static final int LOG_ACTION_INSERT_COMMENT = 19;
    public static final int LOG_ACTION_UPDATE_COMMENT = 20;
    public static final int LOG_ACTION_VOTE = 21;
    public static final int LOG_ACTION_SHARE_FRIEND = 22;
    public static final int LOG_ACTION_UPDATE_USER_PROFILE = 24;
    public static final int LOG_ACTION_RECEIVED_MESSAGE = 25;
    public static final int LOG_ACTION_UPDATE_USER_INTERESTS = 26;
    public static final int LOG_ACTION_REMOVE_USER_LOG = 27;
    public static final int LOG_ACTION_DOWNLOAD_CATEGORIES = 28;
    public static final int LOG_ACTION_DOWNLOAD_STATUSES = 29;
    public static final int LOG_ACTION_DOWNLOAD_INTERESTS = 30;
    public static final int LOG_ACTION_READ_LOG = 31;
    public static final int LOG_ACTION_NEW_ROOM = 32;
    public static final int LOG_ACTION_ACTIVE = 33;
    public static final int LOG_ACTION_NEW_MESSAGES = 34;
    public static final int LOG_ACTION_READ_LOG_VISITED = 35;
    public static final int LOG_ACTION_READ_LOG_LIKED = 36;

    public static final int DATA_DOWNLOAD_END = 8624;
    public static final int ICON_SIZE = 100;
    public static final String Message = "com.wearetogether.v2.notificationexample.Message";
    public static final String ACTION = "com.wearetogether.v2.notificationexample.Action";
    public static final String PROGRESS = "PROGRESS";
    public static final int NOTIFICATION_BASE_ID = 1;
    public static String PROVIDER = LocationManager.NETWORK_PROVIDER;
}
