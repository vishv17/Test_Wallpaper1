package com.hd.wallpaper.background.Model;

import java.io.Serializable;

public class PreviewPhoto implements Serializable
{
    private String id;
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
