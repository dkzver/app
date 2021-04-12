package com.wearetogether.v2.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.listeners.AttachImage;

public class DialogAttachImage extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_attach_image, null);
        Bundle bundle = getArguments();
        if(bundle != null) {
            View view_button_create_photo = v.findViewById(R.id.view_button_create_photo);
            View view_button_select_in_gallery = v.findViewById(R.id.view_button_select_in_gallery);
            View view_button_select_in_album = v.findViewById(R.id.view_button_select_in_album);
            view_button_create_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof AttachImage) {
                        AttachImage activity = (AttachImage) getActivity();
                        activity.createPhoto();
                    }
                    dismiss();
                }
            });
            view_button_select_in_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof AttachImage) {
                        AttachImage activity = (AttachImage) getActivity();
                        activity.selectInGallery();
                    }
                    dismiss();
                }
            });
            view_button_select_in_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof AttachImage) {
                        AttachImage activity = (AttachImage) getActivity();
                        activity.selectInAlbum();
                    }
                    dismiss();
                }
            });
            boolean create = bundle.getBoolean("create");
            boolean gallery = bundle.getBoolean("gallery");
            boolean album = bundle.getBoolean("album");
            if(!create) {
                view_button_create_photo.setVisibility(View.GONE);
            }
            if(!gallery) {
                view_button_select_in_gallery.setVisibility(View.GONE);
            }
            if(!album) {
                view_button_select_in_album.setVisibility(View.GONE);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_photo));
        builder.setView(v);
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dismiss();
                    }
                }
        );
        builder.setCancelable(false);
        return builder.create();
    }
}


