package com.wearetogether.v2.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.ui.activities.ReviewsActivity;
import com.wearetogether.v2.ui.holders.group.*;
import com.wearetogether.v2.utils.DimensionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterGroup extends RecyclerView.Adapter<HolderBaseGroup> {

    public final FragmentActivity activity;
    public final Context context;
    public int sizeAvatar;
    public int small_icon;
    public int litle_icon;
    public int sizeImage;
    public List<DataGroup> items = new ArrayList<>();
    public Class<?> cls;
    public int sizeProfileAvatar;
    public String[] array_categories;
    public int rightOffsetInterest;
    public String url_base;

    public AdapterGroup(FragmentActivity activity, Context context, Class<?> cls) {
        this.cls = cls;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.activity = activity;
        this.context = context;
        this.sizeAvatar = DimensionUtils.Transform(Consts.SIZE_ICON, activity.getApplicationContext());
        this.sizeImage = displaymetrics.widthPixels;
        this.small_icon = DimensionUtils.Transform(58, activity.getApplicationContext());
        this.litle_icon = DimensionUtils.Transform(36, activity.getApplicationContext());
        this.sizeProfileAvatar = DimensionUtils.Transform(Consts.SIZE_ICON, activity.getApplicationContext());
        this.rightOffsetInterest = DimensionUtils.Transform(4, activity.getApplicationContext());
        this.url_base = activity.getString(R.string.url_base);
    }

    @Override
    public HolderBaseGroup onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case DataGroup.TYPE_PREPARE_PICTURE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_prepare_picture_message_group, parent, false);
                return new HolderMessagePreparePictureGroup(view, this);
            case DataGroup.WRITE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_write_message_group, parent, false);
                return new HolderWriteMessageGroup(view, this);
            case DataGroup.TYPE_ROOM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_room_group, parent, false);
                return new HolderRoomGroup(view, litle_icon, this);
            case DataGroup.TYPE_MESSAGE_LEFT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_message_left, parent, false);
                return new HolderMessageGroup(view, this);
            case DataGroup.TYPE_MESSAGE_RIGHT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_message_right, parent, false);
                return new HolderMessageGroup(view, this);
            case DataGroup.TYPE_MESSAGE_PICTURE_LEFT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_picture_message_left, parent, false);
                return new HolderMessagePictureGroup(view, this);
            case DataGroup.TYPE_MESSAGE_PICTURE_RIGHT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_picture_message_right, parent, false);
                return new HolderMessagePictureGroup(view, this);
            case DataGroup.TYPE_MESSAGE_DATE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_message_date, parent, false);
                return new HolderMessageDateGroup(view, this);
            case DataGroup.TYPE_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_comment_group, parent, false);
                return new HolderCommentGroup(view, this);
            case DataGroup.TYPE_PLACE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_place_header_item, parent, false);
                return new HolderPlaceHeaderGroup(view, this);
            case DataGroup.TYPE_USER_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_user_header_item, parent, false);
                return new HolderUserHeaderGroup(view, this);
            case DataGroup.TYPE_GALLERY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_gallery_item, parent, false);
                return new HolderGalleryGroup(view, this);
            case DataGroup.TYPE_USER_PROFILE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_user_profile, parent, false);
                return new HolderUserProfileGroup(view, this);
            case DataGroup.TYPE_USER_PLACE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_user_place, parent, false);
                return new HolderUserPlaceGroup(view, this);
            case DataGroup.TYPE_PLACE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_item_place, parent, false);
                return new HolderItemPlaceGroup(view, this);
            case DataGroup.TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_item_user, parent, false);
                return new HolderItemUserGoup(view, this, sizeAvatar, cls);
            case DataGroup.TYPE_APPBAR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_appbar, parent, false);
                return new HolderAppBarGroup(view, this);
            case DataGroup.TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_header, parent, false);
                return new HolderHeaderGroup(view, this);
            case DataGroup.TYPE_HEADER_OPTIONS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_header_friends, parent, false);
                return new HolderHeaderGroupOptions(view, this, context);
            case DataGroup.TYPE_TEXT:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_group_text, parent, false);
                return new HolderTextGroup(view, this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderBaseGroup holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(List<DataGroup> items, String[] array_categories) {
        this.array_categories = array_categories;
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void update(List<DataGroup> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeComment(long unic, long parent, int position) {
        if (parent == 0) {
            if (position >= 0) {
                this.items.remove(position);
                notifyItemRemoved(position);
            }
        } else {
            List<Comment> commentList = null;
            if (ReviewsActivity.CommentMap.containsKey(String.valueOf(parent))) {
                commentList = ReviewsActivity.CommentMap.get(String.valueOf(parent));
            } else {
                commentList = new ArrayList<>();
            }
            if (commentList != null) {
                int findIndexRemove = -1;
                for (int x = 0; x < commentList.size(); x++) {
                    if (commentList.get(x).parent == parent) {
                        findIndexRemove = x;
                        break;
                    }
                }
                commentList.remove(findIndexRemove);
                ReviewsActivity.CommentMap.put(String.valueOf(parent), commentList);
            }
            notifyDataSetChanged();
        }
    }

    public void addComment(long parent, Comment comment, int type) {
        comment.isNew = true;
        DataGroup dataGroup = null;
        int findIndexRemove = -1;
        for (int i = 0; i < items.size(); i++) {
            dataGroup = items.get(i);
            if (dataGroup.type == DataGroup.TYPE_TEXT) {
                findIndexRemove = i;
                break;
            }
        }
        if (findIndexRemove >= 0) {
            this.items.remove(findIndexRemove);
        }

        if (parent == 0) {
            this.items.add(new DataGroup().Comment(comment, type));
            notifyDataSetChanged();
        } else {
            List<Comment> commentList = null;
            if (ReviewsActivity.CommentMap.containsKey(String.valueOf(parent))) {
                commentList = ReviewsActivity.CommentMap.get(String.valueOf(parent));
            } else {
                commentList = new ArrayList<>();
            }
            if (commentList != null) {
                commentList.add(comment);
                ReviewsActivity.CommentMap.put(String.valueOf(parent), commentList);
            }
            notifyDataSetChanged();
        }
    }

    public void updateHeader() {
        DataGroup dataGroup = items.get(0);
        items.set(0, dataGroup);
        notifyItemChanged(0);
    }

    public boolean selectedMessage(String unic, boolean is) {
        int position = -1;
        for(int x = 0; x < items.size(); x++) {
            if(items.get(x).type == DataGroup.TYPE_MESSAGE_RIGHT) {
                if(items.get(x).message_unic.equals(unic)) {
                    position = x;
                    break;
                }
            }
        }
        if(position >= 0) {
            DataGroup dataGroup = items.get(position);
            dataGroup.selected = is ? 1 : 0;
            items.set(position, dataGroup);
            notifyItemChanged(position);
            return true;
        }
        return false;
    }

    public void showWrite(String user_name, Long user_unic) {
        int index = -1;
        for(int x = 0; x < items.size(); x++) {
            if(items.get(x).type == DataGroup.WRITE && items.get(x).user_unic == user_unic) {
                index = x;
            }
        }
        if(index == -1) {
            items.add(new DataGroup().Write(user_name, user_unic));
            notifyItemInserted(items.size());
        }
    }

    public void hideWrite(long user_unic) {
        int index = -1;
        for(int x = 0; x < items.size(); x++) {
            if(items.get(x).type == DataGroup.WRITE && items.get(x).user_unic == user_unic) {
                index = x;
            }
        }
        if(index >= 0) {
            items.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void toggleWrite(String user_name, String user_unic, Boolean isInput) {
        if(isInput) {
            showWrite(user_name, Long.parseLong(user_unic));
        } else {
            hideWrite(Long.parseLong(user_unic));
        }
    }

    public void removeMessage(String message_unic) {
        int index = -1;
        for(int x = 0; x < items.size(); x++) {
            if(items.get(x).type == DataGroup.TYPE_MESSAGE_LEFT || items.get(x).type == DataGroup.TYPE_MESSAGE_RIGHT) {
                if(items.get(x).message_unic.equals(message_unic)) {
                    index = x;
                    break;
                }
            }
        }
        if(index >= 0) {
            items.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void updateProfileIcon(Bitmap bitmap) {
        int findIndex = -1;
        for(int x = 0; x < items.size(); x++) {
            if(items.get(x).type == DataGroup.TYPE_USER_PROFILE) {
                findIndex = x;
                break;
            }
        }
        if(findIndex >= 0) {
            DataGroup dataGroup = items.get(findIndex);
            dataGroup.bitmapAvatar = bitmap;
            items.set(findIndex, dataGroup);
            notifyItemChanged(findIndex);
        }
    }

    public void readMessage(String message_unic) {
        int index = -1;
        for(int x = 0; x < items.size(); x++) {
            if(items.get(x).type == DataGroup.TYPE_MESSAGE_RIGHT) {
                if(items.get(x).message_unic.equals(message_unic)) {
                    index = x;
                    break;
                }
            }
        }
        if(index >= 0) {
            DataGroup dataGroup = items.get(index);
            dataGroup.is_read = "1";
            items.set(index, dataGroup);
            notifyItemChanged(index);
        }
    }

    public int prepareMessage(String icon, String message_unic, long user_unic) {
        int index = items.size();
        items.add(new DataGroup().PreparePicture(icon, message_unic, user_unic));
        notifyItemInserted(index);
        return index;
    }

    public void updatePicture(int index, String message_unic, String user_unic, String icon, String small, String original) {
        items.set(index, new DataGroup().MessagePictureRight(icon, small, original, message_unic, user_unic));
        notifyItemChanged(index);
    }
}