package com.wearetogether.v2.viewmodel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.database.model.Vote;

import java.util.List;

public class ReviewsViewModel extends ViewModel {

    public MutableLiveData<List<Comment>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> typeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> replayUnicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> parentUnicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> itemUnicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> replayUserNameMutableLiveData = new MutableLiveData<>();

    public void bind(final FragmentActivity activity, final long item_unic, final int type) {
        System.out.println("Review bind " + item_unic);
        System.out.println("Review bind " + type);
        itemUnicMutableLiveData.setValue(item_unic);
        typeMutableLiveData.setValue(type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Comment> commentes = App.Database.daoComment().getByItemUnic(item_unic);
                for(Comment comment : commentes) {
                    Vote vote = App.Database.daoVote().get(comment.unic);
                    if (vote != null) {
                        comment.vote = vote.vote;
                    } else {
                        comment.vote = 0;
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mutableLiveData.setValue(commentes);
                    }
                });
            }
        }).start();
    }
}
