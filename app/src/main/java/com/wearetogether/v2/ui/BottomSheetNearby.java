package com.wearetogether.v2.ui;

import android.view.Gravity;
import android.view.View;
import androidx.annotation.NonNull;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheetNearby {
    public static void Setup(MainActivity activity) {
        activity.view_bottom_sheet_nearby = activity.findViewById(R.id.bottom_sheet_nearby);
        activity.view_bottom_sheet_nearby.setVisibility(View.GONE);
        activity.bottom_sheet_nearby = BottomSheetBehavior.from(activity.view_bottom_sheet_nearby);
        activity.bottom_sheet_nearby.setState(BottomSheetBehavior.STATE_COLLAPSED);
        activity.bottom_sheet_nearby.setPeekHeight(0);
        activity.bottom_sheet_nearby.setHideable(false);
        activity.bottom_sheet_nearby.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        activity.view_bottom_sheet_serach = activity.findViewById(R.id.bottom_sheet_serach);
        activity.view_bottom_sheet_serach.setVisibility(View.GONE);
        activity.bottom_sheet_serach = BottomSheetBehavior.from(activity.view_bottom_sheet_serach);
        activity.bottom_sheet_serach.setState(BottomSheetBehavior.STATE_COLLAPSED);
        activity.bottom_sheet_serach.setPeekHeight(0);
        activity.bottom_sheet_serach.setHideable(false);
        activity.bottom_sheet_serach.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    activity.component_view_serach.view_container.setBackgroundResource(R.drawable.serach_background);
                    if(activity.component_view_serach.button_back.getVisibility() == View.VISIBLE) {
                        activity.component_view_serach.button_back.setVisibility(View.GONE);
                        activity.component_view_serach.hideView(activity.component_view_serach.button_back, Gravity.RIGHT);
                        activity.component_view_serach.showView(activity.component_view_serach.button_search, Gravity.RIGHT);

                    }
                } else {
                    activity.component_view_serach.showView(activity.component_view_serach.button_back, Gravity.RIGHT);
                    activity.component_view_serach.hideView(activity.component_view_serach.button_search, Gravity.RIGHT);
                    activity.component_view_serach.view_container.setBackgroundResource(R.drawable.serach_background_active);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
}
