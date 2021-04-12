package com.wearetogether.v2.utils;

import android.graphics.Bitmap;
import com.wearetogether.v2.App;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.SShowPlace;
import com.wearetogether.v2.ui.map.MarkerItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ObjectUtils {
    public static Place Build(Place item, long user_unic) {
        if (App.MapCache == null) {
            App.InitCache();
        }
        item.mediaItemList = App.Database.daoMediaItem().getListOrderByPositionStar(item.unic);
        Visit visit = App.Database.daoVisit().get(item.unic, user_unic);
        Vote vote = App.Database.daoVote().get(item.unic, user_unic);
        Book book = App.Database.daoBook().get(item.unic);
        if (visit != null) {
            item.visit = visit.visit;
        } else {
            item.visit = 0;
        }
        if (vote != null) {
            item.vote = vote.vote;
        } else {
            item.vote = 0;
        }
        item.save = book == null ? 0 : 1;
        List<User> userVisiters = App.Database.daoVisit().getUsersByPlace(item.unic, 1);
        item.visiters = new ArrayList<>();
        if (userVisiters.size() > 0) {
            Bitmap bitmap;
            for (User visiter : userVisiters) {
                bitmap = App.MapCache.get(visiter.avatar);
                if (bitmap == null) {
                    bitmap = FileUtils.GetBitmap(visiter.avatar);
                    if (bitmap != null) App.MapCache.put(visiter.avatar, bitmap);
                }
                item.visiters.add(new DataUser(bitmap, visiter.name, visiter.unic));
            }
        }
        return item;
    }

    public static SShowPlace Build(SShowPlace item, long user_unic) {
        if (App.MapCache == null) {
            App.InitCache();
        }
        item.mediaItemList = App.Database.daoMediaItem().getListOrderByPositionStar(Long.parseLong(item.unic));
        Visit visit = App.Database.daoVisit().get(Long.parseLong(item.unic), user_unic);
        Vote vote = App.Database.daoVote().get(Long.parseLong(item.unic), user_unic);
        Book book = App.Database.daoBook().get(Long.valueOf(item.unic));
        if (visit != null) {
            item.visit = visit.visit;
        } else {
            item.visit = 0;
        }
        if (vote != null) {
            item.vote = vote.vote;
        } else {
            item.vote = 0;
        }
        item.save = book == null ? 0 : 1;
        List<User> userVisiters = App.Database.daoVisit().getUsersByPlace(Long.parseLong(item.unic), 1);
        item.visiters = new ArrayList<>();
        if (userVisiters.size() > 0) {
            Bitmap bitmap;
            for (User visiter : userVisiters) {
                bitmap = App.MapCache.get(visiter.avatar);
                if (bitmap == null) {
                    bitmap = FileUtils.GetBitmap(visiter.avatar);
                    if (bitmap != null) App.MapCache.put(visiter.avatar, bitmap);
                }
                item.visiters.add(new DataUser(bitmap, visiter.name, visiter.unic));
            }
        }
        return item;
    }

    public static User Build(User item, long user_unic) {
        item.mediaItemList = App.Database.daoMediaItem().getListOrderByPositionStar(item.unic);
        Vote vote = App.Database.daoVote().get(item.unic, user_unic);
        if (vote != null) {
            item.vote = vote.vote;
            if (vote.vote == 1) {
                item.rating += 1;
            } else {
                item.rating -= 1;
            }
        } else {
            item.vote = 0;
        }
        Friend friend = App.Database.daoFriends().getByUser(item.unic);
        if (friend != null) {
            item.friend = friend.type;
        } else {
            item.friend = 0;
        }
        return item;
    }

    public static boolean IsShow(Place place, MapOptions mapOptions, Calendar calendarNow, Boolean isBegin, Boolean isEnd) {
        System.out.println("test is show");
        if (place == null) return false;
        if (place.is_remove.equals(1)) return false;
        boolean all_date = mapOptions.all_date;
        boolean current_date = mapOptions.current_date;
        boolean old_date = mapOptions.old_date;
        boolean future_date = mapOptions.future_date;
        if (place.date_begin != null && !place.date_begin.equals("")) {
            Calendar calendarBegin = Calendar.getInstance();
            try {
                calendarBegin.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(place.date_begin));
            } catch (Exception e) {
                e.printStackTrace();
            }
            calendarBegin.set(Calendar.HOUR, 0);
            calendarBegin.set(Calendar.MINUTE, 0);
            calendarBegin.set(Calendar.SECOND, 0);
            if (calendarNow.getTime().getTime() >= calendarBegin.getTime().getTime()) {
                isBegin = true;
            } else {
                isBegin = false;
            }
        } else {
            isBegin = true;
        }
        if (place.date_end != null && !place.date_end.equals("")) {
            Calendar calendarEnd = Calendar.getInstance();
            try {
                calendarEnd.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(place.date_end));
            } catch (Exception e) {
                e.printStackTrace();
            }
            calendarEnd.set(Calendar.HOUR, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            if (calendarNow.getTime().getTime() >= calendarEnd.getTime().getTime()) {
                isEnd = true;
            } else {
                isEnd = false;
            }

        }
        boolean validate = false;
        if (all_date) validate = true;
        if (current_date) validate = (isBegin && !isBegin);
        if (old_date) validate = (isBegin && isEnd);
        if (future_date) validate = !isBegin && isEnd;
        if (!validate) return false;
        if (place.only_for_friends.equals(1)) {
            Friend friend = App.Database.daoFriends().getByUser(place.user_unic);
            if (friend != null) {
                if (friend.type == Friend.FRIEND) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public static boolean IsShow(User user, MapOptions mapOptions, Long user_unic) {
        if (user == null) return false;
        if (user_unic.equals(user.unic)) return false;
        boolean viewOffline = mapOptions.offlie_mode;
        if (viewOffline || User.IsActive(user.last_time_activity)) return true;
        if (user.show_in_map == 1) {
            if (User.IsActive(user.last_time_activity)) {
                return true;
            } else {
                return false;
            }
        } else if (user.show_in_map == 2) {
            Friend friend = App.Database.daoFriends().getByUser(user.unic);
            if (friend != null) {
                if (friend.type == Friend.FRIEND) {
                    if (User.IsActive(user.last_time_activity)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
