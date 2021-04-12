package com.wearetogether.v2.ui.holders.group;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.listeners.HolderBackButtonActivity;
import com.wearetogether.v2.ui.listeners.HolderOptionsActivity;
import org.jetbrains.annotations.NotNull;

public class HolderHeaderGroupOptions extends HolderBaseGroup {

    private final Spinner spinner;
    private final ArrayAdapter<String> adapter;

    public HolderHeaderGroupOptions(@NonNull @NotNull View itemView, AdapterGroup adapterGroup, Context context) {
        super(itemView, adapterGroup);
        spinner = itemView.findViewById(R.id.select);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        View image_view_menu = itemView.findViewById(R.id.image_view_menu);
        if(image_view_menu != null) {
            image_view_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        itemView.findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof HolderBackButtonActivity) {
                    ((HolderBackButtonActivity) activity).onBackPressed();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (activity instanceof HolderOptionsActivity) {
                    ((HolderOptionsActivity) activity).onOptionsSelected(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void bind(DataGroup item) {
        if(item.options != null) {
            adapter.clear();
            adapter.addAll(item.options);
            adapter.notifyDataSetChanged();
            spinner.setSelection(item.selected);
        }
    }
}
