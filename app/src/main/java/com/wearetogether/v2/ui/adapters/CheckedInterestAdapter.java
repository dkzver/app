package com.wearetogether.v2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.InterestsActivity;
import com.wearetogether.v2.ui.holders.HolderCheckedInterest;

import java.util.ArrayList;
import java.util.List;

public class CheckedInterestAdapter extends RecyclerView.Adapter<HolderCheckedInterest> {

    private List<Interest> interestList = new ArrayList<>();
    private InterestsActivity activity;

    public CheckedInterestAdapter(InterestsActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public HolderCheckedInterest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_checked_interest, parent, false);
        return new HolderCheckedInterest(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCheckedInterest holder, int position) {
        holder.bind(interestList.get(position));
    }

    public void update(List<Interest> interestList) {
        this.interestList.clear();
        this.interestList.addAll(interestList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }
}

