package com.wearetogether.v2.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.GPS;
import com.wearetogether.v2.app.Google;
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.app.WIFI;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.app.place.Show;
import com.wearetogether.v2.database.model.NotificationItem;
import com.wearetogether.v2.receivers.LocationReceiver;
import com.wearetogether.v2.services.LocationService;
import com.wearetogether.v2.ui.BottomSheetNearby;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.components.AccessViewComponent;
import com.wearetogether.v2.ui.components.LoginViewComponent;
import com.wearetogether.v2.ui.components.SearchViewComponent;
import com.wearetogether.v2.ui.components.TabViewComponent;
import com.wearetogether.v2.ui.map.*;
import com.wearetogether.v2.utils.MapUtils;
import com.wearetogether.v2.utils.NotificationUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.wearetogether.v2.viewmodel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vk.api.sdk.VK;

import java.util.*;

public class MainActivity extends OptionActivity {
    private static final int OPTION_COMPLAIN = 1;
    private static final int OPTION_VIEW_PLACE = 2;
    private static final int OPTION_VIEW_USER = 3;
    private static final int OPTION_COMMENTS_PLACE = 4;
    private static final int OPTION_COMMENTS_USER = 5;
    private static final int OPTION_ADD_TO_FAVORITE = 6;
    private MainViewModel viewModel;
    public GoogleMap googleMap;
    public ClusterManagerMarker clusterManager;
    public SearchViewComponent component_view_serach;
    public LoginViewComponent component_view_login;
    public TabViewComponent component_view_tab;
    public AccessViewComponent component_view_access;
    public BottomSheetBehavior<View> bottom_sheet_nearby;
    public BottomSheetBehavior<View> bottom_sheet_serach;
    public View view_bottom_sheet_nearby;
    public View view_bottom_sheet_serach;
    private RecyclerView recycler_view_nearby;
    private RecyclerView recycler_view_serach;
    public AdapterGroup adapterGroupNearby;
    public AdapterGroup adapterGroupSerach;
    public NotificationUtils notificationUtils;
    private int countBackPressed = 0;
    private Timer mTimer;
    private CountBackPressedTimerTask countBackPressedTimerTask;
    private DatabaseReference database;

    public static Intent intentService;
    public static LocationService locationService;
    public static boolean bindService;

    private static ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("LocationService")) {
                locationService = ((LocationService.LocationServiceBinder) service).getService();
                bindService = true;
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            System.out.println("onServiceDisconnected");
            if (className.getClassName().equals("BackgroundLocationService")) {
                bindService = false;
            }
        }
    };
    private LocationReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity activity = this;

        intentService = new Intent(getApplicationContext(), LocationService.class);
        broadcastReceiver = new LocationReceiver();
        broadcastReceiver.mainActivity = activity;
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.ACTION_UPDATE_LOCATION));
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.ACTION_GET_LOCATION));

        Intent intent = getIntent();
        NotificationUtils.Start(this, intent);

        App.SUser = PreferenceUtils.GetUser(getApplicationContext());

        component_view_serach = findViewById(R.id.component_view_serach);
        component_view_serach.setup(this);
        component_view_login = findViewById(R.id.component_view_login);
        component_view_access = findViewById(R.id.component_view_access);
        component_view_login.setup(this, MainActivity.this);
        component_view_login.setVisibility(View.GONE);
        component_view_tab = findViewById(R.id.component_view_tab);
        component_view_tab.setup(this, MainActivity.this);


        FirebaseCrashlytics.getInstance();
        FirebaseCrashlytics.getInstance().log("Error main");
        BottomSheetNearby.Setup(activity);

        recycler_view_nearby = (RecyclerView) findViewById(R.id.recycler_view_nearby);
        recycler_view_nearby.setHasFixedSize(true);
        recycler_view_nearby.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroupNearby = new AdapterGroup(this, MainActivity.this, MainActivity.class);
        recycler_view_nearby.setAdapter(adapterGroupNearby);

        recycler_view_serach = (RecyclerView) findViewById(R.id.recycler_view_serach);
        recycler_view_serach.setHasFixedSize(true);
        recycler_view_serach.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroupSerach = new AdapterGroup(this, MainActivity.this, MainActivity.class);
        recycler_view_serach.setAdapter(adapterGroupSerach);

        MapsInitializer.initialize(getApplicationContext());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setup(this);


        FirebaseMessaging.getInstance().getToken()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        init(savedInstanceState);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        init(savedInstanceState);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            PreferenceUtils.SaveCloudMessageToken(getApplicationContext(), token);
                        }
                        init(savedInstanceState);
                    }
                });

        viewModelObserve(this);


    }

    private void viewModelObserve(MainActivity activity) {
        viewModel.currentTabMutableLiveData.observe(this, new Observer<CurrentTab>() {
            @Override
            public void onChanged(CurrentTab currentTab) {
                if(currentTab == CurrentTab.Places || currentTab == CurrentTab.Users) {
                    onUpdateNotificationItems(null);
                }
            }
        });
        viewModel.mapOptionsMutableLiveData.observe(this, new Observer<MapOptions>() {
            @Override
            public void onChanged(MapOptions options) {
                if (options != null) {
                    ToastUtils.Short(getApplicationContext(), "Options");
                    onUpdateNotificationItems(null);
                }
            }
        });
    }

    private void init(Bundle savedInstanceState) {
        viewModel.setupMap(this, MainActivity.this);
    }

    public boolean hideNearby() {
        if (bottom_sheet_nearby.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            bottom_sheet_nearby.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (component_view_login != null && component_view_login.getVisibility() == View.VISIBLE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.continue_without_authorization));
            builder.setPositiveButton(getString(R.string.button_continue), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    component_view_login.cellContinue();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            if (hideNearby()) {
                checkExit();
            }
        }
    }

    public MainViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        }
        return viewModel;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean disableRequest = getViewModel().disableRequestMutableLiveData.getValue();
        LatLngBounds bounds = getViewModel().currentCameraBoundsMutableLiveData.getValue();
        Location user = getViewModel().locationMutableLiveData.getValue();
        LatLng camera = PreferenceUtils.GetCameraPosition(getApplicationContext());
        boolean isSaveLog = PreferenceUtils.GetLog(getApplicationContext());
        if (disableRequest != null && isSaveLog) {
            if (disableRequest) {
                getViewModel().disableRequestMutableLiveData.setValue(false);
                getViewModel().workCamera(this, bounds, user, MapUtils.LatLngToLocation(camera, getApplicationContext()));
            }
        }
        System.out.println("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
        if (locationService != null) {
            if (bindService && connection != null) {
//                unbindService(connection);
                bindService = false;
            }
            stopService(intentService);
            locationService.stopSelf();
        }
        try {
            if (intentService != null) {
                startService(intentService);
                if (!bindService && connection != null) {
                    bindService(intentService, connection, Context.BIND_AUTO_CREATE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationService.Stop(this);
        if (bindService && locationService != null) {
            stopService(intentService);
            locationService.stopSelf();
            unbindService(connection);
            bindService = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Consts.REQUEST_PERMISSIONS_LOCATION || requestCode == Consts.REQUEST_PERMISSIONS_STORAGE) {
            boolean access = false;
            for (int x = 0; x < grantResults.length; x++) {
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) {
                    access = true;
                }
            }

            if (requestCode == Consts.REQUEST_PERMISSIONS_LOCATION) {
                getViewModel().accessLocationMutableLiveData.setValue(access);
            } else {
//                getViewModel().accessStorageMutableLiveData.setValue(access);
            }
            if (access) {
                if (requestCode == Consts.REQUEST_PERMISSIONS_LOCATION) {
                    if (component_view_access.view_pager.getCurrentItem() == 0) {
                        component_view_access.button_access_agree.setVisibility(View.GONE);
                    }
                } else {
                    if (component_view_access.view_pager.getCurrentItem() == 1) {
                        component_view_access.button_access_agree.setVisibility(View.GONE);
                    }
                }
                Boolean location = getViewModel().accessLocationMutableLiveData.getValue();
                if (location == null) {
                    location = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                }
                getViewModel().setAccessMutableLiveData.setValue(location);
//                Boolean storage = getViewModel().accessStorageMutableLiveData.getValue();
//                if (storage == null) {
//                    boolean WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//                    boolean READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//                    storage = WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE;
//                }
//                if (location/* && !storage*/) {
//                    component_view_access.view_pager.setCurrentItem(1, true);
//                } else if (!location/* && storage*/) {
//                    component_view_access.view_pager.setCurrentItem(0, true);
//                }
//                if (location/* && storage*/) {
//                    component_view_access.setVisibility(View.GONE);
//                    getViewModel().setAccessMutableLiveData.setValue(true);
//                }
            }
        } else if (requestCode == Consts.REQUEST_CODE_LOCATION) {
            getViewModel().attemptRunServer(this, MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Voice.Result(component_view_serach, requestCode, resultCode, data, Consts.REQUEST_VOICE_SEARCH);
        GPS.Result(this, MainActivity.this, requestCode, resultCode);
        WIFI.Result(this, MainActivity.this, requestCode);
        Google.Result(this, requestCode, resultCode, data);
        component_view_login.callbackManager.onActivityResult(requestCode, resultCode, data);
        if (!VK.onActivityResult(requestCode, resultCode, data, component_view_login)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showButtonFindUser(boolean isShow) {

    }

    public void onTabSelected(int tab) {
        getViewModel().currentTabMutableLiveData.setValue(tab == 0 ? CurrentTab.Places : CurrentTab.Users);
    }

    public void onUpdateNotificationItems(List<NotificationItem> notificationItems) {
        boolean enableSoundNotification = PreferenceUtils.getEnableSoundNotification(getApplicationContext());
        if (Show.UpdateApp) {
            NotificationItem notification = new NotificationItem();
            notification.id = -1;
            notification.title = "Update app";
            notification.content = Consts.URL_MARKET_PLACE;
            notification.status = NotificationItem.STATUS_NOT_READ;
            notification.item_unic = 0;
            notification.type = -1;
            notification.action = "-1";
            notification.channelId = NotificationUtils.CHANNEL_ID_BASE;
            App.NotificationUtils.showNotification(notification, getApplicationContext(), enableSoundNotification);
        }
        if (notificationItems != null && App.SUser != null) {
            for (NotificationItem notificationItem : notificationItems) {
                App.NotificationUtils.showNotification(notificationItem, getApplicationContext(), enableSoundNotification);
            }
        }
        showProgressBar(false);
        if (clusterManager != null) {
            clusterManager.clearItems();
            googleMap.clear();
            clusterManager.cluster();
            CurrentTab currentTab = getViewModel().currentTabMutableLiveData.getValue();
            if (currentTab == null) {
                currentTab = CurrentTab.Places;
            }
            if (currentTab == CurrentTab.Places) {
                List<MarkerItem> markerItems = getViewModel().markerItemPlaceListsMutableLiveData.getValue();
                if(markerItems == null) markerItems = new ArrayList<>();
                for (int i = 0; i < markerItems.size(); i++) {
                    addMarkerPlace(markerItems.get(i));
                }
            } else if (currentTab == CurrentTab.Users) {
                List<MarkerItem> markerItems = getViewModel().markerItemUserListsMutableLiveData.getValue();
                if(markerItems == null) markerItems = new ArrayList<>();
                for (int i = 0; i < markerItems.size(); i++) {
                    addMarkerUser(markerItems.get(i));
                }
            }
            clusterManager.cluster();
        }
    }

    private void addMarkerPlace(final MarkerItem markerItem) {
        PlaceMarker placeMarker = new PlaceMarker(markerItem, this);
        clusterManager.addItem(placeMarker);
    }

    private void addMarkerUser(final MarkerItem markerItem) {
        UserMarker userMarker = new UserMarker(markerItem, this);
        clusterManager.addItem(userMarker);
    }

//    public void onShowNear(Integer currentTab) {
//        if (currentTab == 0 || currentTab == 1) {
//            int px = DimensionUtils.Transform(104, getApplicationContext());
//            bottom_sheet_nearby.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
//            List<DataGroup> posts = new ArrayList<>();
//            if (currentTab == 0) {
//                if (App.nearPlaces == null) return;
//                if (App.nearPlaces.size() == 0) return;
//                posts.add(new DataGroup(getString(R.string.places_nearby), DataGroup.TYPE_HEADER));
//                if (App.nearPlaces.size() > 0) {
//                    for (int x = 0; x < App.nearPlaces.size(); x++) {
////                    place.position = x;
//                        posts.add(new DataGroup().Place(App.nearPlaces.get(x)));
//                    }
//                } else {
//                    posts.add(new DataGroup(getString(R.string.not_places), DataGroup.TYPE_TEXT));
//                }
//            } else {
//                if (App.nearUsers == null) return;
//                if (App.nearUsers.size() == 0) return;
//                posts.add(new DataGroup(getString(R.string.users_nearby), DataGroup.TYPE_HEADER));
//                if (App.nearUsers.size() > 0) {
//                    MarkerItem markerItem;
//                    for (int x = 0; x < App.nearUsers.size(); x++) {
//                        markerItem = App.nearUsers.get(x);
//                        if (markerItem.show_in_map == 1) {
//                            Boolean viewOffline = mainViewModel.viewOfflineMutableLiveData.getValue();
//                            if (viewOffline == null) viewOffline = true;
//                            if (viewOffline || markerItem.is_online == 1) {
////                                user.position = x;
//                                posts.add(new DataGroup().User(markerItem));
//                            }
//                        }
//                    }
//                } else {
//                    posts.add(new DataGroup(getString(R.string.not_users), DataGroup.TYPE_TEXT));
//                }
//            }
//            if (posts.size() > 0) {
//                adapterGroup.update(posts);
//            }
//        }
//    }

    public void showProgressBar(boolean isShow) {
        findViewById(R.id.view_progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void removeItemFromAdapter(int position) {
        adapterGroupNearby.remove(position);
    }

    public void showLoginView() {
        if (component_view_login != null) {
            ViewGroup parent = findViewById(R.id.root);

            Transition transition = new Slide(Gravity.BOTTOM);
            transition.setDuration(600);
            transition.addTarget(component_view_login);

            TransitionManager.beginDelayedTransition(parent, transition);
            component_view_login.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoginView() {
        if (component_view_login != null) {
            ViewGroup parent = findViewById(R.id.root);

            Transition transition = new Slide(Gravity.BOTTOM);
            transition.setDuration(600);
            transition.addTarget(component_view_login);

            TransitionManager.beginDelayedTransition(parent, transition);
            component_view_login.setVisibility(View.GONE);
//            component_view_login.restoreDefaultFocus();
        }
    }

    private void checkExit() {
        countBackPressed++;
        if (countBackPressed > 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
                System.exit(0);
            }
        } else {
            ToastUtils.Long(getApplicationContext(), getString(R.string.click_to_exit));
            if (mTimer != null) {
                mTimer.cancel();
            }
            mTimer = new Timer();
            countBackPressedTimerTask = new CountBackPressedTimerTask(this);
            mTimer.schedule(countBackPressedTimerTask, 2000);
        }
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
        if (key == OPTION_VIEW_PLACE) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            App.GoToPlace(this, place_unic, cls);
        } else if (key == OPTION_VIEW_USER) {
            long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
            App.GoToUser(this, user_unic, cls);
        } else if (key == OPTION_COMMENTS_PLACE) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            App.GoToReviews(this, place_unic, cls, Consts.TYPE_PLACE);
        } else if (key == OPTION_COMMENTS_USER) {
            long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
            App.GoToRoom(this, null, user_unic, cls);
        }
    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        int type = bundle.containsKey("type") ? bundle.getInt("type", 0) : 0;
        int disable_comments = bundle.containsKey("disable_comments") ? bundle.getInt("disable_comments") : 0;
        List<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.option_complain_place), OPTION_COMPLAIN));
        if (type == Consts.TYPE_PLACE) {
            options.add(new Option(getString(R.string.option_view_place), OPTION_VIEW_PLACE));
            if (disable_comments == 0) {
                options.add(new Option(getString(R.string.option_commentes), OPTION_COMMENTS_PLACE));
            }
        } else if (type == Consts.TYPE_USER) {
            if (disable_comments == 0) {
                options.add(new Option(getString(R.string.option_view_user), OPTION_VIEW_USER));
            }
            options.add(new Option(getString(R.string.option_write_message), OPTION_COMMENTS_USER));
        }
//        options.add(new Option(getString(R.string.option_add_to_favorite), OPTION_ADD_TO_FAVORITE));
        return options;
    }

    class CountBackPressedTimerTask extends TimerTask {

        private MainActivity activity;

        public CountBackPressedTimerTask(MainActivity activity) {

            this.activity = activity;
        }

        @Override
        public void run() {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.countBackPressed = 0;
                }
            });
        }
    }
}
