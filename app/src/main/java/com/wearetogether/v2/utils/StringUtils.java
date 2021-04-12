package com.wearetogether.v2.utils;

import java.util.List;

public class StringUtils {
    public static String GetNames(List<String> names) {
        String users_names = "";
        if(names.size() > 0) {
            users_names = names.get(0);
            if(names.size() > 1) {
                for(int x = 1; x < names.size(); x++) {
                    users_names+=", " + names.get(x);
                }
            }
        }
        return users_names;
    }
}
