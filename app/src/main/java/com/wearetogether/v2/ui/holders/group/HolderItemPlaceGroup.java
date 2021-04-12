package com.wearetogether.v2.ui.holders.group;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
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

public class HolderItemPlaceGroup extends HolderPlaceGroup {

    private final View view_avatar;
    private final View view_icon;
    private final ImageView image_view_avatar;
    private final ViewPager view_pager;
    private final ImageView image_view_menu;
    private final TextView text_view_name;
    private final TextView text_view_title;
    private final TextView text_view_description;
    private final Adapter adapter;
    private Long user_unic;
    private String title;
    private int is_remove;
    private int disable_comments;

    public HolderItemPlaceGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);

        view_visiters = itemView.findViewById(R.id.view_visiters);

        view_avatar = itemView.findViewById(R.id.view_avatar);
        view_icon = itemView.findViewById(R.id.view_icon);

        image_view_avatar = itemView.findViewById(R.id.image_view_avatar);
        view_pager = itemView.findViewById(R.id.view_pager);
        view_pager.setVisibility(View.GONE);
        adapter = new Adapter(this);
        view_pager.setAdapter(adapter);
        image_view_menu = itemView.findViewById(R.id.image_view_menu);

        text_view_name = itemView.findViewById(R.id.text_view_name);
        text_view_title = itemView.findViewById(R.id.text_view_title);
        text_view_description = itemView.findViewById(R.id.text_view_description);

        text_view_title.setOnClickListener(onClickToUser());
        itemView.findViewById(R.id.view_avatar).setOnClickListener(onClickToUser());
        text_view_name.setOnClickListener(onClickToPlace());
        view_icon.setOnClickListener(onClickToPlace());
        view_pager.setOnClickListener(onClickToPlace());

        if (activity instanceof OptionActivity) {
            final OptionActivity optionListener = (OptionActivity) activity;
            image_view_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(activity, optionListener, place_unic, user_unic);
                }
            });
            if (activity instanceof ProfileActivity) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showMenu(activity, optionListener, place_unic, user_unic);
                        return true;
                    }
                });
            }
        }
    }

    public View.OnClickListener onClickToPlace() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long uUnic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
                if (user_unic.equals(uUnic) && adapterGroup.cls == ProfileActivity.class) {
                    App.GoToFormPlace(activity, place_unic, adapterGroup.cls);
                } else {
                    App.GoToPlace(activity, place_unic, adapterGroup.cls);
                }
            }
        };
    }

    private View.OnClickListener onClickToUser() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.GoToUser(activity, user_unic, adapterGroup.cls);
            }
        };
    }

    private void showMenu(FragmentActivity activity, OptionActivity optionListener, Long place_unic, Long user_unic) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", Consts.TYPE_PLACE);
        bundle.putInt("disable_comments", disable_comments);
        bundle.putString("title", title);
        bundle.putLong("place_unic", place_unic);
        bundle.putLong("user_unic", user_unic);
        bundle.putInt("is_remove", is_remove);
        bundle.putInt("position", getAdapterPosition());
        optionListener.showMenu(bundle, adapterGroup.cls);
    }

    @Override
    public void bind(DataGroup item) {
        super.bind(item);

        this.place_unic = item.unic;
        this.user_unic = item.user_unic;
        this.title = item.title;
        this.is_remove = item.is_remove;
        this.disable_comments = item.disable_comments;
        view_avatar.setVisibility((item.bitmapAvatar == null) ? View.GONE : View.VISIBLE);
        if (item.user_name == null || item.user_name.equals("")) {
            text_view_name.setVisibility(View.GONE);
        } else {
            text_view_name.setText(item.user_name);
            text_view_name.setVisibility(View.VISIBLE);
        }
        if (item.title == null || item.title.equals("")) {
            text_view_title.setVisibility(View.GONE);
        } else {
            text_view_title.setText(item.title);
            text_view_title.setVisibility(View.VISIBLE);
        }

        Spanned description = BuildPlaceDescription(activity, item);

        if (description == null) {
            text_view_description.setVisibility(View.GONE);
        } else {
            text_view_description.setText(description);
            text_view_description.setVisibility(View.VISIBLE);
        }

        if (item.bitmapList.size() > 0) {
            adapter.update(item.bitmapList);
            view_icon.setVisibility(View.VISIBLE);
            view_pager.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = view_pager.getLayoutParams();
            params.height = adapterGroup.sizeImage;
            view_pager.setLayoutParams(params);
        } else {
            adapter.update(new ArrayList<>());
            view_icon.setVisibility(View.GONE);
        }
        Bitmap bitmap = null;
        if (item.bitmapAvatar != null) {
            bitmap = item.bitmapAvatar;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(item.bitmapAvatar, adapterGroup.sizeAvatar, adapterGroup.sizeAvatar, false);
            if (scaledBitmap != null) {
                bitmap = scaledBitmap;
                Bitmap roundedBitmap = App.GetRoundedCornerBitmap(scaledBitmap);
                if (roundedBitmap != null) {
                    bitmap = roundedBitmap;
                }
            }
        }
        if (bitmap != null) {
            image_view_avatar.setImageBitmap(bitmap);
        } else {
            image_view_avatar.setBackgroundResource(R.drawable.baseline_account_circle_black_18dp);
        }
    }

    @Override
    public void OnVisitAnonymous(boolean is_anonymous) {
        checkbox_anonymous_visit.setChecked(is_anonymous);
    }

    @Override
    public void OnVisitUser(boolean is_user) {
        checkbox_user_visit.setChecked(is_user);
    }

    class Adapter extends PagerAdapter {

        private HolderItemPlaceGroup holder;

        public Adapter(HolderItemPlaceGroup holder) {
            this.holder = holder;
        }

        public List<Bitmap> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (items.get(position) != null) imageView.setImageBitmap(Bitmap.createScaledBitmap(items.get(position), adapterGroup.sizeImage, adapterGroup.sizeImage, false));
            imageView.setOnClickListener(holder.onClickToPlace());
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void update(List<Bitmap> bitmapList) {
            System.out.println("CELL UPDATE ADAPTER " + items.size());
            this.items.clear();
            this.items.addAll(bitmapList);
            notifyDataSetChanged();
            System.out.println("CELL UPDATE ADAPTER " + items.size());
        }
    }
}

