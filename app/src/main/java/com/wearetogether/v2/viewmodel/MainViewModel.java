package com.wearetogether.v2.viewmodel;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.app.user.UpdateLocation;
import com.wearetogether.v2.database.model.Category;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.Status;
import com.wearetogether.v2.receivers.LocationReceiver;
import com.wearetogether.v2.ui.map.MarkerItem;
import com.wearetogether.v2.utils.*;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.place.Show;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.map.ClusterManagerMarker;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainViewModel extends ViewModel {
    public MutableLiveData<List<Object>> listMarkerMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> sizeIconMarkerWindowMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<CurrentTab> currentTabMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MapOptions> mapOptionsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> disableRequestMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isNotSendRequestMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<LatLngBounds> currentCameraBoundsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> previousZoomLevelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> snapMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isZoomingMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> firstShowMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> addressMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> countryMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> cityMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<MarkerItem>> markerItemPlaceListsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<MarkerItem>> markerItemUserListsMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> accessLocationMutableLiveData = new MutableLiveData<>();
//    public MutableLiveData<Boolean> accessStorageMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> setAccessMutableLiveData = new MutableLiveData<>();

    private ResolvableApiException resolvableApiException;

    public void setup(MainActivity activity) {
        snapMutableLiveData.setValue(Long.valueOf("0"));
        previousZoomLevelMutableLiveData.setValue(Float.valueOf("0"));
        firstShowMutableLiveData.setValue(false);
        isNotSendRequestMutableLiveData.setValue(false);
        listMarkerMutableLiveData.setValue(new ArrayList<>());
        sizeIconMarkerWindowMutableLiveData.setValue(DimensionUtils.Transform(50, activity.getApplicationContext()));


        boolean location = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        setAccessMutableLiveData.setValue(location);
//        boolean WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//        boolean READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//        boolean storage = WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE;
//        accessStorageMutableLiveData.setValue(storage);
//        if (location/* || storage*/) {
//            setAccessMutableLiveData.setValue(true);
//        } else {
//            setAccessMutableLiveData.setValue(false);
//        }
    }

    public void setupMap(MainActivity activity, Context context) {
        SupportMapFragment supportMapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(mapReadyCallback(activity, context));
    }

    private OnMapReadyCallback mapReadyCallback(MainActivity activity, Context context) {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                activity.googleMap = googleMap;
                activity.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                activity.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        hideNearby(activity);
                    }
                });
                activity.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        hideNearby(activity);
                    }
                });
                activity.clusterManager = new ClusterManagerMarker(activity, activity.googleMap);
                activity.googleMap.setInfoWindowAdapter(activity.clusterManager.getMarkerManager());
                activity.googleMap.setOnCameraIdleListener(activity.clusterManager);
                activity.googleMap.setOnMarkerClickListener(activity.clusterManager);
                activity.googleMap.setOnInfoWindowClickListener(activity.clusterManager);
                activity.googleMap.getUiSettings().setRotateGesturesEnabled(false);
                activity.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        disableRequestMutableLiveData.setValue(false);
                        activity.hideNearby();
                    }
                });
                setAccessMutableLiveData.observe(activity, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean != null) {
                            Boolean location = accessLocationMutableLiveData.getValue();
                            if (location == null) {
                                location = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                            }
//                            Boolean storage = accessStorageMutableLiveData.getValue();
//                            if (storage == null) {
//                                boolean WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//                                boolean READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//                                storage = WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE;
//                            }
                            if(location) {
                                checkEnableGps(activity, context);
                            } else {
                                ActivityCompat.requestPermissions(activity, new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                }, Consts.REQUEST_PERMISSIONS_LOCATION);
                            }
//                            if (location/* && storage*/) {
//                                activity.component_view_access.setVisibility(View.GONE);
//                                checkEnableGps(activity, context);
//                            } else {
//                                activity.component_view_access.setVisibility(View.VISIBLE);
//                                activity.component_view_access.setup(activity);
//                                if (location/* && !storage*/) {
//                                    activity.component_view_access.view_pager.setCurrentItem(1, true);
//                                } else if (!location/* && storage*/) {
//                                    activity.component_view_access.view_pager.setCurrentItem(0, true);
//                                }
//                            }
                        }
                    }
                });
            }
        };
    }

    private void hideNearby(MainActivity activity) {
        if (activity != null) {
            activity.hideNearby();
        }
    }

    private void checkEnableGps(MainActivity activity, Context context) {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                checkNetwork(activity, context);
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    resolvableApiException = (ResolvableApiException) e;
                    enableGPS(activity);
                }
            }
        });
    }

    private void enableGPS(final MainActivity activity) {
        if (resolvableApiException != null) {
            try {
                resolvableApiException.startResolutionForResult(activity,
                        Consts.REQUEST_CHECK_SETTINGS_GPS);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    public void showMessageEnableGps(final MainActivity activity, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_enable_gps);
        builder.setPositiveButton(R.string.button_enable_gps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableGPS(activity);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void checkAccessPermissions(MainActivity activity) {
        Boolean location = accessLocationMutableLiveData.getValue();
        if (location == null) {
            location = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
//        Boolean storage = accessStorageMutableLiveData.getValue();
//        if (storage == null) {
//            boolean WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//            boolean READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//            storage = WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE;
//        }
        if (!location/* || !storage*/) {
            setAccessMutableLiveData.setValue(false);
        } else {
            setAccessMutableLiveData.setValue(true);
        }
    }

    public void checkNetwork(MainActivity activity, Context context) {
        if (!ConnectUtils.IsOnline(activity.getApplicationContext())) {
            showMessageEnableNetwork(activity, context);
        } else {
            checkAccessPermissionsLocation(activity, context);
        }
    }

    public void checkAccessPermissionsLocation(MainActivity activity, Context context) {
        boolean access = PermissionUtils.CheckPermission(activity.getApplicationContext(), PermissionUtils.NEED_PERMISSIONS);
        if (!access) {
            boolean should = PermissionUtils.ShouldPermissionRationale(activity, PermissionUtils.NEED_PERMISSIONS);
            if (should) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Enable fine location");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PermissionUtils.RequestPermissions(activity, PermissionUtils.NEED_PERMISSIONS, Consts.REQUEST_CODE_LOCATION);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            } else {
                PermissionUtils.RequestPermissions(activity, PermissionUtils.NEED_PERMISSIONS, Consts.REQUEST_CODE_LOCATION);
            }
        } else {
            attemptRunServer(activity, context);
        }
    }

    public void attemptRunServer(MainActivity activity, Context context) {
        Location location = null;
        String string_address = null;
        String string_country = null;
        String string_city = null;
        LatLng cameraPosition = PreferenceUtils.GetCameraPosition(activity.getApplicationContext());
        if (cameraPosition != null && App.SUser != null) {
            location = MapUtils.LatLngToLocation(cameraPosition, activity.getApplicationContext());
            string_address = App.SUser.location;
            string_country = App.SUser.country;
            string_city = App.SUser.city;
        }

        if(location != null && string_address != null && string_country != null && string_city != null) {
            MainActivity.locationService.getLocation(true);
            setLocationRunServiceFindLocation(activity, context, location, string_address, string_country, string_city);
        } else if (MainActivity.locationService != null) {
            MainActivity.locationService.getLocation(false);
        }
    }

    public void showMessageEnableNetwork(final MainActivity activity, final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_enable_network);
        builder.setPositiveButton(R.string.button_enable_wifi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager != null) {
                        if (!wifiManager.isWifiEnabled()) {
                            wifiManager.setWifiEnabled(true);
                            checkNetwork(activity, context);
                        } else {
                            wifiManager.setWifiEnabled(false);
                        }
                    }
                } else {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    activity.startActivityForResult(panelIntent, Consts.REQUEST_ENABLE_WIFI);
                }
            }
        });
        builder.setNeutralButton(R.string.button_enable_network, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                checkNetwork(activity, context);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setLocationRunServiceFindLocation(MainActivity activity, Context context,
                                                  Location location, String string_address, String string_country, String string_city) {

        if (location != null) locationMutableLiveData.setValue(location);
        if (string_address != null) addressMutableLiveData.setValue(string_address);
        if (string_country != null) countryMutableLiveData.setValue(string_country);
        if (string_city != null) cityMutableLiveData.setValue(string_city);
        if (location != null) {
            PreferenceUtils.SaveUserLocation(activity.getApplicationContext(), location.getLatitude(), location.getLongitude(), string_address, string_country, string_city);
        } else {
            PreferenceUtils.SaveUserLocation(activity.getApplicationContext(), 0, 0, string_address, string_country, string_city);
        }
        attachMap(activity, context, string_address, string_country, string_city);
    }

    public void attachMap(MainActivity activity, Context context, String address, String country, String city) {
        LatLng cameraPosition = PreferenceUtils.GetCameraPosition(activity.getApplicationContext());
        Location location = locationMutableLiveData.getValue();
        if (cameraPosition == null) {
            if (location != null)
                activity.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapUtils.LocationToLatLng(location), 16));
        } else {
            float zoom = PreferenceUtils.GetCameraZoom(activity.getApplicationContext());
            activity.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, zoom));
        }
        activity.googleMap.setOnCameraChangeListener(cameraChangeListener(activity, context));
        if (App.SUser == null) {
            activity.googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    activity.showLoginView();
                    activity.component_view_tab.update(activity.getApplicationContext());
                }
            });
        }

    }

    private GoogleMap.OnCameraChangeListener cameraChangeListener(MainActivity activity, Context context) {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Location userLocation = locationMutableLiveData.getValue();
                if (userLocation != null && activity.clusterManager.chosenMarker == null) {
                    activity.clusterManager.cluster();
                    Float previousZoomLevel = previousZoomLevelMutableLiveData.getValue();
                    if (previousZoomLevel != null) {
                        if (previousZoomLevel != cameraPosition.zoom) {
                            isZoomingMutableLiveData.setValue(true);
                        }
                    }

                    previousZoomLevelMutableLiveData.setValue(cameraPosition.zoom);
                    LatLngBounds bounds = activity.googleMap.getProjection().getVisibleRegion().latLngBounds;
                    PreferenceUtils.SaveCameraPosition(activity.getApplicationContext(), cameraPosition.target);
                    PreferenceUtils.SaveCameraZoom(activity.getApplicationContext(), cameraPosition.zoom);
                    LatLngBounds currentCameraBounds = currentCameraBoundsMutableLiveData.getValue();
                    if (currentCameraBounds != null) {
                        if (currentCameraBounds.northeast.latitude == bounds.northeast.latitude
                                && currentCameraBounds.northeast.longitude == bounds.northeast.longitude
                                && currentCameraBounds.southwest.latitude == bounds.southwest.latitude
                                && currentCameraBounds.southwest.longitude == bounds.southwest.longitude) {
                            return;
                        }

                        final long snap = System.currentTimeMillis();
                        Long lastCallMs = snapMutableLiveData.getValue();
                        if (lastCallMs == null) {
                            snapMutableLiveData.setValue(snap);
                            return;
                        } else {
                            if (lastCallMs + Consts.CAMERA_MOVE_REACT_THRESHOLD_MS > snap) {
                                snapMutableLiveData.setValue(snap);
                                return;
                            }
                        }
                        snapMutableLiveData.setValue(snap);
                    }
                    workCamera(activity, bounds, userLocation, MapUtils.LatLngToLocation(cameraPosition.target, activity.getApplicationContext()));
                    currentCameraBoundsMutableLiveData.setValue(bounds);
                } else {
                    activity.clusterManager.chosenMarker = null;
                }

            }
        };
    }

    public void workCamera(MainActivity activity, LatLngBounds bounds, Location user, Location camera) {
        float distance = user.distanceTo(camera);
        activity.showButtonFindUser(distance >= 600);


        Boolean disableRequest = activity.getViewModel().disableRequestMutableLiveData.getValue();
        System.out.println("disableRequest " + disableRequest);
        if (disableRequest == null) disableRequest = false;
        System.out.println("disableRequest " + disableRequest);
        if (!disableRequest) {
            updateDataFromServer(activity, bounds, user, camera);
        }

    }

    private void updateDataFromServer(MainActivity activity, LatLngBounds bounds, Location user, Location camera) {
        if (ConnectUtils.IsOnline(activity.getApplicationContext())) {
            boolean isSaveLog = PreferenceUtils.GetLog(activity.getApplicationContext());
            System.out.println("isSaveLog " + isSaveLog);
            System.out.println("IsRun " + App.IsRun);
            if (isSaveLog) {
                runUpdateProgress(activity, bounds, user, camera);
                App.IsRun = true;
            } else {
                System.out.println("IsRun " + App.IsRun);
                if (!App.IsRun) {
                    runUpdateProgress(activity, bounds, user, camera);
                    App.IsRun = true;
                } else {
                    System.out.println("IsRun " + App.IsRun);
                    Boolean firstShow = firstShowMutableLiveData.getValue();
                    System.out.println("firstShow " + firstShow);
                    if (firstShow == null) {
                        firstShow = false;
                    }
                    if (!firstShow) {
                        runFirstProcesss(activity, bounds, user, camera);
                    } else {
                        runUpdateProgress(activity, bounds, user, camera);
                    }
                }
            }
        }
    }

    private boolean equalsStringValue(String a, String b) {
        return a.equals(b);
    }

    private boolean equalsLocation(Location camera) {
        if (equalsStringValue(App.SUser.latitude, String.valueOf(camera.getLatitude())) ||
                equalsStringValue(App.SUser.longitude, String.valueOf(camera.getLongitude())))
            return false;
        return true;
    }

    private boolean hasUpdateLocation(MainActivity activity, Location camera) {
        if (camera == null) return false;
        if (PreferenceUtils.GetRegister(activity.getApplicationContext())) return false;
        return equalsLocation(camera);
    }

    private void runFirstProcesss(MainActivity activity, LatLngBounds bounds, Location user, Location camera) {
        if (App.SUser != null) {
            if (hasUpdateLocation(activity, camera)) {
                PreferenceUtils.SetRegister(activity.getApplicationContext(), false);
                updateFirstMap(activity);
            } else {
                updateFirstMap(activity);
            }
        } else {
            updateFirstMap(activity);
        }
    }

    private void updateFirstMap(MainActivity activity) {
        activity.showProgressBar(false);
        firstShowMutableLiveData.setValue(true);
    }

    private void runUpdateProgress(MainActivity activity, LatLngBounds bounds, Location user, Location camera) {
        activity.showProgressBar(true);
        if (App.SUser != null) {
            if (hasUpdateLocation(activity, camera)) {
                PreferenceUtils.SetRegister(activity.getApplicationContext(), false);
                runShow(activity, bounds, user, camera);
            } else {
                runShow(activity, bounds, user, camera);
            }
        } else {
            runShow(activity, bounds, user, camera);
        }
    }

    private void runShow(MainActivity activity, LatLngBounds bounds, Location user, Location camera) {
        System.out.println("Run show");
        if (ConnectUtils.IsOnline(activity.getApplicationContext())) {
            Show.Start(activity, bounds, camera);
        }
    }
}
