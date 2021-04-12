package com.wearetogether.v2.app.user;

import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.UserInterest;
import com.wearetogether.v2.ui.activities.InterestsActivity;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.*;

public class EditInterest {

    private static boolean isEdit = false;

    public static void Start(final InterestsActivity activity) {
        final HashMap<Integer, Boolean> mapSelected = activity.getViewModel().selectedMutableLiveData.getValue();
        if (mapSelected != null && App.SUser != null) {
            System.out.println("mapSelected " + mapSelected);
            System.out.println(mapSelected);
            System.out.println(mapSelected.keySet());
            System.out.println(mapSelected.values());
            final Long user_unic = Long.parseLong(App.SUser.unic);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<UserInterest> newUserInterests = new ArrayList<>();
                    Interest interest = null;
                    for (Integer key : mapSelected.keySet()) {
                        if(mapSelected.get(key) != null) {
                            if(mapSelected.get(key)) {
                                interest = App.Database.daoInterest().getById(key);
                                if (interest != null) {
                                    newUserInterests.add(new UserInterest(user_unic, interest.id));
                                }
                            }
                        }
                    }
                    List<UserInterest> oldUserInterests = App.Database.daoUserInterest().get(user_unic);
                    isEdit = isEdit(oldUserInterests, newUserInterests);
                    System.out.println("EditInterest " + isEdit);
                    if(isEdit) {
                        App.Database.daoUserInterest().delete(user_unic);
                        for(UserInterest userInterest : newUserInterests) {
                            System.out.println("userInterest: " + userInterest.interest_id);
                            App.Database.daoUserInterest().insert(userInterest);
                        }
                        ItemLog log = App.Database.daoLog().getLog(Consts.LOG_ACTION_UPDATE_USER_INTERESTS);
                        if (log == null) {
                            log = new ItemLog();
                            log.unic = Calendar.getInstance().getTimeInMillis();
                            log.user_id = App.SUser.getUserId();
                            log.item_unic = Long.parseLong(App.SUser.unic);
                            log.action = Consts.LOG_ACTION_UPDATE_USER_INTERESTS;
                            App.Database.daoLog().insert(log);
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isEdit) {
                                PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                                App.IsUpdate = true;
                            }
                            activity.back();
                        }
                    });
                }
            }).start();
        } else {
            activity.back();
        }
    }

    public static boolean compareList(List<Integer> list1, List<Integer> list2){
        if(list1 == null) return false;
        if(list2 == null) return false;
        return list1.containsAll(list2) && list1.size() == list2.size();
    }

    private static boolean isEdit(List<UserInterest> oldUserInterests, List<UserInterest> newUserInterests) {
        List<Integer> list1 = new ArrayList<>();
        for(UserInterest oldInterest : oldUserInterests) {
            list1.add(oldInterest.interest_id);
        }
        List<Integer> list2 = new ArrayList<>();
        for(UserInterest newInterest : newUserInterests) {
            list2.add(newInterest.interest_id);
        }
        System.out.println("list1");
        System.out.println(list1);
        System.out.println("list2");
        System.out.println(list2);

        boolean equals = compareList(list1, list2);
        System.out.println("equals " + equals);
        return !equals;
    }
}
