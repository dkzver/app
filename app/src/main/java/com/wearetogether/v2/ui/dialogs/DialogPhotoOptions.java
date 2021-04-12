package com.wearetogether.v2.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.listeners.PhotoOptions;

import java.util.ArrayList;
import java.util.List;

public class DialogPhotoOptions extends DialogFragment {

    private PhotoOptions listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<String> actions = new ArrayList<String>();
        Bundle bundle = getArguments();
        final int position = bundle != null ? bundle.getInt(Consts.POSITION, 0) : 0;
        final long unic = bundle != null ? bundle.getLong(Consts.UNIC, 0) : 0;
        final String hint = bundle != null ? bundle.getString(Consts.HINT, "") : "";
        actions.add(getString(R.string.option_set_star));
        actions.add(getString(R.string.option_change_hint));
        actions.add(getString(R.string.option_remove));
        FragmentActivity activity = getActivity();
        Context context;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && activity != null) {
            context = getContext();
        } else {
            context = activity;
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                actions
        );
        return new AlertDialog.Builder(context)
                .setTitle(getString(R.string.selected_action))
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        if(actions.get(pos).equalsIgnoreCase(getString(R.string.option_set_star))) {
                            listener.clickToPhotoStar(unic, position);
                        } else if(actions.get(pos).equalsIgnoreCase(getString(R.string.option_remove))) {
                            listener.clickToRemovePhoto(unic, position);
                        } else if(actions.get(pos).equalsIgnoreCase(getString(R.string.option_change_hint))) {
                            listener.clickToChangeHintPhoto(unic, position, hint);
                        }
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

    public void setListener(PhotoOptions listener) {
        this.listener = listener;
    }
}

