package com.wearetogether.v2.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.adapters.ImageViewAdapter;
import com.wearetogether.v2.ui.listeners.*;
import com.wearetogether.v2.utils.MapUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.wearetogether.v2.viewmodel.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceActivity extends OptionActivity implements PreviewListener {
    private static final int OPTION_COMMENTS = 1;
    private static final int OPTION_EDIT_PLACE = 2;
    private static final int OPTION_REMOVE_PLACE = 3;
    private static final int OPTION_COMPLAIN = 5;
    private static final int OPTION_ADD_TO_FAVORITE = 6;
    private static final int OPTION_VISITED = 7;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;
    private PlaceViewModel viewModel;
    private ViewPager viewPager;
    private ImageViewAdapter imageViewAdapter;
    private View progress_bar;
    public static boolean Limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        adapterGroup = new AdapterGroup(this, PlaceActivity.this, PlaceActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        progress_bar = findViewById(R.id.progress_bar);
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
        viewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            if (string_unic != null && savedInstanceState == null) {
                viewModel.bind(this, Long.parseLong(string_unic));
            }
        }
        viewModel.arrayCategoriesMutableLiveData.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] array) {
                Place place = viewModel.mutableLiveData.getValue();
                if (place == null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    if (supportMapFragment == null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        supportMapFragment = SupportMapFragment.newInstance();
                        fragmentTransaction.replace(R.id.map, supportMapFragment).commit();
                    }
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            googleMap.getUiSettings().setRotateGesturesEnabled(false);
                            LatLng latLng = MapUtils.LocationToLatLng(place.latitude, place.longitude);
                            googleMap.addMarker(new MarkerOptions().position(latLng));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                        @Override
                                        public void onSnapshotReady(Bitmap bitmap) {
                                            findViewById(R.id.view_map).setVisibility(View.GONE);
                                            progress_bar.setVisibility(View.GONE);
                                            App.PlaceTitle = place.title;
                                            List<DataGroup> posts = new ArrayList<>();
                                            posts.add(new DataGroup().HeaderPlace(bitmap,
                                                    place, App.SUser == null ? 0 : Long.parseLong(App.SUser.unic)));
                                            posts.add(new DataGroup().Gallery());
                                            adapterGroup.update(posts, viewModel.arrayCategoriesMutableLiveData.getValue());
                                            imageViewAdapter.update();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }

            }
        });
        viewModel.showPhotoMutableLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateAppbar(aBoolean);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getVisibility() == View.VISIBLE) {
            setAppTitle(App.PlaceTitle);
            togglePreview(false);
        } else {
            super.onBackPressed();
        }
    }

    public PlaceViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        }
        return viewModel;
    }

    private void updateAppbar(Boolean aBoolean) {
        if (adapterGroup != null) {
            if (adapterGroup.items.size() > 0) {
                DataGroup dataGroup = adapterGroup.items.get(0);
                dataGroup.colorAppbar = aBoolean;
                adapterGroup.items.set(0, dataGroup);
                adapterGroup.notifyItemChanged(0);
            }
        }
    }

    private void setAppTitle(int position) {
        String title = position + " " + getList().size();
        setAppTitle(title);
    }

    private void setAppTitle(String title) {
        if (adapterGroup != null) {
            if (adapterGroup.items.size() > 0) {
                DataGroup dataGroup = adapterGroup.items.get(0);
                dataGroup.title = title;
                adapterGroup.items.set(0, dataGroup);
                adapterGroup.notifyItemChanged(0);
            }
        }
    }

    private void togglePreview(boolean isShow) {
        getViewModel().showPhotoMutableLiveData.setValue(isShow);
        if (isShow) {
            viewPager.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPhoto(long unic, int position, String original, String icon) {
        viewPager.setCurrentItem(position);
        setAppTitle(position + 1);
        togglePreview(true);
    }

    @Override
    public List<MediaItem> getList() {
        Place place = getViewModel().mutableLiveData.getValue();
        if (place == null) return new ArrayList<>();
        return place.mediaItemList;
    }

    @Override
    public void showProgressBar(boolean isShow) {
        progress_bar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
        long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
        int position = bundle.containsKey("position") ? bundle.getInt("position", 0) : 0;
        if (key == OPTION_COMMENTS) {
            System.out.println("place_unic " + place_unic);
            App.GoToReviews(this, place_unic, cls, Consts.TYPE_PLACE);
        } else if (key == OPTION_VISITED) {
            App.GoToVisited(this, place_unic, cls);
        } else if (key == OPTION_EDIT_PLACE) {
            App.GoToFormPlace(this, place_unic, cls);
        } else if (key == OPTION_REMOVE_PLACE) {
            int is_remove = bundle.containsKey("is_remove") ? bundle.getInt("is_remove", 0) : 0;
            App.ShowDialogRemovePlace(this, PlaceActivity.this, is_remove, place_unic, position);
        } else if (key == OPTION_ADD_TO_FAVORITE) {
            ToastUtils.Short(getApplicationContext(), "Shared");
        }
    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        List<Option> options = new ArrayList<>();
        int disable_comments = bundle.containsKey("disable_comments") ? bundle.getInt("disable_comments") : 0;
        if(disable_comments == 0) {
            options.add(new Option(getString(R.string.option_commentes), OPTION_COMMENTS));
        }
        int is_remove = bundle.containsKey("is_remove") ? bundle.getInt("is_remove", 0) : 0;
        if (App.SUser != null) {
            boolean is_edit = false;
            Place place = getViewModel().mutableLiveData.getValue();
            if (place != null) {
                if (place.user_unic == Long.parseLong(App.SUser.unic)) {
                    is_edit = true;
                }
            }
            if (is_edit) {
                options.add(new Option(getString(R.string.option_visits), OPTION_VISITED));
                options.add(new Option(getString(R.string.option_edit_place), OPTION_EDIT_PLACE));
                options.add(new Option(is_remove == 0 ? getString(R.string.option_remove_place) : getString(R.string.option_restore_place), OPTION_REMOVE_PLACE));
            } else {
                options.add(new Option(getString(R.string.option_complain_place), OPTION_COMPLAIN));
                options.add(new Option(getString(R.string.option_shared), OPTION_ADD_TO_FAVORITE));
            }
        }
        return options.stream()
                .sorted(Comparator.comparing(Option::getPosition))
                .collect(Collectors.toList());
    }
}
