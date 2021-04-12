package com.wearetogether.v2.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.ui.activities.OptionActivity;
import com.wearetogether.v2.ui.adapters.AdapterOption;
import com.wearetogether.v2.ui.listeners.OptionListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DialogOptionBottom extends BottomSheetDialogFragment {
    protected AdapterOption adapterOption;
    protected RecyclerView recycler_view_options;

    public static DialogOptionBottom newInstance(Bundle bundle, Class<?> cls) {
        DialogOptionBottom dialog = new DialogOptionBottom();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getDialog() != null) {
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_option_bottom, container,
                false);
        if(getActivity() != null) {
            Bundle bundle = getArguments();
            if(bundle != null) {
                Class<?> cls = (Class<?>) bundle.getSerializable("cls");
                recycler_view_options = (RecyclerView) view.findViewById(R.id.recycler_view_options);
                recycler_view_options.setHasFixedSize(true);
                recycler_view_options.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                adapterOption = new AdapterOption((OptionActivity) getActivity(), cls);
                recycler_view_options.setAdapter(adapterOption);
                adapterOption.update(bundle);
            }
        }

        // get the views and attach the listener

        return view;
    }
}
