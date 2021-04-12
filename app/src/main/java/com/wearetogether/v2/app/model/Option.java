package com.wearetogether.v2.app.model;

public class Option {
    private String title;
    private int key;
    private int position;

    public Option(String string, int key, int position) {
        this.title = string;
        this.key = key;
        this.position = position;
    }

    public Option(String string, int key) {
        this.title = string;
        this.key = key;
        this.position = 0;
    }

    public String getTitle() {
        return title;
    }

    public int getKey() {
        return key;
    }

    public int getPosition() {
        return position;
    }
}
