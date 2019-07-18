package com.hd.wallpaper.background.Model;

import com.google.gson.annotations.SerializedName;

public class PreviewPhotos
{
    @SerializedName("id")
    private String id;

    @SerializedName("urls")
    private Urls urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }
}
