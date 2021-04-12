package com.wearetogether.v2.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.ui.listeners.ChangeHintListener;
import com.wearetogether.v2.ui.listeners.VoiceListener;

public class DialogHintPhoto extends DialogFragment implements VoiceListener {

    private long unic = 0;
    public EditText edit_text_hint;
    private ChangeHintListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_photo_hint, null);
        ImageView image_view_voice = v.findViewById(R.id.image_view_voice);
        image_view_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Voice.Voice(getActivity(), Consts.REQUEST_VOICE_HINT);
                }
            }
        });
        edit_text_hint = v.findViewById(R.id.edit_text_hint);
        Bundle bundle = getArguments();
        if(bundle != null) {
            unic = bundle.getLong(Consts.UNIC);
            edit_text_hint.setText(bundle.getString(Consts.HINT));
            edit_text_hint.setTag(bundle.getInt(Consts.POSITION));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_title_change_hint))
                .setView(v)
                .setPositiveButton(R.string.save,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(listener != null) {
                                    String hint = edit_text_hint.getText().toString();
                                    int position = edit_text_hint.getTag() != null ? (int)edit_text_hint.getTag() : 0;
                                    listener.onChangeHint(hint, position, unic);
                                }
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

    @Override
    public void OnSetVoice(int code, String spokenText) {
        if (edit_text_hint != null) {
            edit_text_hint.setText(spokenText);
        }
    }

    public void setListener(ChangeHintListener listener) {
        this.listener = listener;
    }
}

