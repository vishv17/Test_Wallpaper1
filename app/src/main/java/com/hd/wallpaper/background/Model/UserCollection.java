package com.hd.wallpaper.background.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCollection
{
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("published_at")
    private String published_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("curated")
    private boolean curated;

    @SerializedName("featured")
    private boolean featured;

    @SerializedName("total_photos")
    private int total_photos;

    @SerializedName("tags")
    private List<Tags> tagsList;

    @SerializedName("links")
    private Links links;

    @SerializedName("user")
    private User user;

    @SerializedName("cover_photo")
    private CoverPhoto coverPhoto;

    @SerializedName("preview_photos")
    private List<PreviewPhotos> previewPhotosList;

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

    public List<Tags> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<Tags> tagsList) {
        this.tagsList = tagsList;
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

    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(CoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<PreviewPhotos> getPreviewPhotosList() {
        return previewPhotosList;
    }

    public void setPreviewPhotosList(List<PreviewPhotos> previewPhotosList) {
        this.previewPhotosList = previewPhotosList;
    }
}
