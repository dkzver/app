package com.wearetogether.v2.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.app.photo.*;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.ucrop.UCrop;
import com.wearetogether.v2.ui.adapters.PhotoAdapter;
import com.wearetogether.v2.ui.adapters.ImageViewAdapter;
import com.wearetogether.v2.ui.dialogs.DialogAttachImage;
import com.wearetogether.v2.ui.dialogs.DialogHintPhoto;
import com.wearetogether.v2.ui.dialogs.DialogPhotoOptions;
import com.wearetogether.v2.ui.listeners.*;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.wearetogether.v2.viewmodel.AlbumViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity implements PreviewListener, AttachImage, CaptureListener, EditorPhotoListener {

    private RecyclerView recycler_view_item;
    private PhotoAdapter photoAdapter;
    private ViewPager viewPager;
    private ImageViewAdapter imageViewAdapter;
    private View progress_bar;
    private AlbumViewModel albumViewModel;
    private DialogHintPhoto dialogHintPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        albumViewModel.setup();
        findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.view_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.SUser != null) {
                    DialogAttachImage dialogAttachImage = new DialogAttachImage();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("create", true);
                    bundle.putBoolean("gallery", true);
                    dialogAttachImage.setArguments(bundle);
                    dialogAttachImage.show(getSupportFragmentManager(), "attach_image");
                }
            }
        });
        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
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
        if (savedInstanceState == null && App.SUser != null) {
            albumViewModel.bind(this, Long.parseLong(App.SUser.unic));
        }
        albumViewModel.mutableLiveData.observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                photoAdapter.update();
                imageViewAdapter.update();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setAppTitle(int position) {
        ((TextView) findViewById(R.id.text_view_title)).setText(position + " " + getList().size());
    }

    private void setAppTitle(String title) {
        ((TextView) findViewById(R.id.text_view_title)).setText(title);
    }

    @Override
    public void showPhoto(long unic, int position, String original, String icon) {
        String action = getIntent().getStringExtra("action");
        if(action != null) {
            Long item_unic = App.SUser == null ? 0 : Long.valueOf(App.SUser.unic);
            ClickToPhotoStar clickToPhotoStar = new ClickToPhotoStar(Consts.TYPE_USER, this, item_unic, unic, position);
            ClickToPhotoStar.original = original;
            ClickToPhotoStar.icon = icon;
            clickToPhotoStar.execute();
        } else {
            viewPager.setCurrentItem(position);
            setAppTitle(position + 1);
            togglePreview(true);
        }
    }

    private void togglePreview(boolean isShow) {
        if (isShow) {
            viewPager.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCapture(String original, String small, String icon) {
        if (App.SUser != null) {
            Insert.Start(this, original, small, icon, Long.parseLong(App.SUser.unic), Consts.TYPE_USER, 0);
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

    @Override
    public List<MediaItem> getList() {
        if (getAlbumViewModel() == null) return new ArrayList<>();
        if (getAlbumViewModel().mutableLiveData == null) return new ArrayList<>();
        return getAlbumViewModel().mutableLiveData.getValue();
    }

    @Override
    public void showProgressBar(boolean isShow) {
        progress_bar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private AlbumViewModel getAlbumViewModel() {
        if (albumViewModel == null) {
            albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        }
        return albumViewModel;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getVisibility() == View.VISIBLE) {
            setAppTitle(getString(R.string.gallery));
            togglePreview(false);
        } else {
            back();
        }
    }

    public void back() {
        super.onBackPressed();
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
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        Long unic = null;
        Integer position = null;
        Uri resultUri = null;
        if(dialogHintPhoto != null) {
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
                    if (unic != 0 && resultUri != null) {
                        File file = new File(resultUri.getPath());
                        if (file.exists()) {
                            CropOriginalEnd cropOriginalEnd = new CropOriginalEnd();
                            cropOriginalEnd.execute(this, file, unic);
                        }
                    }
                    break;
                case Consts.REQUEST_CROP_ICON:
                    unic = UCrop.getUnic(data);
                    position = UCrop.getPosition(data);
                    resultUri = UCrop.getOutput(data);
                    int type = UCrop.getType(data);
                    if (unic != 0 && resultUri != null) {
                        File file = new File(resultUri.getPath());
                        if (file.exists()) {
                            CropIconEnd cropIconEnd = new CropIconEnd();
                            cropIconEnd.execute(this, file, unic, position, Consts.TYPE_USER);
                        }
                    }
                    break;
            }

        }
    }

    public void attachPhoto(MediaItem mediaItem) {
        List<MediaItem> mediaItems = getList();
        mediaItems.add(mediaItem);
        getAlbumViewModel().mutableLiveData.setValue(mediaItems);
        if (photoAdapter != null) {
            photoAdapter.update();
        }
        if (imageViewAdapter != null) {
            imageViewAdapter.update();
        }
        showProgressBar(false);
    }

    @Override
    public void showMenu(Long unic, int position, String hint, int star, String original, String icon) {
        final AlbumActivity activity = this;
        DialogPhotoOptions dialogUserPhotoOptions = new DialogPhotoOptions();
        dialogUserPhotoOptions.setListener(new PhotoOptions() {
            @Override
            public void clickToPhotoStar(long unic, int position) {
                Long item_unic = App.SUser == null ? 0 : Long.valueOf(App.SUser.unic);
                ClickToPhotoStar clickToPhotoStar = new ClickToPhotoStar(Consts.TYPE_USER, activity, item_unic, unic, position);
                ClickToPhotoStar.original = original;
                ClickToPhotoStar.icon = icon;
                clickToPhotoStar.execute();
            }

            @Override
            public void clickToRemovePhoto(long unic, int position) {
                Remove.Start(position, Long.parseLong(App.SUser.unic), unic, Consts.TYPE_USER, activity, new RemovePhotoListener() {
                    @Override
                    public void removePhoto(int position, boolean isStar) {
                        List<MediaItem> mediaItems = getAlbumViewModel().mutableLiveData.getValue();
                        if (mediaItems != null) {
                            mediaItems.remove(position);
                            getAlbumViewModel().mutableLiveData.setValue(mediaItems);
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
                                    List<MediaItem> mediaItems = getAlbumViewModel().mutableLiveData.getValue();
                                    if (mediaItems != null) {
                                        for (int x = 0; x < mediaItems.size(); x++) {
                                            if (x == position) mediaItems.get(x).hint = hint;
                                            else continue;
                                        }
                                        getAlbumViewModel().mutableLiveData.setValue(mediaItems);
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

    public void updateIcon(MediaItem mediaItem, int position) {
        System.out.println("updateIcon " + mediaItem);
        List<MediaItem> mediaItems = getAlbumViewModel().mutableLiveData.getValue();
        if (mediaItems != null) {
            mediaItems.get(position).star = 1;
            mediaItems.get(position).icon = mediaItem.icon;
        }
        getAlbumViewModel().mutableLiveData.setValue(mediaItems);
        if (photoAdapter != null) {
            photoAdapter.update();
        }
        if (imageViewAdapter != null) {
            imageViewAdapter.update();
        }
        showProgressBar(false);
        PreferenceUtils.SaveUserAvatar(getApplicationContext(), mediaItem.icon);
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());
        App.IsUpdate = true;
    }
}
