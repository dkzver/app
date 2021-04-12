package com.wearetogether.v2.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.room.Remove;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.viewmodel.RoomsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoomsActivity extends AppCompatActivity {

    public RoomsViewModel roomsViewModel;
    public AdapterGroup adapterGroup;
    private RecyclerView recycler_view_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        final RoomsActivity activity = this;

        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        roomsViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        adapterGroup = new AdapterGroup(this, RoomsActivity.this, RoomsActivity.class);
        recycler_view_item.setAdapter(adapterGroup);


        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FormRoomActivity.class));
            }
        });

        if (savedInstanceState == null) {
            roomsViewModel.bind(this);
        }
        roomsViewModel.mutableLiveData.observe(this, new Observer<List<Room>>() {
            @Override
            public void onChanged(List<Room> rooms) {
                List<DataGroup> dataGroups = new ArrayList<>();
                dataGroups.add(new DataGroup().Appbar(getString(R.string.title_messages), RoomsActivity.class));
                if (rooms != null && rooms.size() > 0) {
                    for (Room room : rooms) {
                        dataGroups.add(new DataGroup().Room(room));
                    }
                } else {
                    dataGroups.add(new DataGroup(getString(R.string.dont_comments), DataGroup.TYPE_TEXT));
                }
                adapterGroup.update(dataGroups, null);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.IsUpdate) {
            roomsViewModel.bind(this);
            App.IsUpdate = false;
        }
    }

    public void back() {
        super.onBackPressed();
    }

    public RoomsViewModel getViewModel() {
        if (roomsViewModel == null) {
            roomsViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        }
        return roomsViewModel;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void remove(long unic) {
        final FragmentActivity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(RoomsActivity.this);
        builder.setTitle("Remove room");
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Remove.Start(activity, unic);
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
}
