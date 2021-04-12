package com.wearetogether.v2.utils;

import android.content.Context;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.Category;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.database.model.Status;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static String[] GetInterests(List<Interest> interests, Context context) {
        if(interests.size() == 0) return new String[] {};
        List<String> list = new ArrayList<>();
        for (Interest interest : interests) {
            list.add(interest.title);
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public static String[] GetCategories(List<Category> categories, Context context) {
        if(categories.size() == 0) return new String[] {};
        List<String> list = new ArrayList<>();
        list.add(context.getString(R.string.not_category));
        for (Category category : categories) {
            list.add(category.title);
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public static String[] GetStatuses(List<Status> statuses, Context context) {
        if(statuses.size() == 0) return new String[] {};
        List<String> list = new ArrayList<>();
        list.add(context.getString(R.string.custom_status));
        for (Status status : statuses) {
            list.add(status.title);
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public static String Implode(String separator, List<Long> list) {
        String text = "";
        if (list.size() == 1) {
            text = String.valueOf(list.get(0));
        } else {
            for(int x = 0; x < list.size() - 1; x++) {
                text+=list.get(x)+separator;
            }
            text+=list.get(list.size() - 1);
        }
        return text;
    }
}
