package com.wearetogether.v2.ui.map;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.database.model.Friend;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;

public class ClusterInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private ClusterManagerMarker clusterManagerMarker;
    private MainActivity activity;

    public static int Count = 0;

    public ClusterInfoWindowAdapter(ClusterManagerMarker clusterManagerMarker, MainActivity activity) {
        this.clusterManagerMarker = clusterManagerMarker;
        this.activity = activity;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (clusterManagerMarker.chosenCluster != null) {
Count = 0;
            View v = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cluster_view_window, null);

            TextView info = (TextView) v.findViewById(R.id.clusterTitle);

            CurrentTab currentTab = activity.getViewModel().currentTabMutableLiveData.getValue();
            if (currentTab == null) {
                currentTab = CurrentTab.Places;
            }
            String unic;
            if (currentTab == CurrentTab.Places) {
                info.setText("");
                for (AbstractMarker abstractMarker : clusterManagerMarker.chosenCluster.getItems()) {
                    if (abstractMarker instanceof PlaceMarker) {
                        unic = abstractMarker.getUser_unic();
                        Count += 1;
//                        if (abstractMarker.is_remove.equals("0")) {
//                            if (abstractMarker.only_for_friends.equals("1")) {
//                                if (abstractMarker.friend == Friend.FRIEND) {
//                                    Count += 1;
//                                }
//                            } else {
//                                Count += 1;
//                            }
//                        }
                    }
                }
                info.setText(String.format(activity.getApplicationContext().getString(R.string.format_count_users), Count));


            } else if (currentTab == CurrentTab.Users) {
                String user_unic = App.SUser == null ? "0" : App.SUser.unic;
                info.setText("");
                for (AbstractMarker abstractMarker : clusterManagerMarker.chosenCluster.getItems()) {
                    if (abstractMarker instanceof UserMarker) {
                        unic = abstractMarker.getUser_unic();
                        if(!unic.equals(user_unic)) {
                            Count += 1;
//                            if (abstractMarker.show_in_map.equals("1")) {
//                                Count += 1;
//                            } else if (abstractMarker.show_in_map.equals("2")) {
//                                if (abstractMarker.friend == Friend.FRIEND) {
//                                    Count += 1;
//                                }
//                            }
                        }
                    }
                }
                info.setText(String.format(activity.getApplicationContext().getString(R.string.format_count_places), Count));
            }

            return v;
        }
        return null;
    }
}
