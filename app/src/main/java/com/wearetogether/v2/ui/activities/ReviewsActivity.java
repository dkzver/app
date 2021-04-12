package com.wearetogether.v2.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Voice;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.review.Remove;
import com.wearetogether.v2.app.review.Save;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.listeners.VoiceListener;
import com.wearetogether.v2.utils.ToastUtils;
import com.wearetogether.v2.viewmodel.ReviewsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements VoiceListener {

    public ReviewsViewModel reviewsViewModel;
    public AdapterGroup adapterGroup;
    private RecyclerView recycler_view_item;
    public static HashMap<String, List<Comment>> CommentMap;
    private EditText edit_text;
    private View view_replay;
    private TextView text_view_replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        final ReviewsActivity activity = this;
        edit_text = findViewById(R.id.edit_text);
        View image_view_voice = findViewById(R.id.image_view_voice);
        image_view_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice.Voice(activity, Consts.REQUEST_VOICE_COMMENT);
            }
        });
        Long item_unic = null;
        Integer type = Consts.TYPE_PLACE;
        Intent intent = getIntent();
        if (intent != null) {
            String string_unic = intent.getStringExtra(Consts.UNIC);
            if (string_unic != null) {
                item_unic = Long.parseLong(string_unic);
            } else {
                item_unic = null;
            }
            System.out.println("unic " + String.valueOf(item_unic));
            String string_type = intent.getStringExtra("type");
            if (string_type != null) {
                type = Integer.parseInt(string_type);
            }
        }

        view_replay = findViewById(R.id.view_replay);
        text_view_replay = findViewById(R.id.text_view_replay);
        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        reviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        adapterGroup = new AdapterGroup(this, ReviewsActivity.this, ReviewsActivity.class);
        recycler_view_item.setAdapter(adapterGroup);
        View button_send = findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.SUser != null) {
                    String author_name = getReviewsViewModel().replayUserNameMutableLiveData.getValue();
                    Long author_unic = getReviewsViewModel().replayUnicMutableLiveData.getValue();
                    Long parent_unic = getReviewsViewModel().parentUnicMutableLiveData.getValue();
                    if (parent_unic == null) {
                        parent_unic = Long.parseLong("0");
                    }
                    String review = null;
                    if (edit_text.getText() != null) {
                        review = String.valueOf(edit_text.getText());
                    }
                    if (review != null && review.length() >= 1) {
                        Save.Start(activity, review, parent_unic, author_name == null ? "" : author_name, author_unic == null ? 0 : author_unic);
                    } else {
                        ToastUtils.Short(getApplicationContext(), getString(R.string.error_comment));
                    }
                }
            }
        });


        if (savedInstanceState == null && item_unic != null) {
            reviewsViewModel.bind(this, item_unic, type);
        }
        reviewsViewModel.mutableLiveData.observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                CommentMap = GetFromListToMap(comments);
                List<Comment> commentList = CommentMap.get("0");
                List<DataGroup> dataGroups = new ArrayList<>();
                dataGroups.add(new DataGroup().Appbar(getString(R.string.title_reviews), ReviewsActivity.class));
                if (commentList != null && commentList.size() > 0) {
                    Integer type = reviewsViewModel.typeMutableLiveData.getValue();
                    if (type == null) {
                        type = Consts.TYPE_PLACE;
                    }
                    for (Comment comment : commentList) {
                        dataGroups.add(new DataGroup().Comment(comment, type));
                    }
                } else {
                    dataGroups.add(new DataGroup(getString(R.string.dont_comments), DataGroup.TYPE_TEXT));
                }
                adapterGroup.update(dataGroups, null);
            }
        });
        reviewsViewModel.replayUnicMutableLiveData.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                view_replay.setVisibility(aLong == null ? View.GONE : View.VISIBLE);
                if (aLong != null && reviewsViewModel.replayUserNameMutableLiveData.getValue() != null) {
                    String user_name = reviewsViewModel.replayUserNameMutableLiveData.getValue();
                    text_view_replay.setText(getString(R.string.replay_to) + " " + Html.fromHtml("<b>" + user_name + "<b/>"));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        Voice.Result(this, requestCode, resultCode, data, Consts.REQUEST_VOICE_COMMENT);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Long unic = getReviewsViewModel().replayUnicMutableLiveData.getValue();
        if (unic != null) {
            getReviewsViewModel().replayUnicMutableLiveData.setValue(null);
        } else {
            back();
        }
    }

    public void back() {
        super.onBackPressed();
    }

    @Override
    public void OnSetVoice(int code, String spokenText) {
        if (edit_text != null) {
            edit_text.setText(spokenText);
        }
    }

    public static HashMap<String, List<Comment>> GetFromListToMap(List<Comment> commens) {
        HashMap<String, List<Comment>> commentMap = new HashMap<>();
        for (Comment comment : commens) {
            List<Comment> commentList;
            comment.key = String.valueOf(comment.parent);
            if (commentMap.containsKey(String.valueOf(comment.parent))) {
                commentList = commentMap.get(String.valueOf(comment.parent));
                if (commentList != null) {
                    commentList.add(comment);
                    commentMap.put(String.valueOf(comment.parent), commentList);
                }
            } else {
                commentList = new ArrayList<>();
                commentList.add(comment);
                commentMap.put(String.valueOf(comment.parent), commentList);
            }
        }
        return commentMap;
    }

    public void moveToAuthor(Long user_unic) {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra("cls", ReviewsActivity.class);
        intent.putExtra(Consts.UNIC, String.valueOf(user_unic));
        startActivity(intent);
    }

    public void setReplay(String user_name, Long author_unic, long unic) {
        getReviewsViewModel().replayUserNameMutableLiveData.setValue(user_name);
        getReviewsViewModel().replayUnicMutableLiveData.setValue(author_unic);
        getReviewsViewModel().parentUnicMutableLiveData.setValue(unic);
    }

    public ReviewsViewModel getReviewsViewModel() {
        if (reviewsViewModel == null) {
            reviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        }
        return reviewsViewModel;
    }


    public void add(long parent, Comment comment) {
        Integer type = getReviewsViewModel().typeMutableLiveData.getValue();
        if (type != null) {
            adapterGroup.addComment(parent, comment, type);
            edit_text.setText("");
            getReviewsViewModel().replayUnicMutableLiveData.setValue(null);
            ToastUtils.Short(getApplicationContext(), getString(R.string.action_add_comment));
            App.HideKeyboard(getApplicationContext(), new View[] {edit_text});
        }
    }

    public void remove(long unic, String key, int position) {
        Remove.Start(this, ReviewsActivity.this, unic, key, position);
    }
}
