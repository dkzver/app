package com.wearetogether.v2.ui.activities;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Capture;
import com.wearetogether.v2.app.ClickToPhotoStar;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.app.photo.*;
import com.wearetogether.v2.app.place.Save;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.receivers.LocationReceiver;
import com.wearetogether.v2.services.LocationService;
import com.wearetogether.v2.ucrop.UCrop;
import com.wearetogether.v2.ui.adapters.ImageViewAdapter;
import com.wearetogether.v2.ui.adapters.PhotoAdapter;
import com.wearetogether.v2.ui.components.EditVoiceTextComponent;
import com.wearetogether.v2.ui.dialogs.DialogAttachImage;
import com.wearetogether.v2.ui.dialogs.DialogHintPhoto;
import com.wearetogether.v2.ui.dialogs.DialogPhotoOptions;
import com.wearetogether.v2.ui.dialogs.DialogSelect;
import com.wearetogether.v2.ui.listeners.*;
import com.wearetogether.v2.utils.*;
import com.wearetogether.v2.viewmodel.FormPlaceViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormPlaceActivity extends AppCompatActivity implements EditVoiceListener, PreviewListener, EditorPhotoListener, VoiceListener, AttachImage, CaptureListener {

    public static Intent intentService;
    public static LocationService locationService;

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
    public static boolean bindService;

    private GoogleMap googleMap;
    private GoogleMap googleFullMap;

    private FormPlaceViewModel formPlaceViewModel;
    private RecyclerView recycler_view_item;
    private PhotoAdapter photoAdapter;
    private ProgressBar progress_bar;
    private ViewPager viewPager;
    private ImageViewAdapter imageViewAdapter;
    private View view_more_content;
    private View view_description;
    private View view_map;
    private SupportMapFragment supportMapFragment;
    private SupportMapFragment supportFullMapFragment;
    private ImageView image_view_map;
    private Marker marker;
    private Marker markerFullMap;
    private BottomSheetBehavior<View> bottom_sheet_form_place;
    private FloatingActionButton fab_capture;
    private DialogHintPhoto dialogHintPhoto;
    private View image_view_remove_date_begin;
    private View image_view_remove_date_end;
    private View image_view_remove_time_visit;


    public EditVoiceTextComponent edit_voice_text_title;
    public EditVoiceTextComponent edit_voice_text_description;
    public CheckBox checkbox_anonymous_visit;
    public View view_date_begin;
    public View view_date_end;
    public View view_time_visit;
    public EditText edit_text_count_participant;
    public View view_category;
    public CheckBox checkbox_disable_comments;
    public CheckBox checkbox_only_for_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_place);
        final FormPlaceActivity activity = this;

        intentService = new Intent(getApplicationContext(), LocationService.class);
        broadcastReceiver = new LocationReceiver();
        broadcastReceiver.formPlaceActivity = activity;
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.ACTION_GET_ADDRESS_BY_LOCATION));
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.ACTION_GET_ADDRESSES_BY_STRING));
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());
        if (App.SUser == null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        checkbox_anonymous_visit = findViewById(R.id.checkbox_anonymous_visit);
        checkbox_disable_comments = findViewById(R.id.checkbox_disable_comments);
        checkbox_only_for_friends = findViewById(R.id.checkbox_only_for_friends);


        formPlaceViewModel = ViewModelProviders.of(this).get(FormPlaceViewModel.class);

        setAppTitle("");
        if (supportMapFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, supportMapFragment).commit();
        }
        if (supportFullMapFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportFullMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapFull, supportFullMapFragment).commit();
        }

        findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.view_gallery_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet_form_place.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        fab_capture = (FloatingActionButton) findViewById(R.id.fab_capture);
        fab_capture.setVisibility(View.GONE);
        fab_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAttachImage dialogAttachImage = new DialogAttachImage();
                Bundle bundle = new Bundle();
                bundle.putBoolean("create", true);
                bundle.putBoolean("gallery", true);
                dialogAttachImage.setArguments(bundle);
                dialogAttachImage.show(getSupportFragmentManager(), "attach_image");
            }
        });
        edit_voice_text_title = (EditVoiceTextComponent) findViewById(R.id.edit_voice_text_title);
        edit_voice_text_title.setup(this, getString(R.string.hint_title), Consts.REQUEST_VOICE_TITLE);
        edit_voice_text_description = (EditVoiceTextComponent) findViewById(R.id.edit_voice_text_description);
        edit_voice_text_description.setup(this, getString(R.string.hint_description), Consts.REQUEST_VOICE_DESCRIPTION);

        view_category = findViewById(R.id.view_category);
        view_date_begin = findViewById(R.id.view_date_begin);
        view_date_end = findViewById(R.id.view_date_end);
        view_time_visit = findViewById(R.id.view_time_visit);
        view_date_begin.setOnClickListener(showDatePicker("dateBegin"));
        view_date_end.setOnClickListener(showDatePicker("dateEnd"));
        view_time_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = getCalendarTimeVisit();
                TimePickerDialog timePickerDialog = new TimePickerDialog(FormPlaceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        getViewModel().calendarTimeVisitMutableLiveData.setValue(calendar);
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false);

                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        getViewModel().calendarTimeVisitMutableLiveData.setValue(null);
                    }
                });
                timePickerDialog.show();

            }
        });

        TextView text_view_title = findViewById(R.id.text_view_title);
        ImageView image_view_back = findViewById(R.id.image_view_back);
        View appbar = findViewById(R.id.appbar);

        View view_save = findViewById(R.id.view_save);
        view_save.setVisibility(View.VISIBLE);
        view_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap snapshot = getViewModel().snapshotMutableLiveData.getValue();
                String location = getViewModel().locationMutableLiveData.getValue();
                LatLng latLng = getViewModel().latLngMutableLiveData.getValue();
                if (snapshot == null) {
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                @Override
                                public void onSnapshotReady(Bitmap bitmap) {
                                    getViewModel().snapshotMutableLiveData.setValue(bitmap);
                                    getViewModel().setLocatin(location, 4);
                                    getViewModel().latLngMutableLiveData.setValue(latLng);
                                    Save.Start(activity, false);
                                }
                            });
                        }
                    });
                } else {
                    Save.Start(activity, false);
                }
            }
        });

        View view_gallery = findViewById(R.id.view_gallery);
        view_gallery.setVisibility(View.VISIBLE);
        view_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet_form_place.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        View view_find_location = findViewById(R.id.view_find_location);
        view_find_location.setVisibility(View.GONE);
        view_find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice.Voice(activity, Consts.REQUEST_VOICE_LOCATION);
            }
        });


        view_more_content = findViewById(R.id.view_more_content);
        view_description = findViewById(R.id.view_description);

        formPlaceViewModel.calendarDateBeginMutableLiveData.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                setDate("dateBegin", calendar, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR, getString(R.string.unlimited));
            }
        });
        formPlaceViewModel.calendarDateEndMutableLiveData.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                setDate("dateEnd", calendar, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR, getString(R.string.unlimited));
            }
        });

        formPlaceViewModel.calendarTimeVisitMutableLiveData.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                setDate("timeVisit", calendar, DateUtils.FORMAT_SHOW_TIME, getString(R.string.any));
            }
        });
        formPlaceViewModel.moreMutableLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                view_more_content.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                view_description.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
        view_map = findViewById(R.id.view_map);
        image_view_map = (ImageView) findViewById(R.id.image_view_map);
        formPlaceViewModel.showMapMutableLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                App.HideKeyboard(getApplicationContext(), new View[]{edit_voice_text_description, edit_voice_text_title, edit_text_count_participant});
                float start = aBoolean ? 0.0f : 1.0f;
                float end = aBoolean ? 1.0f : 0.0f;
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
                valueAnimator.setDuration(350);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {


                        float fractionAnim = (float) animation.getAnimatedValue();

                        appbar.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor("#FFFFFF")
                                , Color.parseColor("#75000000")
                                , fractionAnim));

                        view_map.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                        view_find_location.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                        text_view_title.setVisibility(!aBoolean ? View.VISIBLE : View.GONE);
                        view_save.setVisibility(!aBoolean ? View.VISIBLE : View.GONE);
                        view_gallery.setVisibility(!aBoolean ? View.VISIBLE : View.GONE);
                        if (aBoolean) {
                            image_view_back.setBackgroundResource(R.drawable.baseline_arrow_back_white_18dp);
                        } else {
                            image_view_back.setBackgroundResource(R.drawable.baseline_arrow_back_black_18dp);
                        }
                    }
                });
                valueAnimator.start();

                if (aBoolean && googleFullMap != null) {
                    LatLng latLng = getViewModel().latLngMutableLiveData.getValue();
                    if (latLng != null) {
                        setMarkerFullMap(latLng);
                    }
                }
            }
        });
        formPlaceViewModel.showPhotoMutableLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                float start = aBoolean ? 0.0f : 1.0f;
                float end = aBoolean ? 1.0f : 0.0f;
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
                valueAnimator.setDuration(350);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {


                        float fractionAnim = (float) animation.getAnimatedValue();

                        appbar.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor("#FFFFFF")
                                , Color.parseColor("#75000000")
                                , fractionAnim));

                        text_view_title.setTextColor(ColorUtils.blendARGB(Color.parseColor("#75000000")
                                , Color.parseColor("#FFFFFF")
                                , fractionAnim));


                        view_save.setVisibility(!aBoolean ? View.VISIBLE : View.GONE);
                        view_gallery.setVisibility(!aBoolean ? View.VISIBLE : View.GONE);
                        if (aBoolean) {
                            image_view_back.setBackgroundResource(R.drawable.baseline_arrow_back_white_18dp);
                        } else {
                            image_view_back.setBackgroundResource(R.drawable.baseline_arrow_back_black_18dp);
                        }
                    }
                });
                valueAnimator.start();
            }
        });


        edit_voice_text_title = (EditVoiceTextComponent) findViewById(R.id.edit_voice_text_title);
        edit_voice_text_description = (EditVoiceTextComponent) findViewById(R.id.edit_voice_text_description);
        edit_text_count_participant = (EditText) findViewById(R.id.edit_text_count_participant);
        edit_text_count_participant.setOnTouchListener(TouchUtils.touchListener());


        bottom_sheet_form_place = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_form_place));
        bottom_sheet_form_place.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottom_sheet_form_place.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottom_sheet_form_place.setPeekHeight(0);
        bottom_sheet_form_place.setHideable(false);
        bottom_sheet_form_place.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                showFabCapture(!(newState == BottomSheetBehavior.STATE_COLLAPSED));
//                view_gallery.setVisibility(newState == BottomSheetBehavior.STATE_COLLAPSED ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                fab_add_place.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        image_view_remove_date_begin = findViewById(R.id.image_view_remove_date_begin);
        image_view_remove_date_end = findViewById(R.id.image_view_remove_date_end);
        image_view_remove_time_visit = findViewById(R.id.image_view_remove_time_visit);

        image_view_remove_date_begin.setOnClickListener(remove("dateBegin"));
        image_view_remove_date_end.setOnClickListener(remove("dateEnd"));
        image_view_remove_time_visit.setOnClickListener(remove("timeVisit"));

        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
//        recycler_view_item.setVisibility(View.GONE);
        recycler_view_item.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recycler_view_item.setLayoutManager(layoutManager);
        photoAdapter = new PhotoAdapter(this, this);
        recycler_view_item.setAdapter(photoAdapter);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        imageViewAdapter = new ImageViewAdapter(this);
        viewPager.setAdapter(imageViewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setAppTitle(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Long unic = null;
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            if (string_unic != null) {
                unic = Long.parseLong(string_unic);
            }
        }
        final Long finalUnic = unic;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                activity.googleMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                if (savedInstanceState == null) {
                    formPlaceViewModel.bind(activity, finalUnic);
                }
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        formPlaceViewModel.showMapMutableLiveData.setValue(false);
                    }
                });
            }
        });

        supportFullMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                activity.googleFullMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng latLng) {
                        getViewModel().setLocatin(null, 7);
                        getViewModel().latLngMutableLiveData.setValue(latLng);
                        setMarkerFullMap(latLng);
                    }
                });

            }
        });


        formPlaceViewModel.listMediaItemMutableLiveData.observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItemList) {
                if (mediaItemList != null) {
                    photoAdapter.update();
                    imageViewAdapter.update();
                }
            }
        });
        formPlaceViewModel.mutableLiveData.observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                int category_id = 0;
                if (place != null) {
                    String title = place.title;
                    if (title.length() > 14) {
                        title = title.substring(0, 14);
                        title = title + "...";
                    }
                    App.FormPlaceTitle = title;
                    edit_voice_text_title.setText(place.title);
                    edit_voice_text_description.setText(place.description);
                    checkbox_anonymous_visit.setChecked(place.anonymous_visit == 1);
                    view_date_begin.setTag(place.date_begin);
                    view_date_end.setTag(place.date_end);
                    view_time_visit.setTag(place.time_visit);
                    edit_text_count_participant.setText(String.valueOf(place.count_participant));
                    checkbox_disable_comments.setChecked(place.disable_comments == 1);
                    checkbox_only_for_friends.setChecked(place.only_for_friends == 1);
                    category_id = place.category_id;

                } else {
                    App.FormPlaceTitle = getString(R.string.add_place);
                    view_date_begin.setTag(null);
                    view_date_end.setTag(null);
                    view_time_visit.setTag(null);
                    edit_text_count_participant.setText("0");
                    checkbox_anonymous_visit.setChecked(true);
                    checkbox_disable_comments.setChecked(false);
                    checkbox_only_for_friends.setChecked(false);
                }
                view_category.setTag(category_id);
                final String[] array_categories = getViewModel().arrayCategoryMutableLiveData.getValue();
                if (array_categories != null) {
                    ((TextView) findViewById(R.id.text_view_category)).setText(getString(R.string.label_category) + " " + array_categories[category_id]);
                }
                setAppTitle(App.FormPlaceTitle);

            }
        });

        view_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] array_categories = getViewModel().arrayCategoryMutableLiveData.getValue();
                DialogSelect dialogSelect = new DialogSelect();
                dialogSelect.listener = new SelectListener() {
                    @Override
                    public void onSelect(int position) {
                        if (array_categories != null) {
                            ((TextView) findViewById(R.id.text_view_category)).setText(getString(R.string.label_category) + " " + array_categories[position]);
                            view_category.setTag(position);
                        }
                    }
                };
                Bundle bundle = new Bundle();
                bundle.putStringArray("options", array_categories);
                if (v.getTag() != null) {
                    bundle.putInt("selected", (Integer) v.getTag());
                }
                dialogSelect.setArguments(bundle);
                dialogSelect.show(getSupportFragmentManager(), "category");
            }
        });
        formPlaceViewModel.locationMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String location) {
                view_save.setVisibility(location == null ? View.GONE : View.VISIBLE);
            }
        });
        formPlaceViewModel.latLngMutableLiveData.observe(this, new Observer<LatLng>() {//TODO
            @Override
            public void onChanged(LatLng latLng) {
                if (formPlaceViewModel.locationMutableLiveData.getValue() == null) {
                    if (locationService != null) {
                        locationService.getAddress(MapUtils.LatLngToLocation(latLng, getApplicationContext()));
                    }
                }
                if (googleMap != null) {
                    if (marker == null) {
                        marker = googleMap.addMarker(new MarkerOptions().position(latLng));
                    } else {
                        marker.setPosition(latLng);
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                @Override
                                public void onSnapshotReady(Bitmap bitmap) {
                                    getViewModel().snapshotMutableLiveData.setValue(bitmap);
                                    image_view_map.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });
                }
            }
        });
        findViewById(R.id.view_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean more = formPlaceViewModel.moreMutableLiveData.getValue();
                if (more == null) {
                    more = false;
                }
                more = !more;
                formPlaceViewModel.moreMutableLiveData.setValue(more);
            }
        });
        findViewById(R.id.view_eanble_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheet();
                formPlaceViewModel.showMapMutableLiveData.setValue(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
        if (locationService != null) {
            if (bindService && connection != null) {
                unbindService(connection);
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
        if (bindService && locationService != null) {
            stopService(intentService);
            locationService.stopSelf();
            unbindService(connection);
            bindService = false;
        }
    }

//    @Override
//    protected void onDestroy() {
//        GeocoderBroadcastReceiverFindAddressByLocation.Destroy(this, broadcastReceiverFindAddressByLocation);
//        GeocoderBroadcastReceiverFindAddressesByString.Destroy(this, broadcastReceiverFindAddressesByString);
//        super.onDestroy();
//    }

    private View.OnClickListener remove(final String key) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (key) {
                    case "dateBegin":
                        getViewModel().calendarDateBeginMutableLiveData.setValue(null);
                        break;
                    case "dateEnd":
                        getViewModel().calendarDateEndMutableLiveData.setValue(null);
                        break;
                    case "timeVisit":
                        getViewModel().calendarTimeVisitMutableLiveData.setValue(null);
                        break;
                }
            }
        };
    }

    private void showFabCapture(boolean isShow) {
        fab_capture.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private View.OnClickListener showDatePicker(String key) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = getCalendarDate(key);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FormPlaceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Integer error = null;
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (key.equals("dateEnd")) {
                            Calendar calendarDateBegin = getCalendarDate("dateBegin");
                            if (calendar.getTime().getTime() < calendarDateBegin.getTime().getTime()) {
                                error = 1;
                            }
                        } else if (key.equals("dateBegin")) {
                            Calendar calendarDateEnd = getCalendarDate("dateEnd");
                            if (calendar.getTime().getTime() > calendarDateEnd.getTime().getTime()) {
                                error = 2;
                            }
                        }
                        if (error != null) {
                            switch (error) {
                                default:
                                    ToastUtils.Short(getApplicationContext(), "Error calendar");
                                    break;
                            }
                        } else {
                            if (key.equals("dateBegin")) {
                                getViewModel().calendarDateBeginMutableLiveData.setValue(calendar);
                            } else {
                                getViewModel().calendarDateEndMutableLiveData.setValue(calendar);
                            }
                        }
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (key.equals("dateBegin")) {
                            getViewModel().calendarDateBeginMutableLiveData.setValue(null);
                        } else {
                            getViewModel().calendarDateEndMutableLiveData.setValue(null);
                        }
                    }
                });
                datePickerDialog.show();
            }
        };
    }

    private void setDate(String key, Calendar calendar, int flag, String def) {
        String text = null;
        if (calendar != null) {
            text = DateUtils.formatDateTime(getApplicationContext(), calendar.getTimeInMillis(), flag);
        } else {
            text = def;
        }
        switch (key) {
            case "timeVisit":
                text = getString(R.string.label_time_visit) + " " + text;
                ((TextView) findViewById(R.id.text_view_time_visit)).setText(text);
                view_time_visit.setTag(calendar);
                image_view_remove_time_visit.setVisibility(calendar == null ? View.GONE : View.VISIBLE);
                break;
            case "dateBegin":
                text = getString(R.string.label_date_begin) + " " + text;
                ((TextView) findViewById(R.id.text_view_date_begin)).setText(text);
                view_date_begin.setTag(calendar);
                image_view_remove_date_begin.setVisibility(calendar == null ? View.GONE : View.VISIBLE);
                break;
            case "dateEnd":
                text = getString(R.string.label_date_end) + " " + text;
                ((TextView) findViewById(R.id.text_view_date_end)).setText(text);
                view_date_end.setTag(calendar);
                image_view_remove_date_end.setVisibility(calendar == null ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private void setMarkerFullMap(LatLng latLng) {
        if (markerFullMap == null) {
            markerFullMap = googleFullMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_place_black_18dp)));
        } else {
            markerFullMap.setPosition(latLng);
        }
        googleFullMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public void onBackPressed() {
        Boolean showMap = formPlaceViewModel.showMapMutableLiveData.getValue();
        if (showMap == null) {
            showMap = false;
        }
        if (showMap) {
            formPlaceViewModel.showMapMutableLiveData.setValue(false);
        } else {
            if (viewPager.getVisibility() == View.VISIBLE) {
                getViewModel().showPhotoMutableLiveData.setValue(false);
                setAppTitle(App.FormPlaceTitle);
                Integer stateGallery = getViewModel().stateGalleryMutableLiveData.getValue();
                togglePreview(false);
                if (stateGallery != null) {
                    bottom_sheet_form_place.setState(stateGallery);
                }
            } else {
                if (!hideBottomSheet()) {
                    Save.Start(this, true);
                }
            }
        }
    }

    private boolean hideBottomSheet() {
        getViewModel().stateGalleryMutableLiveData.setValue(bottom_sheet_form_place.getState());
        if (bottom_sheet_form_place.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            bottom_sheet_form_place.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    public void back() {
        showProgressBar(false);
        super.onBackPressed();
//        Class<?> cls = ProfileActivity.class;
//        if (getIntent() != null) {
//            Serializable clsParam = getIntent().getSerializableExtra("cls");
//            if(clsParam != null) {
//                cls = (Class<?>) clsParam;
//            }
//        }
//        Intent intent = new Intent(getApplicationContext(), cls);
//        Place place = getFormPlaceViewModel().mutableLiveData.getValue();
//        if (place != null) {
//            intent.putExtra(Consts.USER_UNIC, String.valueOf(place.user_unic));
//        }
//        startActivity(intent);
    }

    private void setAppTitle(int position) {
        ((TextView) findViewById(R.id.text_view_title)).setText(position + " " + getList().size());
    }

    private void setAppTitle(String title) {
        ((TextView) findViewById(R.id.text_view_title)).setText(title);
    }

    public FormPlaceViewModel getViewModel() {
        if (formPlaceViewModel == null) {
            formPlaceViewModel = ViewModelProviders.of(this).get(FormPlaceViewModel.class);
        }
        return formPlaceViewModel;
    }

    private Calendar getCalendarTimeVisit() {
        Calendar calendar = getViewModel().calendarTimeVisitMutableLiveData.getValue();
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        return calendar;
    }

    private Calendar getCalendarDate(String key) {
        if (key.equals("dateBegin")) {
            Calendar calendar = getViewModel().calendarDateBeginMutableLiveData.getValue();
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            return calendar;
        } else {
            Calendar calendar = getViewModel().calendarDateEndMutableLiveData.getValue();
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            return calendar;
        }
    }

    @Override
    public void showPhoto(long unic, int position, String original, String icon) {
        getViewModel().showPhotoMutableLiveData.setValue(true);
        viewPager.setCurrentItem(position);
        setAppTitle(position + 1);
        togglePreview(true);
    }

    private void togglePreview(boolean isShow) {
        hideBottomSheet();
        if (isShow) {
            viewPager.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCapture(String original, String small, String icon) {
        if (App.SUser != null) {
            Long unic = getViewModel().unicMutableLiveData.getValue();
            if (unic == null || unic == 0) {
                unic = getViewModel().tempUnicMutableLiveData.getValue();
                if (unic == null || unic == 0) {
                    unic = Calendar.getInstance().getTimeInMillis();
                    getViewModel().tempUnicMutableLiveData.setValue(unic);
                }
            }
            Insert.Start(this, original, small, icon, unic, Consts.TYPE_PLACE, 0);
        }
    }

    @Override
    public void addPhoto(MediaItem mediaItem) {
        try {
            Uri sourceUri = Uri.fromFile(new File(mediaItem.original));
            Uri destinationUri = Uri.fromFile(new File(mediaItem.original));
            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            options.setActiveWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            options.setToolbarTitle(getString(R.string.title_crop));
            options.withAspectRatio(9, 16);
            int width = DimensionUtils.Transform(720, getApplicationContext());
            int height = DimensionUtils.Transform(1280, getApplicationContext());
            options.withMaxResultSize(width, height);
            options.setUnic(mediaItem.unic);
            UCrop.of(sourceUri, destinationUri)
                    .withOptions(options)
                    .start(this, Consts.REQUEST_CROP_ORIGINAL);
        } catch (Exception exception) {
            exception.printStackTrace();
            ToastUtils.Short(getApplicationContext(), exception.getMessage());
        }
    }

    private int cell = 0;

    @Override
    public List<MediaItem> getList() {
        cell++;
        if (getViewModel() == null) return new ArrayList<>();
        if (getViewModel().listMediaItemMutableLiveData == null) return new ArrayList<>();
        if (getViewModel().listMediaItemMutableLiveData.getValue() == null) return new ArrayList<>();
        List<MediaItem> list = getViewModel().listMediaItemMutableLiveData.getValue();
        System.out.println(list.size() + " get list " + cell);
        return list;
    }

    @Override
    public void showProgressBar(boolean isShow) {
        System.out.println("show progress " + isShow);
        progress_bar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void OnSetVoice(int code, String spokenText) {
        if (code == Consts.REQUEST_VOICE_TITLE) {
            edit_voice_text_title.setText(App.CapitalizeString(spokenText));
        } else if (code == Consts.REQUEST_VOICE_DESCRIPTION) {
            edit_voice_text_description.setText(App.CapitalizeString(spokenText));
        } else if (code == Consts.REQUEST_VOICE_LOCATION) {
            if (locationService != null) {
                locationService.getAddresses(spokenText, 5);
            }
        }
    }

    @Override
    public void createPhoto() {
        Media.TakeCamera(this);
    }

    @Override
    public void selectInGallery() {
        Media.TakeGallery(this);
    }

    @Override
    public void selectInAlbum() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        List<String> newPermissionList = new ArrayList<>();
        boolean denied = false;
        for (int x = 0; x < grantResults.length; x++) {
            if (grantResults[x] == PackageManager.PERMISSION_DENIED) {
                newPermissionList.add(permissions[x]);
                denied = true;
            }
        }
        if (denied) {
            String[] newPermissions = new String[newPermissionList.size()];
            newPermissionList.toArray(newPermissions);
            ActivityCompat.requestPermissions(this, newPermissions, requestCode);
        } else {
            switch (requestCode) {
                case Consts.PERMISSION_READ_EXTERNAL_STORAGE_CAMERA:
                    Media.StartCamera(this);
                    break;
                case Consts.PERMISSION_READ_EXTERNAL_STORAGE_GALLERY:
                    Media.StartGallery(this);
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String selectedImagePath = "";
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        Long unic = null;
        Integer position = null;
        Uri resultUri = null;
        Voice.Result(this, requestCode, resultCode, data, Consts.REQUEST_VOICE_TITLE);
        Voice.Result(this, requestCode, resultCode, data, Consts.REQUEST_VOICE_DESCRIPTION);
        Voice.Result(this, requestCode, resultCode, data, Consts.REQUEST_VOICE_LOCATION);
        if (dialogHintPhoto != null) {
            Voice.Result(dialogHintPhoto, requestCode, resultCode, data, Consts.REQUEST_VOICE_HINT);
        }
        Capture.Gallery(this, requestCode, resultCode, data);
        Capture.Camera(this, requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Consts.REQUEST_CROP_ORIGINAL:
                    unic = UCrop.getUnic(data);
                    position = UCrop.getPosition(data);
                    resultUri = UCrop.getOutput(data);
                    System.out.println("REQUEST_CROP_ORIGINAL");
                    System.out.println("unic " + unic);
                    System.out.println("resultUri " + resultUri);
                    if (unic != null && resultUri != null) {
                        File file = new File(resultUri.getPath());
                        System.out.println("file " + file);
                        if (file.exists()) {
                            CropOriginalEnd cropOriginalEnd = new CropOriginalEnd();
                            cropOriginalEnd.execute(this, file, unic);
                        }
                    }
                    break;
                case Consts.REQUEST_CROP_ICON:
                    unic = UCrop.getUnic(data);
                    long item_unic = UCrop.getItemUnic(data);
                    position = UCrop.getPosition(data);
                    resultUri = UCrop.getOutput(data);
                    int type = UCrop.getType(data);
                    if (unic != null && resultUri != null) {
                        File file = new File(resultUri.getPath());
                        if (file.exists()) {
                            CropIconEnd cropIconEnd = new CropIconEnd();
                            cropIconEnd.execute(this, file, unic, position, Consts.TYPE_PLACE);
                        }
                    }
                    break;
//                case Consts.REQUEST_GALLERY_CAPTURE:
//                    showProgressBar(true);
//                    try {
//                        Uri selectedImage = data.getData();
//                        String[] filePath = {};
//                        Cursor c = getContentResolver().query(selectedImage, new String[]{}, null, null, null);
//                        if (c == null) {
//                            throw new Exception("Error file");
//                        }
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(MediaStore.Images.Media.DATA);
//                        selectedImagePath = c.getString(columnIndex);
//                        Media.File = new File(selectedImagePath);
//                        if (!Media.File.exists()) {
//                            throw new Exception("Error file");
//                        }
//                        Capture.Catpure(null, BitmapFactory.decodeFile(selectedImagePath), this);
//                        c.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToastUtils.Short(getApplicationContext(), e.getMessage());
//                        showProgressBar(false);
//                    }
//
//                    break;
//
//                case Consts.REQUEST_CAMERA_CAPTURE:
//                    showProgressBar(true);
//                    try {
//                        if (Media.File == null) {
//                            throw new Exception("Error file");
//                        }
//                        selectedImagePath = Media.File.getAbsolutePath();
////                        Media.CropImage(this, Media.File, 1, 1, true);
//                        Capture.Catpure(Media.File, BitmapFactory.decodeFile(Media.File.getAbsolutePath()), this);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToastUtils.Short(getApplicationContext(), e.getMessage());
//                        showProgressBar(false);
//                    }
//                    break;
            }

        }

        System.out.println(selectedImagePath);
        Log.e("IMAGE Path", selectedImagePath.toString());
    }

    public void attachPhoto(MediaItem mediaItem) {
        System.out.println("attachPhoto");
        List<MediaItem> mediaItems = getList();
        mediaItems.add(mediaItem);
        getViewModel().listMediaItemMutableLiveData.setValue(mediaItems);
        if (photoAdapter != null) {
            photoAdapter.update();
        }
        if (imageViewAdapter != null) {
            imageViewAdapter.update();
        }
        showProgressBar(false);
        System.out.println("attachPhoto");
    }

    @Override
    public void showMenu(Long unic, int position, String hint, int star, String original, String icon) {
        Long item_unic = getViewModel().unicMutableLiveData.getValue();
        if (item_unic == null || item_unic == 0) {
            item_unic = getViewModel().tempUnicMutableLiveData.getValue();
            if (item_unic == null) {
                item_unic = Calendar.getInstance().getTimeInMillis();
                getViewModel().tempUnicMutableLiveData.setValue(item_unic);
            }
        }

        final FormPlaceActivity activity = this;
        DialogPhotoOptions dialogUserPhotoOptions = new DialogPhotoOptions();
        Long finalItem_unic = item_unic;
        dialogUserPhotoOptions.setListener(new PhotoOptions() {
            @Override
            public void clickToPhotoStar(long unic, int position) {
                ClickToPhotoStar clickToPhotoStar = new ClickToPhotoStar(Consts.TYPE_PLACE, activity, finalItem_unic, unic, position);
                ClickToPhotoStar.original = original;
                ClickToPhotoStar.icon = icon;
                clickToPhotoStar.execute();
            }

            @Override
            public void clickToRemovePhoto(long unic, int position) {
                Remove.Start(position, finalItem_unic, unic, Consts.TYPE_PLACE, activity, new RemovePhotoListener() {//TODO
                    @Override
                    public void removePhoto(int position, boolean isStar) {
                        List<MediaItem> mediaItems = getViewModel().listMediaItemMutableLiveData.getValue();
                        if (mediaItems != null) {
                            mediaItems.remove(position);
                            getViewModel().listMediaItemMutableLiveData.setValue(mediaItems);
                            if (photoAdapter != null) {
                                photoAdapter.update();
                            }
                            if (imageViewAdapter != null) {
                                imageViewAdapter.update();
                            }
                        }
                    }
                });
            }

            @Override
            public void clickToChangeHintPhoto(final long unic, final int position, final String hint) {
                if (dialogHintPhoto == null) {
                    dialogHintPhoto = new DialogHintPhoto();
                    dialogHintPhoto.setListener(new ChangeHintListener() {
                        @Override
                        public void onChangeHint(String hint, int position, long unic) {
                            Hint.Start(activity, hint, position, unic, new SetHintListener() {
                                @Override
                                public void setHint(int position, String hint) {
                                    List<MediaItem> mediaItems = getViewModel().listMediaItemMutableLiveData.getValue();
                                    if (mediaItems != null) {
                                        for (int x = 0; x < mediaItems.size(); x++) {
                                            if (x == position) mediaItems.get(x).hint = hint;
                                            else continue;
                                        }
                                        getViewModel().listMediaItemMutableLiveData.setValue(mediaItems);
                                    }
                                    photoAdapter.update();
                                    imageViewAdapter.update();
                                }
                            });
                        }
                    });
                }
                Bundle bundle = new Bundle();
                bundle.putLong(Consts.UNIC, unic);
                bundle.putInt(Consts.POSITION, position);
                bundle.putString(Consts.HINT, hint);
                dialogHintPhoto.setArguments(bundle);
                if (!dialogHintPhoto.isAdded()) {
                    dialogHintPhoto.show(getSupportFragmentManager(), "dialogHintPhoto");
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(Consts.HINT, hint);
        bundle.putInt(Consts.POSITION, position);
        bundle.putLong(Consts.UNIC, unic);
        dialogUserPhotoOptions.setArguments(bundle);
        dialogUserPhotoOptions.show(getSupportFragmentManager(), "dialogUserPhotoOptions");
    }

    public void onAttachAddress(ArrayList<Address> addresses) {
        ArrayAdapter arrayAdapter = null;
        if (addresses.size() > 0) {
            String[] options = new String[addresses.size()];
            for (int i = 0; i < addresses.size(); i++) {
                options[i] = addresses.get(i).getAddressLine(0);
            }
            arrayAdapter = new ArrayAdapter<String>(
                    FormPlaceActivity.this,
                    android.R.layout.simple_list_item_1,
                    options
            );

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(FormPlaceActivity.this);
        builder.setTitle(getString(R.string.dialog_title_find_results));
        if (addresses.size() > 0) {
            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    Address address = addresses.get(pos);
                    if (address != null) {
                        LatLng latLng = MapUtils.AddressToLatLng(address);
                        String location = address.getAddressLine(0);
                        getViewModel().setLocatin(location, 6);
                        getViewModel().latLngMutableLiveData.setValue(latLng);
                        setMarkerFullMap(latLng);
                    }
                    dialog.dismiss();
                }
            });
        } else {
            builder.setMessage(getString(R.string.not_find_results));
        }
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showError(int code) {
        switch (code) {
            case Save.ERROR_LOCATION:
                ToastUtils.Short(getApplicationContext(), "ERROR_LOCATION");
                break;
            case Save.ERROR_LATLNG:
                ToastUtils.Short(getApplicationContext(), "ERROR_LATLNG");
                break;
            case Save.ERROR_TITLE:
                ToastUtils.Short(getApplicationContext(), "ERROR_TITLE");
                break;
            case Save.ERROR_SNAPSHOT:
                ToastUtils.Short(getApplicationContext(), "ERROR_SNAPSHOT");
                break;
        }
    }

    public void updateIcon(MediaItem mediaItem, int position) {
        List<MediaItem> mediaItems = getViewModel().listMediaItemMutableLiveData.getValue();
        if (mediaItems != null) {
            mediaItems.get(position).star = 1;
            mediaItems.get(position).icon = mediaItem.icon;
        }
        getViewModel().listMediaItemMutableLiveData.setValue(mediaItems);
        if (photoAdapter != null) {
            photoAdapter.update();
        }
        if (imageViewAdapter != null) {
            imageViewAdapter.update();
        }
        showProgressBar(false);
    }

    @Override
    public boolean hasVoice() {
        Boolean showMap = getViewModel().showMapMutableLiveData.getValue();
        if (showMap == null) return true;
        return !showMap;
    }
}
