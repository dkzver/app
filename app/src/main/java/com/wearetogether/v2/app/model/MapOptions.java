package com.wearetogether.v2.app.model;

public class MapOptions {
    public boolean current_date = false;
    public boolean old_date = false;
    public boolean future_date = false;
    public boolean all_date = false;

    public boolean online_mode = false;
    public boolean offlie_mode = false;
    public boolean all_mode = false;

    public MapOptions() {
        all_date = true;
        all_mode = true;
    }
}
