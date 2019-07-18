
package com.hd.wallpaper.background.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Photo {

    @SerializedName("alt_description")
    private Object mAltDescription;
    @SerializedName("categories")
    private List<Object> mCategories;
    @SerializedName("color")
    private String mColor;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("current_user_collections")
    private List<Object> mCurrentUserCollections;
    @SerializedName("description")
    private Object mDescription;
    @SerializedName("height")
    private Long mHeight;
    @SerializedName("id")
    private String mId;
    @SerializedName("liked_by_user")
    private Boolean mLikedByUser;
    @SerializedName("likes")
    private Long mLikes;
    @SerializedName("links")
    private Links mLinks;
    @SerializedName("sponsored")
    private Boolean mSponsored;
    @SerializedName("sponsored_by")
    private Object mSponsoredBy;
    @SerializedName("sponsored_impressions_id")
    private Object mSponsoredImpressionsId;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("urls")
    private Urls mUrls;
    @SerializedName("user")
    private User mUser;
    @SerializedName("width")
    private Long mWidth;
    @SerializedName("views")
    private int views;
    @SerializedName("downloads")
    private int downloads;
    @SerializedName("exif")
    private Exif exif;

    public Object getmAltDescription() {
        return mAltDescription;
    }

    public Exif getExif() {
        return exif;
    }

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    public void setmAltDescription(Object mAltDescription) {
        this.mAltDescription = mAltDescription;
    }

    public List<Object> getmCategories() {
        return mCategories;
    }

    public void setmCategories(List<Object> mCategories) {
        this.mCategories = mCategories;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public List<Object> getmCurrentUserCollections() {
        return mCurrentUserCollections;
    }

    public void setmCurrentUserCollections(List<Object> mCurrentUserCollections) {
        this.mCurrentUserCollections = mCurrentUserCollections;
    }

    public Object getmDescription() {
        return mDescription;
    }

    public void setmDescription(Object mDescription) {
        this.mDescription = mDescription;
    }

    public Long getmHeight() {
        return mHeight;
    }

    public void setmHeight(Long mHeight) {
        this.mHeight = mHeight;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Boolean getmLikedByUser() {
        return mLikedByUser;
    }

    public void setmLikedByUser(Boolean mLikedByUser) {
        this.mLikedByUser = mLikedByUser;
    }

    public Long getmLikes() {
        return mLikes;
    }

    public void setmLikes(Long mLikes) {
        this.mLikes = mLikes;
    }

    public Links getmLinks() {
        return mLinks;
    }

    public void setmLinks(Links mLinks) {
        this.mLinks = mLinks;
    }

    public Boolean getmSponsored() {
        return mSponsored;
    }

    public void setmSponsored(Boolean mSponsored) {
        this.mSponsored = mSponsored;
    }

    public Object getmSponsoredBy() {
        return mSponsoredBy;
    }

    public void setmSponsoredBy(Object mSponsoredBy) {
        this.mSponsoredBy = mSponsoredBy;
    }

    public Object getmSponsoredImpressionsId() {
        return mSponsoredImpressionsId;
    }

    public void setmSponsoredImpressionsId(Object mSponsoredImpressionsId) {
        this.mSponsoredImpressionsId = mSponsoredImpressionsId;
    }

    public String getmUpdatedAt() {
        return mUpdatedAt;
    }

    public void setmUpdatedAt(String mUpdatedAt) {
        this.mUpdatedAt = mUpdatedAt;
    }

    public Urls getmUrls() {
        return mUrls;
    }

    public void setmUrls(Urls mUrls) {
        this.mUrls = mUrls;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public Long getmWidth() {
        return mWidth;
    }

    public void setmWidth(Long mWidth) {
        this.mWidth = mWidth;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public Object getAltDescription() {
        return mAltDescription;
    }

    public void setAltDescription(Object altDescription) {
        mAltDescription = altDescription;
    }

    public List<Object> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Object> categories) {
        mCategories = categories;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public List<Object> getCurrentUserCollections() {
        return mCurrentUserCollections;
    }

    public void setCurrentUserCollections(List<Object> currentUserCollections) {
        mCurrentUserCollections = currentUserCollections;
    }

    public Object getDescription() {
        return mDescription;
    }

    public void setDescription(Object description) {
        mDescription = description;
    }

    public Long getHeight() {
        return mHeight;
    }

    public void setHeight(Long height) {
        mHeight = height;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Boolean getLikedByUser() {
        return mLikedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        mLikedByUser = likedByUser;
    }

    public Long getLikes() {
        return mLikes;
    }

    public void setLikes(Long likes) {
        mLikes = likes;
    }

    public Links getLinks() {
        return mLinks;
    }

    public void setLinks(Links links) {
        mLinks = links;
    }

    public Boolean getSponsored() {
        return mSponsored;
    }

    public void setSponsored(Boolean sponsored) {
        mSponsored = sponsored;
    }

    public Object getSponsoredBy() {
        return mSponsoredBy;
    }

    public void setSponsoredBy(Object sponsoredBy) {
        mSponsoredBy = sponsoredBy;
    }

    public Object getSponsoredImpressionsId() {
        return mSponsoredImpressionsId;
    }

    public void setSponsoredImpressionsId(Object sponsoredImpressionsId) {
        mSponsoredImpressionsId = sponsoredImpressionsId;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Urls getUrls() {
        return mUrls;
    }

    public void setUrls(Urls urls) {
        mUrls = urls;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Long getWidth() {
        return mWidth;
    }

    public void setWidth(Long width) {
        mWidth = width;
    }

}
