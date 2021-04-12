package com.wearetogether.v2.app.place;

import android.graphics.Bitmap;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Save {
    public static final int ERROR_LOCATION = 1;
    public static final int ERROR_LATLNG = 2;
    public static final int ERROR_TITLE = 3;
    public static final int ERROR_SNAPSHOT = 4;
    private static Boolean isEdit = false;

    public static void Start(final FormPlaceActivity activity, boolean isBackPressed) {
        if(App.SUser == null) return;
        Bitmap snapshot = activity.getViewModel().snapshotMutableLiveData.getValue();
        String location = activity.getViewModel().locationMutableLiveData.getValue();
        System.out.println("snapshot "+snapshot);
        System.out.println("location "+location);
        if (location == null) {
            if (isBackPressed) {
                activity.back();
            } else {
                activity.showError(ERROR_LOCATION);
            }
            return;
        }
        LatLng latLng = activity.getViewModel().latLngMutableLiveData.getValue();
        if (latLng == null) {
            if (isBackPressed) {
                activity.back();
            } else {
                activity.showError(ERROR_LATLNG);
            }
            return;
        }
        if (activity.edit_voice_text_title.isEmpty(1)) {
            if (isBackPressed) {
                activity.back();
            } else {
                activity.showError(ERROR_TITLE);
            }
            return;
        }
        if (snapshot == null) {
            activity.showError(ERROR_SNAPSHOT);
            return;
        }
        String date_public = getDate(Calendar.getInstance(), Consts.Format_yyyy_MM_dd);
        String date_begin = getDate(activity.getViewModel().calendarDateBeginMutableLiveData.getValue(), Consts.Format_yyyy_MM_dd);
        String date_end = getDate(activity.getViewModel().calendarDateEndMutableLiveData.getValue(), Consts.Format_yyyy_MM_dd);
        String time_visit = getDate(activity.getViewModel().calendarTimeVisitMutableLiveData.getValue(), Consts.Format_HH_mm);
        activity.showProgressBar(true);
        final int user_id = App.SUser.getUserId();
        final long user_unic = Long.parseLong(App.SUser.unic);
        final Long place_unic = activity.getViewModel().unicMutableLiveData.getValue();
        final Long temp_place_unic = getTempUnic(activity.getViewModel().tempUnicMutableLiveData.getValue());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Place place = null;
                if (place_unic == null || place_unic == 0) {
                    place = new Place();
                    place.user_unic = user_unic;
                    place.unic = temp_place_unic;
                    isEdit = false;
                } else {
                    place = App.Database.daoPlace().get(place_unic);
                    isEdit = true;
                }
                try {
                    List<MediaItem> mediaItemList = App.Database.daoMediaItem().getList(place.unic);
                    if(mediaItemList.size() == 0) {
                        place.icon = createSnapshot(snapshot, activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    place.icon = "";
                }
                place.title = activity.edit_voice_text_title.getText();
                place.description = activity.edit_voice_text_description.getText();
                place.latitude = latLng.latitude;
                place.longitude = latLng.longitude;
                place.address = location;
                place.anonymous_visit = activity.checkbox_anonymous_visit.isChecked() ? 1 : 0;
                place.date_begin = date_begin;
                place.date_end = date_end;
                place.time_visit = time_visit;
                place.date_public = date_public;
                int count_participant = 0;
                if (activity.edit_text_count_participant.getText() != null) {
                    String string_count_participant = String.valueOf(activity.edit_text_count_participant.getText());
                    if (!string_count_participant.equals("")) {
                        count_participant = Integer.parseInt(String.valueOf(activity.edit_text_count_participant.getText()));
                    }
                }
                place.count_participant = count_participant;
                place.category_id = activity.view_category.getTag() == null ? 1 : (Integer) activity.view_category.getTag();
                place.disable_comments = activity.checkbox_disable_comments.isChecked() ? 1 : 0;
                place.only_for_friends = activity.checkbox_only_for_friends.isChecked() ? 1 : 0;

                if (place.icon.equals("")) {
                    MediaItem mediaItem = App.Database.daoMediaItem().getStar(place.unic);
                    if (mediaItem != null) {
                        place.icon = mediaItem.icon;
                    }
                }

                if (!isEdit) {
                    place.is_remove = 0;
                    place.user_avatar = App.SUser.avatar;
                    place.user_name = App.SUser.name;
                    App.Database.daoPlace().Insert(place);

                    ItemLog log = new ItemLog();
                    log.unic = Calendar.getInstance().getTimeInMillis();
                    log.action = Consts.LOG_ACTION_INSERT_PLACE;
                    log.item_unic = place.unic;
                    App.Database.daoLog().insert(log);
                } else {
                    boolean is_update = false;
                    boolean is_update_location = false;
                    Place oldPlace = App.Database.daoPlace().get(place.unic);
                    if (oldPlace != null) {
                        if (!oldPlace.title.equals(place.title) ||
                                !oldPlace.description.equals(place.description) ||
                                !oldPlace.anonymous_visit.equals(place.anonymous_visit) ||
                                !oldPlace.date_begin.equals(place.date_begin) ||
                                !oldPlace.date_end.equals(place.date_end) ||
                                !oldPlace.time_visit.equals(place.time_visit) ||
                                !oldPlace.count_participant.equals(place.count_participant) ||
                                !oldPlace.category_id.equals(place.category_id) ||
                                !oldPlace.disable_comments.equals(place.disable_comments) ||
                                !oldPlace.only_for_friends.equals(place.only_for_friends)) {
                            is_update = true;
                        }
                        if (!oldPlace.latitude.equals(place.latitude) ||
                                !oldPlace.longitude.equals(place.longitude)) {
                            is_update_location = true;
                        }
                        if (is_update) {
                            App.Database.daoPlace().update(place);

                            ItemLog.UpdatePlace(place.unic, Consts.LOG_ACTION_UPDATE_PLACE);
                        }
                        if (is_update_location) {
                            App.Database.daoPlace().update(place);

                            ItemLog.UpdatePlace(place.unic, Consts.LOG_ACTION_UPDATE_PLACE_LOCATION);
                        }
                    }
                }
                final long unic = place.unic;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferenceUtils.SaveLog(activity.getApplicationContext(), true);


                        App.IsUpdate = true;
                        activity.back();
                    }
                });
            }
        }).start();
    }

    private static Long getTempUnic(Long value) {
        if (value == null) {
            value = Calendar.getInstance().getTimeInMillis();
        }
        return value;
    }

    private static String createSnapshot(Bitmap snapshot, FragmentActivity activity) throws Exception {
        File file = FileUtils.CropSquare(snapshot, activity);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    private static String getDate(Object tag, String pattern) {
        try {
            if (tag == null) return "";
            Calendar calendar = (Calendar) tag;
            SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
            return simpledateformat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
