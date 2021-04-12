package com.wearetogether.v2.ui;

import android.view.View;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.dialogs.DialogMapOptions;

public class ClickToOptions implements View.OnClickListener {
    private MainActivity activity;

    public ClickToOptions(MainActivity activity) {

        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        DialogMapOptions dialogMapOptions = new DialogMapOptions();
        dialogMapOptions.activity = activity;
        dialogMapOptions.show(activity.getSupportFragmentManager(), "options");
    }
}
