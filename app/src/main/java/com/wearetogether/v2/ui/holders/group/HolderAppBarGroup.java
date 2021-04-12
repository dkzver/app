package com.wearetogether.v2.ui.holders.group;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.*;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.holders.group.HolderBaseGroup;
import org.jetbrains.annotations.NotNull;

public class HolderAppBarGroup extends HolderBaseGroup {
    private final TextView text_view_title;
    private Class<?> cls;

    public HolderAppBarGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        text_view_title = itemView.findViewById(R.id.text_view_title);
        itemView.findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cls == PlacesActivity.class && activity instanceof PlacesActivity) {
                    PlacesActivity placesActivity = (PlacesActivity) activity;
                    placesActivity.onBackPressed();
                } else if (cls == ReviewsActivity.class && activity instanceof ReviewsActivity) {
                    ReviewsActivity reviewsActivity = (ReviewsActivity) activity;
                    reviewsActivity.onBackPressed();
                } else if (cls == BackedActivity.class && activity instanceof BackedActivity) {
                    BackedActivity backedActivity = (BackedActivity) activity;
                    backedActivity.onBackPressed();
                } else if (cls == RoomsActivity.class && activity instanceof RoomsActivity) {
                    RoomsActivity roomsActivity = (RoomsActivity) activity;
                    roomsActivity.onBackPressed();
                } else if (cls == RoomActivity.class && activity instanceof RoomActivity) {
                    RoomActivity roomActivity = (RoomActivity) activity;
                    roomActivity.onBackPressed();
                } else if (cls == VisitedPlaceActivity.class && activity instanceof VisitedPlaceActivity) {
                    VisitedPlaceActivity visitedPlaceActivity = (VisitedPlaceActivity) activity;
                    visitedPlaceActivity.onBackPressed();
                }
            }
        });
    }

    @Override
    public void bind(DataGroup item) {
        this.cls = item.cls;
        text_view_title.setText(item.title);
    }
}
