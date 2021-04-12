package com.wearetogether.v2.ui.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.user.EditInterest;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.UserInterest;
import com.wearetogether.v2.ui.adapters.CheckedInterestAdapter;
import com.wearetogether.v2.ui.adapters.CheckedUserAdapter;
import com.wearetogether.v2.viewmodel.FormRoomViewModel;
import com.wearetogether.v2.viewmodel.UserInterestsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    private UserInterestsViewModel userInterestsViewModel;
    private RecyclerView recycler_view_item;
    private CheckedInterestAdapter checkedInterestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        if (App.SUser == null) {
            onBackPressed();
        } else {
            findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
            recycler_view_item.setHasFixedSize(true);
            recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            checkedInterestAdapter = new CheckedInterestAdapter(this);
            recycler_view_item.setAdapter(checkedInterestAdapter);

            userInterestsViewModel = ViewModelProviders.of(this).get(UserInterestsViewModel.class);
            if (savedInstanceState == null) {
                userInterestsViewModel.bind(this);
            }
            userInterestsViewModel.interestsMutableLiveData.observe(this, new Observer<List<Interest>>() {
                @Override
                public void onChanged(List<Interest> interests) {
                    checkedInterestAdapter.update(interests);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        EditInterest.Start(this);
    }

    public void back() {
        super.onBackPressed();
    }

    public void selected(Integer id, boolean isChecked) {
        HashMap<Integer, Boolean> mapSelected = getViewModel().selectedMutableLiveData.getValue();
        if (mapSelected == null) {
            mapSelected = new HashMap<>();
        }
        mapSelected.put(id, isChecked);
        getViewModel().selectedMutableLiveData.setValue(mapSelected);
    }

    public UserInterestsViewModel getViewModel() {
        if (userInterestsViewModel == null) {
            userInterestsViewModel = ViewModelProviders.of(this).get(UserInterestsViewModel.class);
        }
        return userInterestsViewModel;
    }
}
