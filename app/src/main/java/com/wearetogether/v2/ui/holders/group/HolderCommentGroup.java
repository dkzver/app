package com.wearetogether.v2.ui.holders.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.ui.activities.ReviewsActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HolderCommentGroup extends HolderBaseGroup implements View.OnClickListener {

    private View view;
    //    private View view_comment;
    private TextView text_view_date;
    private TextView text_view_author;
    private TextView text_view_replay;
    private TextView text_view_comment;
    private View view_replay;
    private View image_view;
    private ImageView image_view_avatar;
    private RecyclerView recycler_view_item;
    private TextView button_replay;
    private ImageView image_view_like;
    private ImageView image_view_remove;
    private View view_like;
    private View view_remove;
    private AdapterGroup adapter;
    private String user_name;
    private Long unic;
    private Integer vote;
    private Long author_unic;
    private Long user_unic;
    private int type_comment;
    private int position;
    private String key;

    public HolderCommentGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        view = itemView.findViewById(R.id.view);
        view_replay = itemView.findViewById(R.id.view_replay);
        text_view_date = itemView.findViewById(R.id.text_view_date);
        text_view_author = itemView.findViewById(R.id.text_view_author);
        text_view_author.setOnClickListener(this);
        text_view_replay = itemView.findViewById(R.id.text_view_replay);
        text_view_replay.setOnClickListener(this);
        text_view_comment = itemView.findViewById(R.id.text_view_comment);
        image_view = itemView.findViewById(R.id.image_view);
        image_view_avatar = itemView.findViewById(R.id.image_view_avatar);
        recycler_view_item = (RecyclerView) itemView.findViewById(R.id.recycler_view_item);
        button_replay = itemView.findViewById(R.id.button_replay);
        image_view_like = itemView.findViewById(R.id.image_view_like);
        image_view_remove = itemView.findViewById(R.id.image_view_remove);
        view_like = itemView.findViewById(R.id.view_like);
        view_remove = itemView.findViewById(R.id.view_remove);
        view_remove.setVisibility(View.GONE);
        button_replay.setOnClickListener(this);
        image_view_like.setOnClickListener(this);
        image_view_remove.setOnClickListener(this);
        recycler_view_item.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recycler_view_item.setLayoutManager(layoutManager);
    }

    private void updateFavorite(int vote) {
        this.vote = vote;
        if(this.vote == 1) {
            image_view_like.setBackgroundResource(R.drawable.baseline_favorite_black_18dp);
        } else {
            image_view_like.setBackgroundResource(R.drawable.baseline_favorite_border_black_18dp);
        }
    }

    @Override
    public void bind(DataGroup item) {
        this.user_name = item.comment.user_name;
        this.position = getAdapterPosition();
        this.key = String.valueOf(item.comment.parent);
        this.unic = item.comment.unic;
        this.author_unic = item.comment.user_unic;
        this.user_unic = item.comment.user_unic;
        this.type_comment = item.type_comment;
        if(item.comment.user_unic == Long.parseLong(App.SUser.unic) && item.comment.isNew) {
            view_like.setVisibility(View.GONE);
            view_remove.setVisibility(View.VISIBLE);
        } else {
            view_like.setVisibility(View.VISIBLE);
            view_remove.setVisibility(View.GONE);
        }
        updateFavorite(item.comment.vote);
        if (ReviewsActivity.CommentMap.get(String.valueOf(item.comment.unic)) != null) {
            List<Comment> commentList = ReviewsActivity.CommentMap.get(String.valueOf(item.comment.unic));
            if (commentList != null && commentList.size() > 0) {
                view.setLayoutParams(App.GetChildrenParams(activity.getApplicationContext()));
                adapter = new AdapterGroup(activity, context, ReviewsActivity.class);
                recycler_view_item.setAdapter(adapter);
                List<DataGroup> dataGroups = new ArrayList<>();
                for (Comment comment : commentList) {
                    dataGroups.add(new DataGroup().Comment(comment, type_comment));
                }
                adapter.update(dataGroups, null);
            }
        }
        String[] array = App.TransformDateTime(item.comment.comment_datetime);
        text_view_date.setText(array[0] + " " + array[1]);
        text_view_author.setText(item.comment.user_name);
        if (!item.comment.replay_user_name.equals("")) {
            text_view_replay.setText(item.comment.replay_user_name);
            view_replay.setVisibility(View.VISIBLE);
        } else {
            view_replay.setVisibility(View.GONE);
        }
        text_view_replay.setTag(item.comment.replay_user_name);
        text_view_comment.setText(item.comment.text);
        if (item.comment.user_avatar != null && !item.comment.user_avatar.equals("")) {
            image_view.setVisibility(View.VISIBLE);
            int px;
            if(!key.equals("0")) {
                image_view_avatar.getLayoutParams().height = adapterGroup.litle_icon;
                image_view_avatar.getLayoutParams().width = adapterGroup.litle_icon;
                px = adapterGroup.litle_icon;
            } else {
                image_view_avatar.getLayoutParams().height = adapterGroup.small_icon;
                image_view_avatar.getLayoutParams().width = adapterGroup.small_icon;
                px = adapterGroup.small_icon;
            }
            view.requestLayout();
            Bitmap bitmap = App.MapCache.get(item.comment.user_avatar);
            if(bitmap == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = FileUtils.GetBitmap(item.comment.user_avatar);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(bitmap != null) {
                                    Bitmap scale = Bitmap.createScaledBitmap(bitmap, px, px, false);
                                    image_view_avatar.setImageBitmap(scale);
                                    image_view.setVisibility(View.VISIBLE);
                                    App.MapCache.put(item.comment.user_avatar, scale);
                                } else {
                                    image_view.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }).start();
            } else {
                image_view_avatar.setImageBitmap(bitmap);
                image_view.setVisibility(View.VISIBLE);
            }
        } else {
            image_view.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        long userUnic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
        switch (v.getId()) {
            case R.id.text_view_author:
//                if (user_unic != null && user_unic != userUnic && activity instanceof AuthorActivity) {
//                    AuthorActivity authorActivity = (AuthorActivity) activity;
//                    authorActivity.moveToAuthor(user_unic);
//                }
                break;
            case R.id.text_view_replay:
                if (this.author_unic != null && this.author_unic != userUnic && activity instanceof ReviewsActivity) {
                    ReviewsActivity reviewsActivity = (ReviewsActivity) activity;
                    reviewsActivity.moveToAuthor(author_unic);
                }
                break;
            case R.id.button_replay:
                if (this.unic != null && (this.author_unic != null && this.author_unic != userUnic) && recycler_view_item != null && activity instanceof ReviewsActivity) {
                    ReviewsActivity reviewsActivity = (ReviewsActivity) activity;
                    reviewsActivity.setReplay(this.user_name, this.author_unic, this.unic);
                }
                break;
            case R.id.image_view_remove:
                System.out.println("attemp edit comment");
                if(author_unic == userUnic && activity instanceof ReviewsActivity) {
                    ReviewsActivity reviewsActivity = (ReviewsActivity) activity;
                    reviewsActivity.remove(this.unic, this.key, this.position);
                }
                break;
            case R.id.image_view_like:
                if (App.SUser != null) {
                    if (this.unic != null && this.vote != null) {
//                        if(this.vote == 1) {
//                            updateFavorite(0);
//                            VoteComment.Start(activity, 0, this.unic, userUnic);
//                        } else {
//                            updateFavorite(1);
//                            VoteComment.Start(activity, 1, this.unic, userUnic);
//                        }
                    }
                } else {
//                    App.ShowDialogMessageLoginUser(activity, activity.getString(R.string.please_login_for_like), new DialogMessageLoginUser.OnDialogListener() {
//                        @Override
//                        public void OnLogin() {
//                            goToLogin();
//                        }
//
//                        @Override
//                        public void OnCancel() {
//                            updateFavorite(vote);
//                        }
//                    });
                }
                break;
        }
    }
}
