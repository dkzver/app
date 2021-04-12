package com.wearetogether.v2.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.app.place.Remove;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.viewmodel.BackedViewModel;
import com.wearetogether.v2.viewmodel.ProfileViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BackedActivity extends OptionActivity {

    private static final int OPTION_RESTORE_PLACE = 1;
    private BackedViewModel backedViewModel;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backed);
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());
        final FragmentActivity activity = this;


        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroup = new AdapterGroup(this, BackedActivity.this, BackedActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        recycler_view_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println(dy);
            }
        });

        backedViewModel = ViewModelProviders.of(this).get(BackedViewModel.class);
        if (savedInstanceState == null) {// && App.SUser != null
            long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
            backedViewModel.bind(this, user_unic);
        }
        backedViewModel.mutableLiveData.observe(this, new Observer<List<DataGroup>>() {
            @Override
            public void onChanged(List<DataGroup> groupList) {
                findViewById(R.id.view_progress).setVisibility(View.GONE);
                if (groupList != null && groupList.size() > 0) {
                    adapterGroup.update(groupList, backedViewModel.arrayCategoriesMutableLiveData.getValue());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void removeItemFromAdapter(int position) {
        adapterGroup.remove(position);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
        if(key == OPTION_RESTORE_PLACE) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            int position = bundle.containsKey("position") ? bundle.getInt("position", 0) : 0;
            App.ShowDialogRemovePlace(this, BackedActivity.this, 1, place_unic, position);
        }
    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        List<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.option_restore_place), OPTION_RESTORE_PLACE, 1));
        return options;
    }
}
