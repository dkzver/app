package com.wearetogether.v2.ui.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.ui.activities.InterestsActivity;

import java.util.HashMap;

public class HolderCheckedInterest extends RecyclerView.ViewHolder {
    private final CheckBox checkbox;
    private InterestsActivity activity;
    private Integer id;

    public HolderCheckedInterest(@NonNull View itemView, InterestsActivity activity) {
        super(itemView);
        this.activity = activity;
        checkbox = itemView.findViewById(R.id.checkbox);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox.setSelected(!checkbox.isSelected());
                activity.selected(id, checkbox.isChecked());
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.selected(id, isChecked);
            }
        });
    }

    public void bind(Interest interest) {
        this.id = interest.id;
        checkbox.setText(interest.title);
        HashMap<Integer, Boolean> map = activity.getViewModel().selectedMutableLiveData.getValue();
        if(map != null) {
            if(map.containsKey(id)) {
                checkbox.setChecked(map.get(id));
            }
        }
    }
}
