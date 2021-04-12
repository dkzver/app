package com.wearetogether.v2.app.model;

import android.graphics.Bitmap;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.app.message.data.MessagePicture;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.SMediaItem;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataGroup {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_USER_PROFILE = 4;
    public static final int TYPE_PLACE_HEADER = 5;
    public static final int TYPE_USER_HEADER = 6;
    public static final int TYPE_GALLERY = 7;
    public static final int TYPE_APPBAR = 8;
    public static final int TYPE_PLACE = 11;
    public static final int TYPE_USER = 12;
    public static final int TYPE_COMMENT = 13;
    public static final int TYPE_USER_PLACE = 14;
    public static final int TYPE_HEADER_OPTIONS = 15;
    public static final int TYPE_ROOM = 16;
    public static final int TYPE_MESSAGE_LEFT = 17;
    public static final int TYPE_MESSAGE_RIGHT = 18;
    public static final int WRITE = 19;
    public static final int TYPE_MESSAGE_DATE = 20;
    public static final int TYPE_PREPARE_PICTURE = 21;
    public static final int TYPE_MESSAGE_PICTURE_RIGHT = 22;
    public static final int TYPE_MESSAGE_PICTURE_LEFT = 23;

    public int type;
    public String text;
    public String icon;
    public String user_name;
    public Bitmap bitmapIcon = null;
    public Bitmap bitmapAvatar = null;
    public Bitmap bitmapMap = null;
    public String title;
    public String description;
    public int is_remove;
    public int vote;
    public int save;
    public int rating;
    public long unic;
    public long user_unic;
    public String location;
    public String country;
    public String city;
    public int count_place;
    public Double latitude;
    public Double longitude;
    public boolean colorAppbar;
    public Class<?> cls;
    public Comment comment;
    public Room room;
    public int type_comment;
    public String avatar;
    public String date_begin;
    public String date_end;
    public String time_visit;
    public int count_participant;
    public int anonymous_visit;
    public int category_id;
    public int visit;
    public List<DataUser> visiters;
    public int selected;
    public int friend;
    public String small;
    public String original;
    public String message_unic;
    public String message_user_unic;
    public String message_text;
    public String message_type;
    public Date message_date;
    public long room_unic;
    public String room_avatar;
    public String room_title;
    public Long room_owner;
    public String[] options = null;
    public String[] array_interests;
    public List<Bitmap> bitmapList = new ArrayList<>();
    public List<String> hintList = new ArrayList<>();
    public int disable_comments;
    public int only_for_friends;
    public long uUnic;
    public int show_sex;
    public int show_age;
    public String age;
    public String sex;
    public String is_read;
    public float distance;

    public DataGroup() {

    }

    public DataGroup(int id) {
        text = String.valueOf(id);
    }

    public DataGroup(String text, int type) {
        this.type = type;
        this.text = text;
    }

    public DataGroup Message(Message message, String user_unic) {
        this.message_user_unic = String.valueOf(message.user_unic);
        this.message_unic = String.valueOf(message.unic);
        if (message.type == Consts.MESSAGE_TYPE_TEXT) {
            if (!this.message_user_unic.equals(user_unic)) {
                this.type = TYPE_MESSAGE_LEFT;
            } else {
                this.type = TYPE_MESSAGE_RIGHT;
            }
            this.message_text = message.content;
        } else if (message.type == Consts.MESSAGE_TYPE_PICTURE) {
            MessagePicture messagePicture = MessagePicture.Parse(message.content);
            if(messagePicture != null) {
                this.icon = messagePicture.icon;
                this.small = messagePicture.small;
                this.original = messagePicture.original;
                if (!this.message_user_unic.equals(user_unic)) {
                    this.type = TYPE_MESSAGE_PICTURE_LEFT;
                } else {
                    this.type = TYPE_MESSAGE_PICTURE_RIGHT;
                }
            }
        }
        this.message_type = String.valueOf(message.type);
        this.is_read = message.is_read;
        Date date = new Date();
        date.setTime(Long.parseLong(this.message_unic));
        this.message_date = date;
        return this;
    }

    public DataGroup Profile(String country, String city, String location) {
        this.type = TYPE_USER_PROFILE;
        this.country = country;
        this.city = city;
        this.location = location;
        return this;
    }

    public DataGroup Profile(User user, Bitmap avatar, String[] array_interests) {
        this.type = TYPE_USER_PROFILE;
        this.country = user.country;
        this.city = user.city;
        this.location = user.location;
        this.user_name = user.name;
        this.user_unic = user.unic;
        this.bitmapAvatar = avatar;
        this.avatar = user.avatar;
        this.array_interests = array_interests;
        return this;
    }

    public DataGroup UserPlace(Place item, long user_unic) {
        this.type = TYPE_USER_PLACE;
        setupPlace(item, user_unic);
        return this;
    }

    public DataGroup Place(Place item, long user_unic) {
        this.type = TYPE_PLACE;
        setupPlace(item, user_unic);
        return this;
    }

    private void setupPlace(Place item,
                            long user_unic) {
        if (App.MapCache == null) {
            App.InitCache();
        }
        this.uUnic = user_unic;
        this.unic = item.unic;
        this.distance = item.distance != null ? item.distance : 0;
        this.disable_comments = item.disable_comments;
        this.only_for_friends = item.only_for_friends;
        this.anonymous_visit = item.anonymous_visit;
        this.count_participant = item.count_participant;
        this.category_id = item.category_id;
        if (item.icon != null && !item.icon.equals("")) {
            bitmapIcon = App.MapCache.get(item.icon);
            if (bitmapIcon == null) {
                bitmapIcon = FileUtils.GetBitmap(item.icon);
                if (bitmapIcon != null) App.MapCache.put(item.icon, bitmapIcon);
            }
        }
        if (item.user_avatar != null) {
            bitmapAvatar = App.MapCache.get(item.user_avatar);
            if (bitmapAvatar == null) {
                bitmapAvatar = FileUtils.GetBitmap(item.user_avatar);
                if (bitmapAvatar != null) App.MapCache.put(item.user_avatar, bitmapAvatar);
            }
        }

        if (item.mediaItemList != null) {
            Bitmap bitmap;
            if (this.bitmapList != null) {
                this.bitmapList.clear();
            } else {
                this.bitmapList = new ArrayList<>();
            }
            if (item.mediaItemList.size() > 0) {
                for (int x = 0; x < item.mediaItemList.size(); x++) {
                    bitmap = App.MapCache.get(item.mediaItemList.get(x).icon);
                    if (bitmap == null) {
                        bitmap = FileUtils.GetBitmap(item.mediaItemList.get(x).icon);
                        if (bitmap != null) App.MapCache.put(item.mediaItemList.get(x).icon, bitmap);
                    }
                    this.bitmapList.add(bitmap);
                    this.hintList.add(item.mediaItemList.get(x).hint);
                }
            }
        }
        this.user_unic = item.user_unic;
        this.user_name = item.user_name;
        this.title = item.title;
        this.description = item.description;
        this.rating = item.rating;
        this.visit = item.visit;
        this.vote = item.vote;
        this.save = item.save;
        this.location = item.address;
        this.visiters = item.visiters;
    }

    public DataGroup User(User item, long user_unic) {
        if (App.MapCache == null) {
            App.InitCache();
        }
        this.type = TYPE_USER;
        if (item.mediaItemList != null && item.mediaItemList.size() > 0) {
            Bitmap bitmap;
            for (int x = 0; x < item.mediaItemList.size(); x++) {
                bitmap = App.MapCache.get(item.mediaItemList.get(x).icon);
                if (bitmap == null) {
                    bitmap = FileUtils.GetBitmap(item.mediaItemList.get(x).icon);
                    App.MapCache.put(item.mediaItemList.get(x).icon, bitmap);
                }
                this.bitmapList.add(bitmap);
                this.hintList.add(item.mediaItemList.get(x).hint);
            }
        }
        this.unic = item.unic;
        this.user_name = item.name;
        this.rating = item.rating;
        this.country = item.country;
        this.city = item.city;
        this.distance = item.distance;

        if (item.avatar != null) {
            bitmapAvatar = App.MapCache.get(item.avatar);
            if (bitmapAvatar == null) {
                bitmapAvatar = FileUtils.GetBitmap(item.avatar);
                App.MapCache.put(item.avatar, bitmapAvatar);
            }
        }
        this.vote = item.vote;
        this.friend = item.friend;
        return this;
    }

    public DataGroup HeaderPlace(Bitmap bitmapMap, Place place, long user_unic) {
        type = TYPE_PLACE_HEADER;
        this.uUnic = user_unic;
        this.title = place.title;
        this.description = place.description;
        this.bitmapAvatar = place.bitmapAvatar;
        this.bitmapMap = bitmapMap;
        this.latitude = place.latitude;
        this.longitude = place.longitude;
        this.unic = place.unic;
        this.user_unic = place.user_unic;
        this.count_place = place.count_places;
        this.location = place.address;
        this.anonymous_visit = place.anonymous_visit;
        this.is_remove = place.is_remove;
        this.date_begin = place.date_begin;
        this.date_end = place.date_end;
        this.time_visit = place.time_visit;
        this.count_participant = place.count_participant;
        this.category_id = place.category_id;
        this.vote = place.vote;
        this.visit = place.visit;
        this.save = place.save;
        this.rating = place.rating;
        this.visiters = new ArrayList<>();
        this.visiters.addAll(place.visiters);
        this.disable_comments = place.disable_comments;
        return this;
    }

    public DataGroup HeaderUser(Bitmap bitmapMap, Bitmap bitmapAvatar, User user, String[] array_sex, String[] array_interests) {
        type = TYPE_USER_HEADER;
        this.title = user.name;
        this.bitmapMap = bitmapMap;
        this.bitmapAvatar = bitmapAvatar;
        this.latitude = user.latitude;
        this.longitude = user.longitude;
        this.unic = user.unic;
        this.user_unic = user.unic;
        this.count_place = user.count_place;
        this.location = user.location;
        this.country = user.country;
        this.city = user.city;
        this.show_sex = user.show_sex;
        this.show_age = user.show_age;
        if (user.show_age == 1) {
        }
        this.age = App.GetAge(user.date_birth);
        if (user.show_sex == 1) {
        }
        this.sex = array_sex[user.sex];
        this.array_interests = array_interests;
        this.rating = user.rating;
        return this;
    }

    public DataGroup Gallery() {
        type = TYPE_GALLERY;
        return this;
    }

    public DataGroup Appbar(String title, Class<?> cls) {
        type = TYPE_APPBAR;
        this.title = title;
        this.cls = cls;
        return this;
    }

    public DataGroup Comment(Comment comment, int type) {
        this.type = TYPE_COMMENT;
        this.comment = comment;
        this.type_comment = type;
        return this;
    }

    public DataGroup Room(Room room) {
        this.type = TYPE_ROOM;
        this.room_unic = room.unic;
        this.room_title = room.title;
        this.room_avatar = room.avatar;
        this.room_owner = room.owner;
        return this;
    }

    public DataGroup Write(String user_name, Long user_unic) {
        this.type = WRITE;
        this.user_name = user_name;
        this.user_unic = user_unic;
        return this;
    }

    public DataGroup PreparePicture(String icon, String message_unic, long user_unic) {
        this.type = TYPE_PREPARE_PICTURE;
        this.icon = icon;
        this.message_unic = message_unic;
        this.user_unic = user_unic;
        return this;
    }

    public DataGroup MessagePictureRight(String icon, String small, String original, String message_unic, String user_unic) {
        this.type = TYPE_MESSAGE_PICTURE_RIGHT;
        this.icon = icon;
        this.small = small;
        this.original = original;
        this.message_unic = message_unic;
        this.message_user_unic = user_unic;
        Date date = new Date();
        date.setTime(Long.parseLong(this.message_unic));
        this.message_date = date;
        this.is_read = "0";
        return this;
    }

    public DataGroup MessagePictureLeft(String icon, String small, String original, String message_unic, String user_unic) {
        this.type = TYPE_MESSAGE_PICTURE_LEFT;
        this.icon = icon;
        this.small = small;
        this.original = original;
        this.message_unic = message_unic;
        this.message_user_unic = user_unic;
        Date date = new Date();
        date.setTime(Long.parseLong(this.message_unic));
        this.message_date = date;
        this.is_read = "0";
        return this;
    }

    public DataGroup MessageDate(String text) {
        this.type = TYPE_MESSAGE_DATE;
        this.title = text;
        return this;
    }

    public DataGroup Options(String[] options, int selected) {
        this.type = TYPE_HEADER_OPTIONS;
        this.options = options;
        this.selected = selected;
        return this;
    }
}
