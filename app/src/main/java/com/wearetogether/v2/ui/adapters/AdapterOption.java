package com.wearetogether.v2.ui.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.holders.HolderOption;
import com.wearetogether.v2.ui.holders.HolderPhoto;
import com.wearetogether.v2.ui.listeners.OptionListener;
import org.jetbrains.annotations.NotNull;

public class AdapterOption extends RecyclerView.Adapter<HolderOption> {

    private OptionListener listener;
    private Class<?> cls;
    private Bundle bundle;

    public AdapterOption(OptionListener listener, Class<?> cls) {
        this.listener = listener;
        this.cls = cls;
    }

    public void update(Bundle bundle) {
        this.bundle = bundle;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public HolderOption onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_option, parent, false);
        return new HolderOption(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderOption holder, int position) {
        holder.bind(listener.getOptions(bundle, cls).get(position), position, bundle, cls);
    }

    @Override
    public int getItemCount() {
        return listener.getOptions(bundle, cls).size();
    }
}
