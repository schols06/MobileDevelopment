package com.example.luc.timetracker;

public class Item {

    private long id;
    private String description;
    private Tag tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setItem(String description) {
        this.description = description;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return description;
    }

}
