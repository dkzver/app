package com.wearetogether.v2.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Book;
import com.wearetogether.v2.app.Like;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.Vote;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.viewmodel.PlacesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlacesActivity extends OptionActivity {

    private static final int OPTION_VIEW_PLACE = 1;
    private static final int OPTION_COMMENTS = 2;
    private static final int OPTION_COMPLAIN = 3;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;
    private PlacesViewModel placesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroup = new AdapterGroup(this, PlacesActivity.this, PlacesActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        placesViewModel = ViewModelProviders.of(this).get(PlacesViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.USER_UNIC);
            if (string_unic != null && savedInstanceState == null) {
                placesViewModel.bind(this, Long.parseLong(string_unic), App.SUser == null ? 0 : Long.parseLong(App.SUser.unic));
            }
        }
        placesViewModel.mutableLiveData.observe(this, new Observer<List<DataGroup>>() {
            @Override
            public void onChanged(List<DataGroup> dataGroups) {
                findViewById(R.id.view_progress).setVisibility(View.GONE);
                adapterGroup.update(dataGroups, placesViewModel.arrayCategoriesMutableLiveData.getValue());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
        if (key == OPTION_VIEW_PLACE) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            App.GoToPlace(this, place_unic, cls);
        } else if (key == OPTION_COMMENTS) {
            long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
            App.GoToReviews(this, place_unic, cls, Consts.TYPE_PLACE);
        }
    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        List<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.option_view_place), OPTION_VIEW_PLACE));
        options.add(new Option(getString(R.string.option_commentes), OPTION_COMMENTS));
        options.add(new Option(getString(R.string.option_complain_place), OPTION_COMPLAIN));
        return options.stream()
                .sorted(Comparator.comparing(Option::getPosition))
                .collect(Collectors.toList());
    }
}
