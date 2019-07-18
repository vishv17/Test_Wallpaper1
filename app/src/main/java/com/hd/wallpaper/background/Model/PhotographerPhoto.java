package com.hd.wallpaper.background.Model;

import com.google.gson.annotations.SerializedName;

public class PhotographerPhoto
{
    @SerializedName("id")
    private String id;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("description")
    private String description;

    @SerializedName("alt_description")
    private String alt_description;

    @SerializedName("urls")
    private Urls urls;

    @SerializedName("links")
    private Links links;

    @SerializedName("likes")
    private int likes;

    @SerializedName("liked_by_user")
    private boolean liked_by_user;

    @SerializedName("user")
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlt_description() {
        return alt_description;
    }

    public void setAlt_description(String alt_description) {
        this.alt_description = alt_description;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked_by_user() {
        return liked_by_user;
    }

    public void setLiked_by_user(boolean liked_by_user) {
        this.liked_by_user = liked_by_user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
