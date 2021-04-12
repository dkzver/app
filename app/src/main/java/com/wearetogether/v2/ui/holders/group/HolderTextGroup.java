package com.wearetogether.v2.ui.holders.group;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.holders.group.HolderBaseGroup;
import org.jetbrains.annotations.NotNull;

public class HolderTextGroup extends HolderBaseGroup {

    private final TextView text_view;

    public HolderTextGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        text_view = itemView.findViewById(R.id.text_view);
    }

    @Override
    public void bind(DataGroup item) {
        text_view.setText(item.text);
    }
}

