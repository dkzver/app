package com.wearetogether.v2.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.room.Add;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.adapters.CheckedUserAdapter;
import com.wearetogether.v2.viewmodel.FormRoomViewModel;

import java.util.HashMap;
import java.util.List;

public class FormRoomActivity extends AppCompatActivity {
    public FormRoomViewModel formRoomViewModel;
    private RecyclerView recycler_view_checked_users;
    private CheckedUserAdapter checkedUserAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_room);
        final FormRoomActivity activity = this;

        formRoomViewModel = ViewModelProviders.of(this).get(FormRoomViewModel.class);
        findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add.Start(activity, FormRoomActivity.this);
            }
        });

        recycler_view_checked_users = (RecyclerView) findViewById(R.id.recycler_view_checked_users);
        recycler_view_checked_users.setHasFixedSize(true);
        recycler_view_checked_users.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        checkedUserAdapter = new CheckedUserAdapter(activity);
        recycler_view_checked_users.setAdapter(checkedUserAdapter);

        Long unic = null;
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            if (string_unic != null) {
                unic = Long.parseLong(string_unic);
            }
        }

        if(savedInstanceState == null) {
            formRoomViewModel.bind(activity, unic);
        }
        formRoomViewModel.mutableLiveData.observe(this, new Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                if(room != null) {
                }
                List<User> users = formRoomViewModel.usersMutableLiveData.getValue();
                if(users != null) {
                    checkedUserAdapter.update(users);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        super.onBackPressed();
    }

    public FormRoomViewModel getViewModel() {
        if(formRoomViewModel == null) {
            formRoomViewModel = ViewModelProviders.of(this).get(FormRoomViewModel.class);
        }
        return formRoomViewModel;
    }

    public void selected(long unic, boolean isChecked) {
        HashMap<Long, Boolean> mapSelected = getViewModel().selectedMutableLiveData.getValue();
        if(mapSelected == null) {
            mapSelected = new HashMap<>();
        }
        mapSelected.put(unic, isChecked);
        getViewModel().selectedMutableLiveData.setValue(mapSelected);
    }
}
