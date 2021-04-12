package com.wearetogether.v2.ui.holders.group;

import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;

public class HolderWriteMessageGroup extends HolderBaseGroup {

    private final TextView text_view;

    public HolderWriteMessageGroup(View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        text_view = (TextView) itemView.findViewById(R.id.text_view);
    }

    @Override
    public void bind(DataGroup item) {
        text_view.setText(String.format(activity.getString(R.string.format_write), item.user_name));
    }
}
