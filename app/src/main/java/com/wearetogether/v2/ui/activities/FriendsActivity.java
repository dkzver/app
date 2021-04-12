package com.wearetogether.v2.ui.activities;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.listeners.HolderBackButtonActivity;
import com.wearetogether.v2.ui.listeners.HolderOptionsActivity;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.viewmodel.BackedViewModel;
import com.wearetogether.v2.viewmodel.FriendsViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements HolderBackButtonActivity, HolderOptionsActivity {
    private FriendsViewModel friendsViewModel;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());


        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroup = new AdapterGroup(this, FriendsActivity.this, FriendsActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        recycler_view_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println(dy);
            }
        });

        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        if (savedInstanceState == null) {// && App.SUser != null
            long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
            friendsViewModel.bind(this, user_unic);
        }
        friendsViewModel.selectedMutableLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int friend = Friend.FRIEND;
                if (integer == 1) {
                    friend = Friend.REQUEST_FRIEND;
                } else if (integer == 2) {
                    friend = Friend.SEND_REQUEST_FRIEND;
                }
                findViewById(R.id.view_progress).setVisibility(View.GONE);
                List<DataGroup> dataGroupsUsers = friendsViewModel.mutableLiveData.getValue();
                List<DataGroup> dataGroups = new ArrayList<>();
                dataGroups.add(new DataGroup().Options(getResources().getStringArray(R.array.friends_menu), integer));
                if (dataGroupsUsers == null || dataGroupsUsers.size() == 0 ) {
                    dataGroups.add(new DataGroup(getString(R.string.not_friends), DataGroup.TYPE_TEXT));
                } else {
                    System.out.println(friend);
                    for (DataGroup data : dataGroupsUsers) {
                        if (data.friend == friend) {
                            dataGroups.add(data);
                        }
                    }
                }
                adapterGroup.update(dataGroups, null);
            }
        });
    }

    @Override
    public void onOptionsSelected(int position) {
        if (friendsViewModel == null) {
            friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        }
        Integer selected = friendsViewModel.selectedMutableLiveData.getValue();
        if (selected != null && position != selected) {
            friendsViewModel.selectedMutableLiveData.setValue(position);
        }
    }
}
