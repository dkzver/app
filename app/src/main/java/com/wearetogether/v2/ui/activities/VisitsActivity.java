package com.wearetogether.v2.ui.activities;

import android.os.Bundle;
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
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.listeners.HolderBackButtonActivity;
import com.wearetogether.v2.ui.listeners.HolderOptionsActivity;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.viewmodel.VisitsViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisitsActivity extends AppCompatActivity implements HolderBackButtonActivity, HolderOptionsActivity {
    private VisitsViewModel visitsViewModel;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());


        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroup = new AdapterGroup(this, VisitsActivity.this, VisitsActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        recycler_view_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println(dy);
            }
        });

        visitsViewModel = ViewModelProviders.of(this).get(VisitsViewModel.class);
        if (savedInstanceState == null && App.SUser != null) {
            int defSelected = 0;
            visitsViewModel.bind(this, defSelected);
        }
        visitsViewModel.selectedMutableLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer selected) {
                findViewById(R.id.view_progress).setVisibility(View.GONE);
                List<DataGroup> dataGroupsPlaces = null;
                switch (selected) {
                    case 0:
                        dataGroupsPlaces = visitsViewModel.visitMutableLiveData.getValue();
                        break;
                    case 1:
                        dataGroupsPlaces = visitsViewModel.anonimVisitMutableLiveData.getValue();
                        break;
                    case 2:
                        dataGroupsPlaces = visitsViewModel.savedMutableLiveData.getValue();
                        break;
                }
                if(dataGroupsPlaces == null) {
                    dataGroupsPlaces = new ArrayList<>();
                }
                List<DataGroup> dataGroups = new ArrayList<>();
                dataGroups.add(new DataGroup().Options(getResources().getStringArray(R.array.visits_menu), selected));
                if(dataGroupsPlaces.size() == 0) {
                    dataGroups.add(new DataGroup(getString(R.string.not_visits), DataGroup.TYPE_TEXT));
                } else {
                    dataGroups.addAll(dataGroupsPlaces);
                }
                adapterGroup.update(dataGroups, visitsViewModel.arrayCategoriesMutableLiveData.getValue());
            }
        });
    }

    public VisitsViewModel getViewModel() {
        if(visitsViewModel == null) {
            visitsViewModel = ViewModelProviders.of(this).get(VisitsViewModel.class);
        }
        return visitsViewModel;
    }

    @Override
    public void onOptionsSelected(int position) {
        Integer selected = getViewModel().selectedMutableLiveData.getValue();
        if(selected != null && position != selected) {
            getViewModel().selectedMutableLiveData.setValue(position);
        }
    }
}
