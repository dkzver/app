package com.wearetogether.v2.app;

public interface HelperListener {
    void OnResult(String email, String social_id, String avatar);
    void OnResult(String email, String id, String first_name, String last_name, int sex, String date_birth, String avatar);
}
