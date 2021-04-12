package com.wearetogether.v2.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.viewmodel.BackedViewModel;
import com.wearetogether.v2.viewmodel.VisitedPlaceViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VisitedPlaceActivity extends OptionActivity {
    private VisitedPlaceViewModel viewModel;
    private RecyclerView recycler_view_item;
    private AdapterGroup adapterGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_place);
        App.SUser = PreferenceUtils.GetUser(getApplicationContext());
        final FragmentActivity activity = this;


        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterGroup = new AdapterGroup(this, VisitedPlaceActivity.this, VisitedPlaceActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        recycler_view_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println(dy);
            }
        });

        viewModel = ViewModelProviders.of(this).get(VisitedPlaceViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            if (string_unic != null && savedInstanceState == null) {
                viewModel.bind(this, Long.parseLong(string_unic));
            }
        }
        if (savedInstanceState == null) {// && App.SUser != null
            long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
        }
        viewModel.mutableLiveData.observe(this, new Observer<List<DataGroup>>() {
            @Override
            public void onChanged(List<DataGroup> groupList) {
                findViewById(R.id.view_progress).setVisibility(View.GONE);
                if (groupList != null && groupList.size() > 0) {
                    adapterGroup.update(groupList, viewModel.arrayCategoriesMutableLiveData.getValue());
                }
            }
        });
    }

    @Override
    public void clickToOption(int key, Bundle bundle, Class<?> cls) {

    }

    @Override
    public List<Option> getOptions(Bundle bundle, Class<?> cls) {
        return null;
    }
}
