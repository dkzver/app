package com.wearetogether.v2.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.ui.activities.MainActivity;

public class DialogMapOptions extends DialogFragment {

    public MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_map_options, null);
        CheckBox show_current_date = v.findViewById(R.id.show_current_date);
        CheckBox show_old_date = v.findViewById(R.id.show_old_date);
        CheckBox show_future_date = v.findViewById(R.id.show_future_date);
        CheckBox show_all_date = v.findViewById(R.id.show_all_date);
        CheckBox online_mode = v.findViewById(R.id.online_mode);
        CheckBox offline_mode = v.findViewById(R.id.offline_mode);
        CheckBox all_mode = v.findViewById(R.id.all_mode);
        CurrentTab tab = activity.getViewModel().currentTabMutableLiveData.getValue();
        if(tab == null) {
            tab = CurrentTab.Places;
        }
        show_current_date.setVisibility(tab == CurrentTab.Users ? View.GONE : View.VISIBLE);
        show_old_date.setVisibility(tab == CurrentTab.Users ? View.GONE : View.VISIBLE);
        show_future_date.setVisibility(tab == CurrentTab.Users ? View.GONE : View.VISIBLE);
        show_all_date.setVisibility(tab == CurrentTab.Users ? View.GONE : View.VISIBLE);
        online_mode.setVisibility(tab == CurrentTab.Places ? View.GONE : View.VISIBLE);
        offline_mode.setVisibility(tab == CurrentTab.Places ? View.GONE : View.VISIBLE);
        all_mode.setVisibility(tab == CurrentTab.Places ? View.GONE : View.VISIBLE);
        MapOptions mapOptions = activity.getViewModel().mapOptionsMutableLiveData.getValue();
        if(mapOptions == null) {
            mapOptions = new MapOptions();
        }
        show_current_date.setChecked(mapOptions.current_date);
        show_old_date.setChecked(mapOptions.old_date);
        show_future_date.setChecked(mapOptions.future_date);
        show_all_date.setChecked(mapOptions.all_date);
        online_mode.setChecked(mapOptions.online_mode);
        offline_mode.setChecked(mapOptions.offlie_mode);
        all_mode.setChecked(mapOptions.all_mode);

        show_current_date.setOnCheckedChangeListener(change());
        show_old_date.setOnCheckedChangeListener(change());
        show_future_date.setOnCheckedChangeListener(change());
        show_all_date.setOnCheckedChangeListener(change());
        online_mode.setOnCheckedChangeListener(change());
        offline_mode.setOnCheckedChangeListener(change());
        all_mode.setOnCheckedChangeListener(change());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_title_map_options))
                .setView(v)
                .setPositiveButton(R.string.apply,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
        return builder.create();
    }

    private CompoundButton.OnCheckedChangeListener change() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapOptions mapOptions = activity.getViewModel().mapOptionsMutableLiveData.getValue();
                if(mapOptions == null) {
                    mapOptions = new MapOptions();
                }
                switch (buttonView.getId()) {
                    case R.id.show_current_date:
                        mapOptions.current_date = isChecked;
                        break;
                    case R.id.show_old_date:
                        mapOptions.old_date = isChecked;
                        break;
                    case R.id.show_future_date:
                        mapOptions.future_date = isChecked;
                        break;
                    case R.id.show_all_date:
                        mapOptions.all_date = isChecked;
                        break;
                    case R.id.online_mode:
                        mapOptions.online_mode = isChecked;
                        break;
                    case R.id.offline_mode:
                        mapOptions.offlie_mode = isChecked;
                        break;
                    case R.id.all_mode:
                        mapOptions.all_mode = isChecked;
                        break;
                }
                activity.getViewModel().mapOptionsMutableLiveData.setValue(mapOptions);
            }
        };
    }
}

