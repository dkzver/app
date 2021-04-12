package com.wearetogether.v2.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
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
import com.wearetogether.v2.app.user.Friends;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.adapters.ImageViewAdapter;
import com.wearetogether.v2.ui.listeners.FriendListener;
import com.wearetogether.v2.ui.listeners.PreviewListener;
import com.wearetogether.v2.utils.ConnectUtils;
import com.wearetogether.v2.utils.MapUtils;
import com.wearetogether.v2.viewmodel.UserViewModel;
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

public class UserActivity extends OptionActivity implements PreviewListener {
    private static final int OPTION_GO_TO_COMMENTES = 1;
    private static final int OPTION_COMPLAIN = 2;
    private static final int OPTION_REJECT_FRIEND = 3;
    private static final int OPTION_ACCEPT_FRIEND = 4;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;
    private UserViewModel userViewModel;
    private ViewPager viewPager;
    private ImageViewAdapter imageViewAdapter;
    private View progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        adapterGroup = new AdapterGroup(this, UserActivity.this, UserActivity.class);
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
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            System.out.println("user unic " + string_unic);
            if (string_unic != null && savedInstanceState == null) {                ;
                userViewModel.bind(this, Long.parseLong(string_unic), ConnectUtils.IsOnline(getApplicationContext()));
            }
        }
        userViewModel.arrayCategoriesMutableLiveData.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] array) {
                User user = userViewModel.mutableLiveData.getValue();
                if (user == null) {
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
                            LatLng latLng = MapUtils.LocationToLatLng(user.latitude, user.longitude);
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
                                            App.UserName = user.name;
                                            List<DataGroup> posts = userViewModel.listMutableLiveData.getValue();
                                            if(posts != null) {
                                                DataGroup dataGroup = posts.get(0);
                                                dataGroup.bitmapMap = bitmap;
                                                posts.set(0, dataGroup);
                                                adapterGroup.update(posts, userViewModel.arrayCategoriesMutableLiveData.getValue());
                                                imageViewAdapter.update();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });


                }
            }
        });
        userViewModel.showPhotoMutableLiveData.observe(this, new Observer<Boolean>() {
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
            setAppTitle(App.UserName);
            togglePreview(false);
        } else {
            super.onBackPressed();
        }
    }

    public UserViewModel getUserViewModel() {
        if (userViewModel == null) {
            userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        }
        return userViewModel;
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
        getUserViewModel().showPhotoMutableLiveData.setValue(isShow);
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
        List<MediaItem> mediaItemList = getUserViewModel().mediaItemMutableLiveData.getValue();
        if (mediaItemList == null) return new ArrayList<>();
        return mediaItemList;
    }

    @Override
    public void showProgressBar(boolean isShow) {
        progress_bar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void friend(long user_unic) {
        if (App.SUser != null) {
            Integer friend = getUserViewModel().friendMutableLiveData.getValue();
            if (friend == null) friend = 0;
            if (friend == Friend.SEND_REQUEST_FRIEND) {
                showDialogFriend(user_unic, getString(R.string.cancel_request), Friend.CANCEL_FRIEND);
            } else if (friend == Friend.FRIEND) {
                showDialogFriend(user_unic, getString(R.string.remove_friend), Friend.REMOVE_FRIEND);
            } else if (friend == Friend.REQUEST_FRIEND) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", friend);
                bundle.putLong("friend", friend);
                bundle.putLong("user_unic", user_unic);
                showMenu(bundle, UserActivity.class);
            } else {
                showDialogFriend(user_unic, getString(R.string.add_friend), Friend.SEND_FRIEND);
            }
        }
    }

    private void showDialogFriend(long user_unic, String title, int type) {
        final UserActivity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setTitle(title);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                hideOptions();
                Friends.Start(activity, user_unic, type);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
        long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
        if (key == OPTION_GO_TO_COMMENTES) {
            App.GoToReviews(this, user_unic, cls, Consts.TYPE_USER);
        } else if(key == OPTION_REJECT_FRIEND) {
            showDialogFriend(user_unic, getString(R.string.reject_request), Friend.REJECT_FRIEND);
        } else if(key == OPTION_ACCEPT_FRIEND) {
            showDialogFriend(user_unic, getString(R.string.accept_friend), Friend.ACCEPT_FRIEND);
        }
    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        List<Option> options = new ArrayList<>();
        int type = bundle.containsKey("type") ? bundle.getInt("type", 0) : 0;
        if(type == Friend.REQUEST_FRIEND) {
            options.add(new Option(getString(R.string.cancel_friend), OPTION_REJECT_FRIEND));
            options.add(new Option(getString(R.string.accept_friend), OPTION_ACCEPT_FRIEND));
        } else {
            if (App.SUser != null) {
                long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
                if (user_unic != Long.parseLong(App.SUser.unic)) {
                    options.add(new Option(getString(R.string.option_comments), OPTION_GO_TO_COMMENTES));
                    options.add(new Option(getString(R.string.option_complain_user), OPTION_COMPLAIN));
                }
            }
        }
        return options.stream()
                .sorted(Comparator.comparing(Option::getPosition))
                .collect(Collectors.toList());
    }

    public void changeFriendState(int type) {
        if(type == Friend.SEND_FRIEND) {
            getUserViewModel().friendMutableLiveData.setValue(Friend.SEND_REQUEST_FRIEND);
        } else if(type == Friend.ACCEPT_FRIEND) {
            getUserViewModel().friendMutableLiveData.setValue(Friend.FRIEND);
        } else if(type == Friend.REJECT_FRIEND) {
            getUserViewModel().friendMutableLiveData.setValue(0);
        } else if(type == Friend.CANCEL_FRIEND) {
            getUserViewModel().friendMutableLiveData.setValue(0);
        }
        if(adapterGroup != null) {
            adapterGroup.updateHeader();
        }
    }

    public String getTextFriendButton() {
        String text = getString(R.string.add_friend);
        Integer type = getUserViewModel().friendMutableLiveData.getValue();
        if(type != null) {
            if(type == Friend.SEND_REQUEST_FRIEND) {
                text = getString(R.string.cancel_request);
            } else if(type == Friend.REQUEST_FRIEND) {
                text = getString(R.string.request_friend);
            } else if(type == Friend.FRIEND) {
                text = getString(R.string.remove_friend);
            }
        }
        return text;
    }
}
