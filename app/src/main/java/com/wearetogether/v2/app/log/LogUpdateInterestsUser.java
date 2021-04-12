package com.wearetogether.v2.app.log;

import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.UserInterest;
import com.wearetogether.v2.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class LogUpdateInterestsUser {
    private Long unic;
    private String interests;
    private Long log_unic;

    public static LogUpdateInterestsUser Build(Long user_unic, Long log_unic) {
        LogUpdateInterestsUser log = new LogUpdateInterestsUser();
        log.log_unic = log_unic;
        log.unic = user_unic;
        List<UserInterest> userInterests = App.Database.daoUserInterest().get(user_unic);
        List<Integer> list = new ArrayList<>();
        for(UserInterest userInterest : userInterests) {
            list.add(userInterest.interest_id);
        }
        log.interests = Implode(",", list);
        return log;
    }

    public static String Implode(String separator, List<Integer> list) {
        String text = "";
        if(list.size() > 0) {
            if (list.size() == 1) {
                text = String.valueOf(list.get(0));
            } else {
                for(int x = 0; x < list.size() - 1; x++) {
                    text+=list.get(x)+separator;
                }
                text+=list.get(list.size() - 1);
            }
        }
        return text;
    }
}
