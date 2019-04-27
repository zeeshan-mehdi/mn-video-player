package com.jamshed.videoplayer;

public class Model_Video {
    boolean boolean_selected;
    String str_path;
    String str_thumb;
    String title;

    public Model_Video(String str, String str2, String str3, boolean z) {
        this.title = str;
        this.str_path = str2;
        this.str_thumb = str3;
        this.boolean_selected = z;
    }

    public String getStr_path() {
        return this.str_path;
    }

    public void setStr_path(String str) {
        this.str_path = str;
    }

    public String getStr_thumb() {
        return this.str_thumb;
    }

    public void setStr_thumb(String str) {
        this.str_thumb = str;
    }

    public boolean isBoolean_selected() {
        return this.boolean_selected;
    }

    public void setBoolean_selected(boolean z) {
        this.boolean_selected = z;
    }
}
