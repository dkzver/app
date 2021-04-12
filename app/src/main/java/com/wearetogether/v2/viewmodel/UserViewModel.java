package com.wearetogether.v2.viewmodel;

import android.graphics.Bitmap;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.data.DataItem;
import com.wearetogether.v2.app.download.DownloadImages;
import com.wearetogether.v2.app.download.DownloadPlaces;
import com.wearetogether.v2.app.download.DownloadUsers;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.SMediaItem;
import com.wearetogether.v2.smodel.SPlace;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.ObjectUtils;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    public MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DataGroup>> listMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Long> unicMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> voteMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> friendMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> authorMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> showPhotoMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Bitmap> avatarMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<MediaItem>> mediaItemMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String[]> arrayCategoriesMutableLiveData = new MutableLiveData<>();

    public void bind(FragmentActivity activity, final long unic, final boolean isOnline) {
        authorMutableLiveData.setValue(false);
        showPhotoMutableLiveData.setValue(false);
        mediaItemMutableLiveData.setValue(new ArrayList<>());
        final long user_unic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
        final String url_base = activity.getString(R.string.url_base);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> listCategories = App.Database.daoCategory().getAll();

                final User user = (isOnline) ? UserVersion.Execute(unic, activity, url_base) : App.Database.daoUser().get(unic);
                if(user == null) return;
                System.out.println("user");
                System.out.println(user);

                Bitmap bitmapAvatar = FileUtils.GetBitmap(user.avatar);
                final List<MediaItem> mediaItemList = App.Database.daoMediaItem().getListOrderByPositionStar(unic);

                Vote vote = App.Database.daoVote().get(unic, user_unic);
                if(vote != null) {
                    user.vote = vote.vote;
                } else {
                    user.vote = 0;
                }
                Friend friend = App.Database.daoFriends().getByUser(unic);
                if(friend != null) {
                    user.friend = friend.type;
                } else {
                    user.friend = 0;
                }
                List<Place> placeList = App.Database.daoPlace().getByUserUnic(unic, 0);
                List<DataGroup> dataGroups = new ArrayList<>();
                if(placeList == null || placeList.size() == 0) {
                    user.count_place = 0;
                    dataGroups.add(new DataGroup(activity.getString(R.string.not_places), DataGroup.TYPE_TEXT));
                } else {
                    user.count_place = placeList.size();
                    dataGroups.add(new DataGroup(activity.getString(R.string.user_places), DataGroup.TYPE_HEADER));
                    for(Place place : placeList) {
                        dataGroups.add(new DataGroup().Place(ObjectUtils.Build(place, user_unic), user_unic));
                    }
                }
                final List<Interest> interestList = App.Database.daoUserInterest().getAll(unic);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        voteMutableLiveData.setValue(user.vote);
                        friendMutableLiveData.setValue(user.friend);
                        authorMutableLiveData.setValue(user_unic == user.unic);
                        List<DataGroup> posts = new ArrayList<>();
                        String[] array_interest = ListUtils.GetInterests(interestList, activity.getApplicationContext());
                        String[] array_sex = activity.getResources().getStringArray(R.array.sex_array);
                        posts.add(new DataGroup().HeaderUser(null,
                                bitmapAvatar,
                                user,
                                array_sex, array_interest)
                        );
                        posts.add(new DataGroup().Gallery());
                        posts.addAll(dataGroups);
                        mediaItemMutableLiveData.setValue(mediaItemList);
                        listMutableLiveData.setValue(posts);
                        mutableLiveData.setValue(user);
                        arrayCategoriesMutableLiveData.setValue(ListUtils.GetCategories(listCategories, activity.getApplicationContext()));
                    }
                });
            }
        }).start();
    }
}
