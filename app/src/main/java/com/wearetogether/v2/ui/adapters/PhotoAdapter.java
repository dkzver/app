package com.wearetogether.v2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.ui.holders.HolderPhoto;
import com.wearetogether.v2.ui.listeners.CaptureListener;
import com.wearetogether.v2.ui.listeners.PreviewListener;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<HolderPhoto> {

    private PreviewListener listener;
    private FragmentActivity activity;

    public PhotoAdapter(PreviewListener listener, FragmentActivity activity) {
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public HolderPhoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_photo, parent, false);
        return new HolderPhoto(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPhoto holder, int position) {
        holder.bind(listener.getList().get(position));
    }

    public void update() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listener.getList().size();
    }
}

