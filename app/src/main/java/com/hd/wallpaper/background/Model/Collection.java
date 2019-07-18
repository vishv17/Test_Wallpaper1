package com.hd.wallpaper.background.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Collection implements Serializable
{
    private int id;
    private String title;
    private String description;
    private String published_at;
    private String updated_at;
    private boolean curated;
    private boolean featured;
    private int total_photos;
    private boolean mprivate;
    private String share_key;
    private ArrayList<Tags> tags;
    private Links links;
    private User user;
    private CoverPhoto cover_photo;

    public CoverPhoto getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(CoverPhoto cover_photo) {
        this.cover_photo = cover_photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isCurated() {
        return curated;
    }

    public void setCurated(boolean curated) {
        this.curated = curated;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public boolean isMprivate() {
        return mprivate;
    }

    public void setMprivate(boolean mprivate) {
        this.mprivate = mprivate;
    }

    public String getShare_key() {
        return share_key;
    }

    public void setShare_key(String share_key) {
        this.share_key = share_key;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
