package com.wearetogether.v2.ui.holders.group;

import android.view.View;
import androidx.annotation.NonNull;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import org.jetbrains.annotations.NotNull;

public class HolderUserGroup extends HolderItemGroup {

    public HolderUserGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
    }

    @Override
    public void bind(DataGroup item) {

    }
}
