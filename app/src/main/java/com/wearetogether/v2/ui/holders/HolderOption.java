package com.wearetogether.v2.ui.holders;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.ui.listeners.OptionListener;
import org.jetbrains.annotations.NotNull;

public class HolderOption extends RecyclerView.ViewHolder {

    private int key;
    private int position;
    private Class<?> cls;
    private Bundle bundle;

    public HolderOption(@NonNull @NotNull View itemView, OptionListener listener) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickToOption(key, bundle, cls);
            }
        });
    }

    public void bind(Option option, int position, Bundle bundle, Class<?> cls) {
        this.key = option.getKey();
        this.position = position;
        this.bundle = bundle;
        this.cls = cls;
        ((TextView) itemView).setText(option.getTitle());
    }
}
