package com.wearetogether.v2.app.model;

import java.util.ArrayList;
import java.util.List;

public class NearbyItems {
    public int nearby;
    public List<String> names;

    public NearbyItems() {
        nearby = 0;
        names = new ArrayList<>();
    }
}
