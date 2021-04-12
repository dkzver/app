package com.wearetogether.v2.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.Option;
import com.wearetogether.v2.app.place.Remove;
import com.wearetogether.v2.app.user.Friends;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.ui.dialogs.DialogOptionBottom;
import com.wearetogether.v2.ui.listeners.OptionListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class OptionActivity extends AppCompatActivity implements OptionListener {

    public AlertDialog dialog;
    private DialogOptionBottom dialogOptionBottom;
    private static int type_place1 = 1;
    private static int type_place2 = 2;
    private static int type_place3 = 3;
    private static int type_place4 = 4;
    private static int type_place5 = 5;
    private static int type_place6 = 6;

    /*-
        if (bundle == null) return new ArrayList<>();
        int is_remove = bundle.containsKey("is_remove") ? bundle.getInt("is_remove", 0) : 0;
        int type = bundle.containsKey("type") ? bundle.getInt("type", 0) : 0;
        long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
        long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
        List<Option> options = new ArrayList<>();
        if (type == Consts.TYPE_USER_FRIEND1) {
            options.add(new Option(getString(R.string.cancel_request), 1, 1));
        } else if (type == Consts.TYPE_USER_FRIEND2) {
            options.add(new Option(getString(R.string.cancel_friend), 1, 1));
            options.add(new Option(getString(R.string.accept_friend), 2, 2));
        } else if (type == Consts.TYPE_USER_HEADER) {
            options.add(new Option(getString(R.string.option_commentes), 1, 1));
        } else if (type == Consts.TYPE_PLACE_HEADER) {
            options.add(new Option(getString(R.string.option_commentes), 1, 1));
        } else if (type == Consts.TYPE_PLACE) {
            options.add(new Option(getString(R.string.option_view_place), type_place1, 1));
            options.add(new Option(getString(R.string.option_commentes), type_place2, 2));
            if (App.SUser != null) {
                if (user_unic == Long.parseLong(App.SUser.unic)) {
                    options.add(new Option(getString(R.string.option_edit_place), type_place3, 0));
                    if (is_remove == 0 && cls == ProfileActivity.class) {
                        options.add(new Option(getString(R.string.option_remove_place), type_place4));
                    } else if (is_remove == 1 && cls == BackedActivity.class) {
                        options.add(new Option(getString(R.string.option_restore_place), type_place4, 3));
                    }
                } else {
                    options.add(new Option(getString(R.string.option_complain_place), type_place5));
                    options.add(new Option(getString(R.string.option_add_to_favorite), type_place6, 4));
                }
            }
        } else if (type == Consts.TYPE_USER) {
            options.add(new Option(getString(R.string.option_view_user), 1, 1));
            if (App.SUser != null) {
                if (user_unic != Long.parseLong(App.SUser.unic)) {
                    options.add(new Option(getString(R.string.option_add_to_friend), 2, 2));
                    options.add(new Option(getString(R.string.option_comments), 3, 3));
                    options.add(new Option(getString(R.string.option_complain_user), 4, 4));
                }
            }
        } else if (type == Consts.TYPE_PROFILE) {
            options.add(new Option(getString(R.string.option_friends), 1));
            options.add(new Option(getString(R.string.option_backed), 2));
        } else if (type == Consts.TYPE_BACKED) {
            options.add(new Option(getString(R.string.option_profile), 1));
        return options.stream()
                .sorted(Comparator.comparing(Option::getPosition))
                .collect(Collectors.toList());
        }*/

//    @Override
//    public void clickToOption(int key, Bundle bundle, Class<?> cls) {
//        if (bundle == null) return;
//        final int is_remove = bundle.containsKey("is_remove") ? bundle.getInt("is_remove", 0) : 0;
//        int type = bundle.containsKey("type") ? bundle.getInt("type", 0) : 0;
//        int friend = bundle.containsKey("friend") ? bundle.getInt("friend", 0) : 0;
//        int position = bundle.containsKey("position") ? bundle.getInt("position", 0) : 0;
//        long user_unic = bundle.containsKey("user_unic") ? bundle.getLong("user_unic") : 0;
//        long place_unic = bundle.containsKey("place_unic") ? bundle.getLong("place_unic") : 0;
//        String title = bundle.containsKey("title") ? bundle.getString("title") : "";
//        final FragmentActivity activity = this;
//        long uUnic = App.SUser == null ? 0 : Long.parseLong(App.SUser.unic);
//        if (key == 1 && type == Consts.TYPE_USER_FRIEND1) {
//            showDialogConfirmFriend(key, type, user_unic, friend);
//        } else if (key == 1 && type == Consts.TYPE_USER_FRIEND2) {
//            showDialogConfirmFriend(key, type, user_unic, friend);
//        } else if (key == 2 && type == Consts.TYPE_USER_FRIEND2) {
//            showDialogConfirmFriend(key, type, user_unic, friend);
//        } else if (key == 1 && type == Consts.TYPE_PLACE_HEADER) {
//            App.GoToReviews(activity, place_unic, cls, Consts.TYPE_PLACE);
//        } else if (key == 1 && type == Consts.TYPE_USER_HEADER) {
//            App.GoToReviews(activity, user_unic, cls, Consts.TYPE_USER);
//        } else if (key == 1 && type == Consts.TYPE_PROFILE) {
//            startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
//        } else if (key == 2 && type == Consts.TYPE_PROFILE) {
//            startActivity(new Intent(getApplicationContext(), BackedActivity.class));
//        } if (key == 1 && type == Consts.TYPE_BACKED) {
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//        } else if (key == 1 && type == Consts.TYPE_USER) {
//            App.GoToUser(this, user_unic, cls);
//        } else if (key == type_place1 && type == Consts.TYPE_PLACE) {
//            App.GoToPlace(this, place_unic, cls);
//        } else if (key == type_place2 && uUnic == user_unic && type == Consts.TYPE_PLACE) {
//            App.GoToReviews(this, place_unic, cls, Consts.TYPE_PLACE);
//        } else if (key == type_place3 && uUnic == user_unic && type == Consts.TYPE_PLACE) {
//            App.GoToFormPlace(this, place_unic, cls);
//        } else if (key == type_place4 && uUnic == user_unic && type == Consts.TYPE_PLACE) {
//            showDialogRemovePlace(activity, title, is_remove, place_unic, position);
//        } else if (key == type_place5 && uUnic == user_unic && type == Consts.TYPE_PLACE) {
//            App.GoToFormPlace(this, place_unic, cls);
//        } else if (key == type_place6 && uUnic == user_unic && type == Consts.TYPE_PLACE) {
//            App.GoToFormPlace(this, place_unic, cls);
//        }
//    }

    public void showMenu(Bundle bundle, Class<?> cls) {
        bundle.putSerializable("cls", cls);
        if (dialogOptionBottom == null) {
            dialogOptionBottom = DialogOptionBottom.newInstance(bundle, cls);
        } else {
            dialogOptionBottom.setArguments(bundle);
        }
        if (!dialogOptionBottom.isAdded()) {
            dialogOptionBottom.show(getSupportFragmentManager(), "options");
        }
    }

    public void hideOptions() {
        if(dialogOptionBottom != null) {
            dialogOptionBottom.dismiss();
        }
    }
}
