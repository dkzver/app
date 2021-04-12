package com.wearetogether.v2.ui.holders.group;

import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.adapters.PhotoAdapter;
import com.wearetogether.v2.ui.holders.group.HolderBaseGroup;
import com.wearetogether.v2.ui.listeners.PreviewListener;

public class HolderGalleryGroup extends HolderBaseGroup {
    private final RecyclerView recycler_view;
    private final PhotoAdapter adapter;

    public HolderGalleryGroup(View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        recycler_view = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recycler_view.setLayoutManager(layoutManager);
        adapter = new PhotoAdapter((PreviewListener) activity, activity);
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void bind(DataGroup item) {
        adapter.update();
    }
}
