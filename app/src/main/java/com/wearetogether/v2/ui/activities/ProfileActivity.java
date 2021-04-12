package com.wearetogether.v2.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Capture;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.app.data.DataToServer;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.app.photo.Insert;
import com.wearetogether.v2.app.place.Remove;
import com.wearetogether.v2.app.place.Show;
import com.wearetogether.v2.app.user.Logout;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ucrop.UCrop;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.listeners.AttachImage;
import com.wearetogether.v2.ui.listeners.CaptureListener;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.wearetogether.v2.viewmodel.ProfileViewModel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends OptionActivity implements AttachImage, CaptureListener, Observer<List<DataGroup>> {

    private static final int OPTION_GO_TO_FRIENDS = 1;
    private static final int OPTION_GO_TO_BACKED = 2;
    private static final int OPTION_VIEW_PLACE = 3;
    private static final int OPTION_REMOVE_PLACE = 4;
    private static final int OPTION_COMMENTS = 5;
    private static final int OPTION_GO_TO_VISITS = 6;
    private static final int OPTION_GO_TO_ALBUM = 7;
    private static final int OPTION_GO_TO_INTERESTS = 8;
    private static final int OPTION_ADD_PLACE = 9;
    private ProfileViewModel profileViewModel;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());
        final FragmentActivity activity = this;


        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroup = new AdapterGroup(this, ProfileActivity.this, ProfileActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        recycler_view_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println(dy);
            }
        });

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        if (savedInstanceState == null) {// && App.SUser != null
            profileViewModel.bind(this);
        }
        profileViewModel.mutableLiveData.observe(this, this);
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.IsUpdate) {
            profileViewModel.bind(this);
            App.IsUpdate = false;
        }
    }

    public ProfileViewModel getViewModel() {
        if (profileViewModel == null) {
            profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        }
        return profileViewModel;
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
        Intent intent = new Intent(getApplicationContext(), AlbumActivity.class);
        intent.putExtra("action", "select");
        startActivity(intent);
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
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);

        Capture.Gallery(this, requestCode, resultCode, data);
        Capture.Camera(this, requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Consts.REQUEST_CROP_ICON) {
            long unic = UCrop.getUnic(data);
            int position = UCrop.getPosition(data);
            Uri resultUri = UCrop.getOutput(data);
            System.out.println("resultUri");
            System.out.println(resultUri);
            System.out.println("resultUri");
            if (App.SUser != null && resultUri != null) {
                File file = new File(resultUri.getPath());
                long user_unic = Long.parseLong(App.SUser.unic);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final MediaItem mediaItem = App.Database.daoMediaItem().get(unic);
                        if (mediaItem != null) {
                            App.Database.daoMediaItem().RestStar(mediaItem.item_unic);

                            mediaItem.icon = file.getAbsolutePath();
                            mediaItem.star = 1;
                            App.Database.daoMediaItem().update(mediaItem);

                            ItemLog log = new ItemLog();
                            log.unic = Calendar.getInstance().getTimeInMillis();
                            log.action = Consts.LOG_ACTION_INSERT_PHOTO;
                            log.item_unic = mediaItem.unic;
                            App.Database.daoLog().insert(log);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PreferenceUtils.SaveLog(getApplicationContext(), true);
                                    PreferenceUtils.SaveUserAvatar(getApplicationContext(), mediaItem.icon);
                                    App.SUser = PreferenceUtils.GetUser(getApplicationContext());
                                    /*
                            int size = DimensionUtils.Transform(Consts.SIZE_ICON, getApplicationContext());
                            Bitmap roundedBitmap = App.GetRoundedCornerBitmap(Bitmap.createScaledBitmap(bitmap, size, size, false));
                            ((ImageView) findViewById(R.id.image_view_avatar)).setImageBitmap(roundedBitmap);*/
                                    updateProfileIcon(mediaItem.icon);
                                }
                            });
                        }
                    }
                }).start();
            }

        }
    }

    @Override
    public void onCapture(String original, String small, String icon) {
        if (App.SUser != null) {
            Insert.Start(this, original, small, icon, Long.parseLong(App.SUser.unic), Consts.TYPE_USER, 1);
        }
    }

    @Override
    public void addPhoto(MediaItem mediaItem) {
        Uri sourceUri = Uri.fromFile(new File(mediaItem.original));
        Uri destinationUri = Uri.fromFile(new File(mediaItem.icon));
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setToolbarTitle(getString(R.string.title_crop));
        options.withAspectRatio(1, 1);
        options.setUnic(mediaItem.unic);
        long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
        options.setItemUnic(user_unic);
        options.setPosition(0);
        options.setType(Consts.TYPE_USER);
        options.setPosition(0);
        int size = DimensionUtils.Transform(Consts.SIZE_ICON, getApplicationContext());
        options.withMaxResultSize(size, size);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this, Consts.REQUEST_CROP_ICON);
    }

    @Override
    public List<MediaItem> getList() {
        return null;
    }

    @Override
    public void showProgressBar(boolean isShow) {
        findViewById(R.id.view_progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void updateProfileIcon(String path) {//TODO
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = FileUtils.GetBitmap(path);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            adapterGroup.updateProfileIcon(bitmap);
                        } else {
                            ToastUtils.Short(getApplicationContext(), "error bitmap");
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void removeItemFromAdapter(int position) {
        adapterGroup.remove(position);
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
        if (key == OPTION_ADD_PLACE) {
            App.GoToFormPlace(this, null, SettingsActivity.class);
        } else if (key == OPTION_GO_TO_ALBUM) {
            startActivity(new Intent(getApplicationContext(), AlbumActivity.class));
        } else if (key == OPTION_GO_TO_INTERESTS) {
            startActivity(new Intent(getApplicationContext(), InterestsActivity.class));
        } else if (key == OPTION_GO_TO_FRIENDS) {
            startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
        } else if (key == OPTION_GO_TO_BACKED) {
            startActivity(new Intent(getApplicationContext(), BackedActivity.class));
        } else if (key == OPTION_GO_TO_VISITS) {
            startActivity(new Intent(getApplicationContext(), VisitsActivity.class));
        } else if (key == OPTION_VIEW_PLACE) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            App.GoToPlace(this, place_unic, ProfileActivity.class);
        } else if (key == OPTION_REMOVE_PLACE) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            int position = bundle.containsKey("position") ? bundle.getInt("position", 0) : 0;
            String title = bundle.containsKey("title") ? bundle.getString("title") : "";
            App.ShowDialogRemovePlace(this, ProfileActivity.this, 0, place_unic, position);
        } else if (key == OPTION_COMMENTS) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            App.GoToReviews(this, place_unic, cls, Consts.TYPE_PLACE);
        }
    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        List<Option> options = new ArrayList<>();
        int type = bundle.containsKey("type") ? bundle.getInt("type") : 0;
        int disable_comments = bundle.containsKey("disable_comments") ? bundle.getInt("disable_comments", 0) : 0;
        long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
        long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
        if (place_unic == 0 && type == Consts.TYPE_PROFILE) {
            options.add(new Option(getString(R.string.option_add_place), OPTION_ADD_PLACE));
            options.add(new Option(getString(R.string.album_activity), OPTION_GO_TO_ALBUM));
            options.add(new Option(getString(R.string.option_friends), OPTION_GO_TO_FRIENDS));
            options.add(new Option(getString(R.string.option_backed), OPTION_GO_TO_BACKED));
            options.add(new Option(getString(R.string.option_visits), OPTION_GO_TO_VISITS));
            options.add(new Option(getString(R.string.option_interests), OPTION_GO_TO_INTERESTS));
        } else if (place_unic != 0 && type == Consts.TYPE_PLACE) {
            options.add(new Option(getString(R.string.option_view_place), OPTION_VIEW_PLACE));
            options.add(new Option(getString(R.string.option_remove_place), OPTION_REMOVE_PLACE));
            if(disable_comments == 0) {
                options.add(new Option(getString(R.string.option_commentes), OPTION_COMMENTS));
            }
        }
        return options.stream()
                .sorted(Comparator.comparing(Option::getPosition))
                .collect(Collectors.toList());
    }

    @Override
    public void onChanged(List<DataGroup> groupList) {
        findViewById(R.id.view_progress).setVisibility(View.GONE);
        if (groupList != null && groupList.size() > 0) {
            adapterGroup.update(groupList, profileViewModel.arrayCategoriesMutableLiveData.getValue());
        }
    }
}