package com.wearetogether.v2.app.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;

public class Account {
    private String unic;
    private String social_id;
    private String email;
    private String avatar;
    private String name;

    public Account(String social_id, String email, String avatar, String name) {
        this.unic = String.valueOf(Calendar.getInstance().getTimeInMillis());
        this.social_id = social_id;
        this.email = email;
        this.avatar = avatar;
        this.name = name;
    }

    public Account(GoogleSignInAccount account) {
        this.unic = String.valueOf(Calendar.getInstance().getTimeInMillis());
        this.email = account.getEmail();
        this.social_id = account.getId();
        this.name = account.getDisplayName();
        this.avatar = String.valueOf(account.getPhotoUrl());
    }

    public String getUnic() {
        return unic;
    }

    public String getSocialId() {
        return social_id;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDisplayName() {
        return name;
    }
}

