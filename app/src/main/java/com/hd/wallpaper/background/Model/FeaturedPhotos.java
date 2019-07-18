package com.hd.wallpaper.background.Model;

import java.io.Serializable;
import java.util.List;

public class FeaturedPhotos implements Serializable
{
    private String id;
    private String created_at;
    private String updated_at;
    private String width;
    private String height;
    private String color;
    private String description;
    private String alt_description;
    private Urls urls;
    private Links links;
    private List<Object> mCategories;
    private boolean sponsored;
    private String sponsored_by;
    private String sponsored_impressions_id;
    private int likes;
    private boolean liked_by_user;
    private List<Object> mCurrentUserCollections;
    private User user;

    public String getId()
    {
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public List<Object> getmCategories() {
        return mCategories;
    }

    public void setmCategories(List<Object> mCategories) {
        this.mCategories = mCategories;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public void setSponsored(boolean sponsored) {
        this.sponsored = sponsored;
    }

    public String getSponsored_by() {
        return sponsored_by;
    }

    public void setSponsored_by(String sponsored_by) {
        this.sponsored_by = sponsored_by;
    }

    public String getSponsored_impressions_id() {
        return sponsored_impressions_id;
    }

    public void setSponsored_impressions_id(String sponsored_impressions_id) {
        this.sponsored_impressions_id = sponsored_impressions_id;
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

    public List<Object> getmCurrentUserCollections() {
        return mCurrentUserCollections;
    }

    public void setmCurrentUserCollections(List<Object> mCurrentUserCollections) {
        this.mCurrentUserCollections = mCurrentUserCollections;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
