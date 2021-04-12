package com.wearetogether.v2.ui.holders.group;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import org.jetbrains.annotations.NotNull;

public abstract class HolderBaseGroup extends RecyclerView.ViewHolder {

    public FragmentActivity activity;
    protected AdapterGroup adapterGroup;
    protected Context context;

    public HolderBaseGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView);
        this.adapterGroup = adapterGroup;
        this.activity = adapterGroup.activity;
        this.context = adapterGroup.context;
    }

    public abstract void bind(DataGroup item);
}
