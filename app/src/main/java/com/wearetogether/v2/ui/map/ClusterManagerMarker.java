package com.wearetogether.v2.ui.map;

import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.app.place.Near;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.wearetogether.v2.App;
import com.wearetogether.v2.ui.activities.MainActivity;

import java.util.HashMap;

public class ClusterManagerMarker extends ClusterManager<AbstractMarker> {
    public MainActivity activity;
    public Cluster<AbstractMarker> chosenCluster;
    public AbstractMarker chosenMarker;
    public ClusterManagerMarker(final MainActivity activity, final GoogleMap map) {
        super(activity.getApplicationContext(), map);
        this.activity = activity;
        setRenderer(new OwnIconRendered(activity.getApplicationContext(), map, this));
        setOnClusterItemClickListener(new OnClusterItemClickListener<AbstractMarker>() {
            @Override
            public boolean onClusterItemClick(AbstractMarker item) {
                activity.getViewModel().disableRequestMutableLiveData.setValue(true);
                chosenMarker = item;
//                activity.hideNearby();
//                CurrentTab currentTab = activity.getMainViewModel().currentTabMutableLiveData.getValue();
//                if(currentTab == null) currentTab = CurrentTab.Places;
//                Boolean viewOffline = activity.getMainViewModel().viewOfflineMutableLiveData.getValue();
//                if(viewOffline == null) viewOffline = false;
//                NearPlace.Start(activity, item, currentTab, viewOffline);
                return false;
            }
        });


        setOnClusterClickListener(new OnClusterClickListener<AbstractMarker>() {
            @Override
            public boolean onClusterClick(Cluster<AbstractMarker> cluster) {
                activity.getViewModel().disableRequestMutableLiveData.setValue(true);
                chosenCluster = cluster;
                CurrentTab currentTab = activity.getViewModel().currentTabMutableLiveData.getValue();
                if(currentTab == null) currentTab = CurrentTab.Places;
                MapOptions mapOptions = activity.getViewModel().mapOptionsMutableLiveData.getValue();
                if(mapOptions == null) mapOptions = new MapOptions();
                Near.Start(activity, currentTab, cluster, mapOptions);
                return false;
            }
        });
        getMarkerCollection().setInfoWindowAdapter(new MarkerInfoWindowAdapter(this, activity));

//        getClusterMarkerCollection().setInfoWindowAdapter(new ClusterInfoWindowAdapter(this, activity));

        setOnClusterItemInfoWindowClickListener(new OnClusterItemInfoWindowClickListener<AbstractMarker>() {
            @Override
            public void onClusterItemInfoWindowClick(AbstractMarker marker) {
//                activity.getMainViewModel().disableRequestMutableLiveData.setValue(false);
                HashMap<String, String> map;
                if (marker instanceof PlaceMarker) {
                    PlaceMarker placeMarker = (PlaceMarker) marker;
                    App.GoToPlace(activity, Long.valueOf(placeMarker.getUnic()), MainActivity.class);
                } else if (marker instanceof UserMarker) {
                    UserMarker userMarker = (UserMarker) marker;
                    App.GoToUser(activity, Long.valueOf(userMarker.getUnic()), MainActivity.class);
                }
            }
        });

        setOnClusterInfoWindowLongClickListener(new OnClusterInfoWindowLongClickListener<AbstractMarker>() {
            @Override
            public void onClusterInfoWindowLongClick(Cluster<AbstractMarker> cluster) {
                Float previousZoomLevel = activity.getViewModel().previousZoomLevelMutableLiveData.getValue();
                if(previousZoomLevel == null) {
                    previousZoomLevel = Float.parseFloat("16");
                }
                previousZoomLevel+=2;
                activity.getViewModel().previousZoomLevelMutableLiveData.setValue(previousZoomLevel);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), previousZoomLevel));
            }
        });

        setOnClusterInfoWindowClickListener(new OnClusterInfoWindowClickListener<AbstractMarker>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<AbstractMarker> cluster) {
//                cluster.getPosition();
//                LatLng latLng = cluster.getPosition();
//                List<AbstractMarker> markers = (List<AbstractMarker>) cluster.getItems();
//                List<Long> placeList = new ArrayList<>();
//                List<Long> userList = new ArrayList<>();
//                for(AbstractMarker marker : markers) {
//                    if (marker instanceof PlaceMarker) {
//                        placeList.add(marker.getUnic());
//                    } else if (marker instanceof UserMarker) {
//                        userList.add(marker.getUnic());
//                    }
//                }
                ToastUtils.Short(activity.getApplicationContext(), "onClusterInfoWindowClick");
            }
        });
    }
}
