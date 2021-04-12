package com.wearetogether.v2.app.place;

import android.location.Location;
import android.view.View;
import android.widget.ProgressBar;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.database.model.Visit;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.map.AbstractMarker;
import com.wearetogether.v2.ui.map.MarkerItem;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.MapUtils;
import com.wearetogether.v2.utils.ObjectUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class NearPlace {
    private static int progress;

    public static void Start(final MainActivity activity, final AbstractMarker item, CurrentTab currentTab, Boolean viewOffline) {
        final Location targetLocation = MapUtils.LatLngToLocation(item.getPosition(), activity.getApplicationContext());
        Long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
        activity.adapterGroupNearby.update(new ArrayList<>(), null);
        activity.view_bottom_sheet_nearby.setVisibility(View.VISIBLE);
        activity.bottom_sheet_nearby.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        ProgressBar view_progress_nearby = activity.findViewById(R.id.view_progress_nearby);
        view_progress_nearby.setVisibility(View.VISIBLE);
        progress = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> listCategories = App.Database.daoCategory().getAll();
                final String[] array_categories = ListUtils.GetCategories(listCategories, activity.getApplicationContext());
                final List<DataGroup> dataGroups = new ArrayList<>();
                float distance = 0;
                boolean checkDistance = false;
                if(currentTab == CurrentTab.Places) {

                    final List<Place> placeList = new ArrayList<>();
                    for(Place place : App.Database.daoPlace().getAll(Long.parseLong(item.getUnic()))) {
                        if(place.is_remove == 0) {
                            if (place.only_for_friends == 1) {
                                Friend friend = App.Database.daoFriends().getByUser(place.user_unic);
                                if (friend != null) {
                                    if (friend.type == Friend.FRIEND) {
                                        checkDistance = true;
                                    }
                                }
                            } else {
                                checkDistance = true;
                            }
                        }
                        if(checkDistance) {
                            distance = place.getAddress(Consts.PROVIDER).distanceTo(targetLocation);
                            if (distance < Consts.MIN_DISTANCE) {
                                placeList.add(place);
                            }
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view_progress_nearby.setMax(placeList.size());
                            view_progress_nearby.setProgress(0);
                        }
                    });
                    for(Place place : placeList) {
                        dataGroups.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                        progress++;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view_progress_nearby.setProgress(progress);
                            }
                        });
                    }
                } else {
                    final List<User> userList = new ArrayList<>();
                    for(User user : App.Database.daoUser().getAll(Long.parseLong(item.getUnic()))) {
                        if(user.unic != user_unic) {
                            if(user.show_in_map == 1) {
                                checkDistance = true;
                            } else if(user.show_in_map == 2) {
                                Friend friend = App.Database.daoFriends().getByUser(user.unic);
                                if(friend != null) {
                                    if (friend.type == Friend.FRIEND) {
                                        checkDistance = true;
                                    }
                                }
                            }
                        }
                        if(checkDistance) {
                            distance = user.getLocation(Consts.PROVIDER).distanceTo(targetLocation);
                            if (distance < Consts.MIN_DISTANCE) {
                                userList.add(user);
                            }
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view_progress_nearby.setMax(userList.size());
                            view_progress_nearby.setProgress(0);
                        }
                    });
                    for(User user : userList) {
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

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<DataGroup> resultPosts = new ArrayList<>();
                        if (currentTab == CurrentTab.Places) {
                            resultPosts.add(new DataGroup(activity.getString(R.string.places_nearby), DataGroup.TYPE_HEADER));
                        } else {
                            resultPosts.add(new DataGroup(activity.getString(R.string.users_nearby), DataGroup.TYPE_HEADER));
                        }

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
