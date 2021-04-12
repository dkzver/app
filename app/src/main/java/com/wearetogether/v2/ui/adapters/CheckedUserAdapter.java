package com.wearetogether.v2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.FormRoomActivity;
import com.wearetogether.v2.ui.holders.HolderCheckedUser;

import java.util.ArrayList;
import java.util.List;

public class CheckedUserAdapter extends RecyclerView.Adapter<HolderCheckedUser> {

    private List<User> userList = new ArrayList<>();
    private FormRoomActivity activity;

    public CheckedUserAdapter(FormRoomActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public HolderCheckedUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_checked_user, parent, false);
        return new HolderCheckedUser(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCheckedUser holder, int position) {
        holder.bind(userList.get(position));
    }

    public void update(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

