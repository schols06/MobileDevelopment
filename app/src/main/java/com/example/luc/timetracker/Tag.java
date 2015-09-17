package com.example.luc.timetracker;

public class Tag {

    private long id;
    private String tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return tag;
    }
}
