package com.wearetogether.v2.ui.holders.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.activities.OptionActivity;
import com.wearetogether.v2.ui.activities.ProfileActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HolderUserPlaceGroup extends HolderBaseGroup {

    private final View view_item;
    private final View view_more;
    private final AdapterItemImage adapter;
    private final TextView text_view_title;
    private final TextView text_view_description;
    private final TextView text_view_location;
    private final TextView text_view_date_begin;
    private final TextView text_view_date_end;
    private final TextView text_view_time_visit;
    private final TextView text_view_count_participant;
    private final TextView text_view_anonymous_visit;
    private final TextView text_view_category;
    private final RecyclerView recycler_view;
    private final RelativeLayout view_image;
    private final ImageView image_view_snapshot;
    private final ImageView button_toggle;
    private Long place_unic;
    private int is_remove;
    private String title;
    private long user_unic;

    public HolderUserPlaceGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        view_item = itemView.findViewById(R.id.view_item);
        view_more = itemView.findViewById(R.id.view_more);
        view_more.setVisibility(View.GONE);
        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(place_unic != null) {
                    App.GoToFormPlace(activity, place_unic, ProfileActivity.class);
                }
            }
        });
        if (activity instanceof OptionActivity) {
            final OptionActivity optionListener = (OptionActivity) activity;
            view_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Consts.TYPE_PLACE);
                    bundle.putString("title", title);
                    bundle.putLong("place_unic", place_unic);
                    bundle.putLong("user_unic", user_unic);
                    bundle.putInt("is_remove", is_remove);
                    bundle.putInt("position", getAdapterPosition());
                    optionListener.showMenu(bundle, adapterGroup.cls);
                    return true;
                }
            });
        }
        text_view_title = itemView.findViewById(R.id.text_view_title);
        text_view_description = itemView.findViewById(R.id.text_view_description);
        text_view_location = itemView.findViewById(R.id.text_view_location);
        text_view_date_begin = itemView.findViewById(R.id.text_view_date_begin);
        text_view_date_end = itemView.findViewById(R.id.text_view_date_end);
        text_view_time_visit = itemView.findViewById(R.id.text_view_time_visit);
        text_view_count_participant = itemView.findViewById(R.id.text_view_count_participant);
        text_view_anonymous_visit = itemView.findViewById(R.id.text_view_anonymous_visit);
        text_view_category = itemView.findViewById(R.id.text_view_category);
        recycler_view = itemView.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recycler_view.setLayoutManager(layoutManager);
        adapter = new AdapterItemImage();
        recycler_view.setAdapter(adapter);
        view_image = itemView.findViewById(R.id.view_image);
        image_view_snapshot = itemView.findViewById(R.id.image_view_snapshot);
        button_toggle = itemView.findViewById(R.id.button_toggle);
        button_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view_more.getVisibility() == View.GONE) {
                    view_more.setVisibility(View.VISIBLE);
                    button_toggle.setBackgroundResource(R.drawable.baseline_keyboard_arrow_up_black_18dp);
                } else {
                    view_more.setVisibility(View.GONE);
                    button_toggle.setBackgroundResource(R.drawable.baseline_keyboard_arrow_down_black_18dp);
                }
            }
        });
        view_more.setVisibility(View.GONE);
        button_toggle.setBackgroundResource(R.drawable.baseline_keyboard_arrow_down_black_18dp);
    }

    @Override
    public void bind(DataGroup item) {
        place_unic = item.unic;
        is_remove = item.is_remove;
        this.user_unic = item.user_unic;
        title = item.title;
        if(item.bitmapList != null) {
            view_image.setVisibility(View.VISIBLE);
            image_view_snapshot.setImageBitmap(Bitmap.createScaledBitmap(item.bitmapList.get(0), adapterGroup.sizeImage, adapterGroup.sizeImage, false));
            image_view_snapshot.setVisibility(View.VISIBLE);
        }

        text_view_title.setText(item.title);
        if(item.description.equals("")) {
            text_view_description.setVisibility(View.GONE);
        } else {
            text_view_description.setText(item.description);
            text_view_description.setVisibility(View.VISIBLE);
        }
        text_view_location.setText(item.location);
        text_view_date_begin.setText(GetStringValuePlace(activity.getApplicationContext(), item.date_begin));
        text_view_date_end.setText(GetStringValuePlace(activity.getApplicationContext(), item.date_end));
        text_view_time_visit.setText(GetStringValuePlace(activity.getApplicationContext(), item.time_visit));
        text_view_count_participant.setText(String.valueOf(item.count_participant));
        text_view_anonymous_visit.setText(item.anonymous_visit == 1 ? activity.getString(R.string.ok) : activity.getString(R.string.no));
        if(App.Categories.length == 0) {
            String[] category_array = activity.getResources().getStringArray(R.array.category_array);
            text_view_category.setText((item.category_id < category_array.length) ? category_array[item.category_id]  : "");
        } else {
            text_view_category.setText((item.category_id < App.Categories.length) ? App.Categories[item.category_id]  : "");
        }
        if(item.bitmapList.size() > 0) {
            recycler_view.setVisibility(View.VISIBLE);
            adapter.updateList(item.bitmapList, item.hintList);
        } else {
            recycler_view.setVisibility(View.GONE);
        }
    }

    private static String GetStringValuePlace(Context context, String string_value) {
        if (string_value == null || string_value.equals("")) return context.getString(R.string.no_indicated);
        return string_value;
    }

    class AdapterItemImage extends RecyclerView.Adapter<HolderItemImage> {
        private List<Bitmap> bitmapList = new ArrayList<>();
        private List<String> hintList = new ArrayList<>();

        @NonNull
        @Override
        public HolderItemImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_image, parent, false);
            return new HolderItemImage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HolderItemImage holder, int position) {
            holder.bind(bitmapList.get(position), hintList.get(position));
        }

        public void updateList(List<Bitmap> bitmapList, List<String> hintList) {
            this.bitmapList.clear();
            this.bitmapList.addAll(bitmapList);
            this.hintList.clear();
            this.hintList.addAll(hintList);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return bitmapList.size();
        }
    }

    class HolderItemImage extends RecyclerView.ViewHolder {
        private final View view_item;
        private final View view_background;
        private final ImageView image_view_photo;
        private final TextView text_view_hint;

        public HolderItemImage(@NonNull View itemView) {
            super(itemView);
            view_item = itemView.findViewById(R.id.view_item);
            view_background = itemView.findViewById(R.id.view_background);
            image_view_photo = itemView.findViewById(R.id.image_view_photo);
            text_view_hint = itemView.findViewById(R.id.text_view_hint);
        }

        public void bind(Bitmap bitmap, String hint) {
            if(bitmap != null) {
                image_view_photo.setImageBitmap(bitmap);
            }
            if(!hint.equals("")) {
                text_view_hint.setText(hint);
                text_view_hint.setVisibility(View.VISIBLE);
            } else {
                text_view_hint.setVisibility(View.GONE);
            }
        }
    }
}
