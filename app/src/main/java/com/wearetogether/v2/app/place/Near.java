package com.wearetogether.v2.app.place;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.database.model.Visit;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.map.AbstractMarker;
import com.wearetogether.v2.ui.map.PlaceMarker;
import com.wearetogether.v2.ui.map.UserMarker;
import com.wearetogether.v2.utils.*;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.Cluster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Near {

    private static int progress = 0;


    public static void Start(final MainActivity activity, final CurrentTab currentTab, final Cluster<AbstractMarker> cluster, final MapOptions mapOptions) {
        List<AbstractMarker> markers = (List<AbstractMarker>) cluster.getItems();
        List<String> longList = new ArrayList<>();
        for (AbstractMarker marker : markers) {
            if (marker instanceof PlaceMarker) {
                longList.add(marker.getUnic());
            } else if (marker instanceof UserMarker) {
                longList.add(marker.getUnic());
            }
        }

        if (currentTab == CurrentTab.Places || currentTab == CurrentTab.Users) {
            activity.adapterGroupNearby.update(new ArrayList<>(), null);
            activity.view_bottom_sheet_nearby.setVisibility(View.VISIBLE);
            activity.bottom_sheet_nearby.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            ProgressBar view_progress_nearby = activity.findViewById(R.id.view_progress_nearby);
            view_progress_nearby.setVisibility(View.VISIBLE);
            view_progress_nearby.setMax(longList.size());
            view_progress_nearby.setProgress(0);
            progress = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Category> listCategories = App.Database.daoCategory().getAll();
                    final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                    final long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
                    final List<DataGroup> dataGroups = new ArrayList<>();
                    if (currentTab == CurrentTab.Places) {
                        Calendar calendar = Calendar.getInstance();
                        ShowPlace showPlace;
                        Place place;
                        Boolean isBegin = false;
                        Boolean isEnd = false;
                        for (int x = 0; x < longList.size(); x++) {
                            showPlace = App.Database.daoPlace().getShow(Long.valueOf(longList.get(x)));
                            if(showPlace != null) {
                                place = showPlace.getPlace();
                                if(ObjectUtils.IsShow(place, mapOptions, calendar, isBegin, isEnd)) {
                                    dataGroups.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                                    progress++;
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view_progress_nearby.setProgress(progress);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        System.out.println("build user list");
                        ShowUser showUser;
                        User user;
                        for (int x = 0; x < longList.size(); x++) {
                            System.out.println("user " + x);
                            showUser = App.Database.daoUser().getShow(Long.valueOf(longList.get(x)));
                            if(showUser != null) {
                                user = showUser.getUser();
                                if(ObjectUtils.IsShow(user, mapOptions, user_unic)) {
                                    System.out.println("user " + showUser);
                                    dataGroups.add(new DataGroup().User(ObjectUtils.Build(user, user_unic), user_unic));
                                    progress++;
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view_progress_nearby.setProgress(progress);
                                        }
                                    });
                                }
                            }
                        }
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<DataGroup> resultPosts = new ArrayList<>();
                            if (currentTab == CurrentTab.Places) {
                                resultPosts.add(new DataGroup(activity.getString(R.string.places_nearby), DataGroup.TYPE_HEADER));
                            } else {
                                resultPosts.add(new DataGroup(activity.getString(R.string.users_nearby), DataGroup.TYPE_HEADER));
                            }
                            ToastUtils.Short(activity.getApplicationContext(), "longList.size " + longList.size());
                            if (currentTab == CurrentTab.Places) {
                                if (dataGroups.size() > 0) {
                                    resultPosts.addAll(dataGroups);
                                } else {
                                    resultPosts.add(new DataGroup(activity.getString(R.string.not_places), DataGroup.TYPE_TEXT));
                                }
                            } else {
                                if (dataGroups.size() > 0) {
                                    resultPosts.addAll(dataGroups);
                                } else {
                                    resultPosts.add(new DataGroup(activity.getString(R.string.not_users), DataGroup.TYPE_TEXT));
                                }
                            }
                            if (resultPosts.size() > 0) {
                                activity.adapterGroupNearby.update(resultPosts, array_categories);
                            } else {
                                activity.hideNearby();
                            }
                            view_progress_nearby.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();
        }
    }
}
