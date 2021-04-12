package com.wearetogether.v2.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.listeners.SelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogSelect extends DialogFragment {

    public SelectListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        Context context;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && activity != null) {
            context = getContext();
        } else {
            context = activity;
        }
        String[] options = null;
        int selected = 0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            options = bundle.getStringArray("options");
            selected = bundle.getInt("selected", 0);
        }
        if (options == null) return new AlertDialog.Builder(context).show();
        return new AlertDialog.Builder(context)
                .setTitle(getString(R.string.selected_action))
                .setSingleChoiceItems(options, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listener != null) {
                            listener.onSelect(which);
                        }
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }
}
