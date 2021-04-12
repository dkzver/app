package com.wearetogether.v2.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Capture;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.app.message.Add;
import com.wearetogether.v2.app.message.Remove;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.RealTimeMessageData;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.components.WriteMessageViewComponent;
import com.wearetogether.v2.ui.listeners.CaptureListener;
import com.wearetogether.v2.utils.ToastUtils;
import com.wearetogether.v2.viewmodel.RoomViewModel;

import java.io.File;
import java.util.*;

public class RoomActivity extends AppCompatActivity implements CaptureListener {

    public RoomViewModel roomViewModel;
    public AdapterGroup adapterGroup;
    private RecyclerView recycler_view_item;
    private View view_remove;
    private View view_check;
    private View view_form_text;
    public WriteMessageViewComponent component_write_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        final RoomActivity activity = this;
        view_form_text = findViewById(R.id.view_form_text);
        TextView text_view_title = findViewById(R.id.text_view_title);
        View image_view_back = findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        view_remove = findViewById(R.id.view_remove);
        View view_edit = findViewById(R.id.view_edit);

        component_write_message = findViewById(R.id.component_write_message);
        component_write_message.setActivity(this);
        component_write_message.setListener(new WriteMessageViewComponent.Listener() {
            @Override
            public void onClickAttachListener(WriteMessageViewComponent component) {

            }

            @Override
            public void onClickPhotoListener(WriteMessageViewComponent component) {

            }

            @Override
            public void onClickWriteMessage(String text) {
                Add.Text(activity, App.RoomUnic);
            }

            @Override
            public void onError(int code) {
                switch (code) {
                    case WriteMessageViewComponent.ERROR_LEN_TEXT:
                        ToastUtils.Short(getApplicationContext(), "Error len text");
                        break;
                }
            }

            @Override
            public void onRecordStart(WriteMessageViewComponent component) {

            }

            @Override
            public void onRecordStop(WriteMessageViewComponent component, File file) {

            }
        });
//        PressingImageView image_view_voice = findViewById(R.id.image_view_voice);
        view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.Short(getApplicationContext(), "Edit room " + App.RoomUnic);
            }
        });
        view_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Room room = getViewModel().mutableLiveData.getValue();
                if (room != null) {
                    HashMap<String, Boolean> map = getViewModel().selectedMutableLiveData.getValue();
                    if (map != null && map.size() > 0) {
                        List<Long> longList = new ArrayList<>();
                        for (String key : map.keySet()) {
                            if (map.get(key)) {
                                longList.add(Long.parseLong(key));
                            }
                        }
                        if (longList.size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
                            builder.setTitle(getString(R.string.remove_messages));
                            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Remove.Start(activity, longList, room.unic, dialog);
                                }
                            });
                            builder.show();
                        }
                    }
                }
            }
        });
//        image_view_voice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Voice.Voice(activity, Consts.REQUEST_VOICE_COMMENT);
//
//            }
//        });
        Long room_unic = null;
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            if (string_unic != null) {
                room_unic = Long.parseLong(string_unic);
            } else {
                room_unic = null;
            }
            App.RoomUnic = room_unic;
        }

        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        adapterGroup = new AdapterGroup(this, RoomActivity.this, RoomActivity.class);
        recycler_view_item.setAdapter(adapterGroup);

        if (savedInstanceState == null && room_unic != null) {
            roomViewModel.bind(this, room_unic);
        }
        roomViewModel.hasModeMutableLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                view_remove.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                view_check.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
        roomViewModel.mapMessagesMutableLiveData.observe(this, new Observer<HashMap<String, RealTimeMessageData>>() {
            @Override
            public void onChanged(HashMap<String, RealTimeMessageData> mapMessages) {
                if (mapMessages == null) {
                    mapMessages = new HashMap<>();
                }
                List<DataGroup> messages = new ArrayList<>();
                Room room = roomViewModel.mutableLiveData.getValue();
                if (room != null && App.SUser != null) {
                    text_view_title.setText(room.title);//room.title +
                    if (room.owner == Long.parseLong(App.SUser.unic)) {
                        view_edit.setVisibility(View.VISIBLE);
                    }
                }
                if (mapMessages.size() > 0) {
                    HashMap<String, Boolean> selectedMap = getViewModel().selectedMutableLiveData.getValue();
                    if (selectedMap == null) {
                        selectedMap = new HashMap<>();
                    }
                    HashMap<String, List<Message>> mainMap = new HashMap<>();
                    List<Message> messageList = new ArrayList<>();

                    for (String key : mapMessages.keySet()) {
                        messageList.add(mapMessages.get(key).getMessage(key, true));
                    }

                    Collections.sort(messageList);
                    List<Message> mList = null;
                    for (Message message : messageList) {
                        String date_message = roomViewModel.getDate(activity, message);
                        if (mainMap.get(date_message) == null) {
                            mList = new ArrayList<>();
                        } else {
                            mList = mainMap.get(date_message);
                        }
                        if (mList != null) {
                            mList.add(message);
                            Collections.sort(mList);
                            mainMap.put(date_message, mList);
                        }
                    }

                    DataGroup dataGroup = null;
                    for (String key : mainMap.keySet()) {
                        messages.add(new DataGroup().MessageDate(key));
                        mList = mainMap.get(key);
                        for (Message message : mList) {
                            dataGroup = new DataGroup().Message(message, App.SUser.unic);
                            if (selectedMap.containsKey(key)) {
                                dataGroup.selected = selectedMap.get(String.valueOf(message.unic)) ? 1 : 0;
                            }
                            messages.add(dataGroup);
                        }
                    }
                } else {
                    messages.add(new DataGroup(getString(R.string.dont_messages), DataGroup.TYPE_TEXT));
                }
                adapterGroup.update(messages, null);
                scrollToBottom();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean access = true;
        for (int x = 0; x < grantResults.length; x++) {
            if (access && grantResults[x] == PackageManager.PERMISSION_DENIED) {
                access = false;
            }
        }
        if(access) {
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

    public void completeInput(boolean isComplete) {
//        ToastUtils.Short(getApplicationContext(), "Input " + isComplete);
        getViewModel().setIsInput(!isComplete);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        Capture.Gallery(this, requestCode, resultCode, data);
        Capture.Camera(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        App.RoomUnic = null;
        if (component_write_message != null) {
            component_write_message.releasePlayer();
            component_write_message.releaseRecorder();
        }
        super.onDestroy();
    }

    public RoomViewModel getViewModel() {
        if (roomViewModel == null) {
            roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        }
        return roomViewModel;
    }

    public void scrollToBottom() {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recycler_view_item.getLayoutManager();
        if (layoutManager != null) {
            AdapterGroup adapter = (AdapterGroup) recycler_view_item.getAdapter();
            if (adapter != null) {
                final int lastItemPosition = adapter.getItemCount() - 1;

                layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
                recycler_view_item.post(new Runnable() {
                    @Override
                    public void run() {
                        View target = layoutManager.findViewByPosition(lastItemPosition);
                        if (target != null) {
                            int offset = recycler_view_item.getMeasuredHeight() - target.getMeasuredHeight();
                            layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);
                        }
                    }
                });
            }
        }
    }

    public void selected(String unic) {
        HashMap<String, Boolean> map = getViewModel().selectedMutableLiveData.getValue();
        if (map == null) {
            map = new HashMap<>();
        }
        boolean isConstains = false;
        if (map.containsKey(unic)) {
            isConstains = map.get(unic);
        }
        boolean is = adapterGroup.selectedMessage(unic, !isConstains);
        if (is) {
            map.put(unic, !isConstains);
            getViewModel().selectedMutableLiveData.setValue(map);
        }
        map = getViewModel().selectedMutableLiveData.getValue();
        if (map != null && map.size() > 0) {
            getViewModel().hasModeMutableLiveData.setValue(true);
        }
    }

    @Override
    public void onCapture(String original, String small, String icon) {
        if(original != null && small != null && icon != null) {
            Add.Picture(original, small, icon, this, Calendar.getInstance().getTimeInMillis(), getString(R.string.url_attach_picture));
        }
    }

    @Override
    public void addPhoto(MediaItem mediaItem) {

    }

    @Override
    public List<MediaItem> getList() {
        return null;
    }

    @Override
    public void showProgressBar(boolean isShow) {

    }


//    public void add(long parent, Comment comment) {
//        Integer type = getReviewsViewModel().typeMutableLiveData.getValue();
//        if (type != null) {
//            adapterGroup.addComment(parent, comment, type);
//            edit_text.setText("");
//            getReviewsViewModel().replayUnicMutableLiveData.setValue(null);
//            ToastUtils.Short(getApplicationContext(), getString(R.string.action_add_comment));
//            App.HideKeyboard(getApplicationContext(), new View[] {edit_text});
//        }
//    }
//
//    public void remove(long unic, String key, int position) {
//        Remove.Start(this, RoomActivity.this, unic, key, position);
//    }
}
