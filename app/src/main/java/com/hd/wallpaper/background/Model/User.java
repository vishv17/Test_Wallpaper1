
package com.hd.wallpaper.background.Model;

import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("accepted_tos")
    private Boolean mAcceptedTos;
    @SerializedName("bio")
    private String mBio;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("instagram_username")
    private String mInstagramUsername;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("links")
    private Links mLinks;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("name")
    private String mName;
    @SerializedName("portfolio_url")
    private Object mPortfolioUrl;
    @SerializedName("profile_image")
    private ProfileImage mProfileImage;
    @SerializedName("total_collections")
    private Long mTotalCollections;
    @SerializedName("total_likes")
    private Long mTotalLikes;
    @SerializedName("total_photos")
    private Long mTotalPhotos;
    @SerializedName("twitter_username")
    private String mTwitterUsername;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("username")
    private String mUsername;

    public Boolean getAcceptedTos() {
        return mAcceptedTos;
    }

    public void setAcceptedTos(Boolean acceptedTos) {
        mAcceptedTos = acceptedTos;
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInstagramUsername() {
        return mInstagramUsername;
    }

    public void setInstagramUsername(String instagramUsername) {
        mInstagramUsername = instagramUsername;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public Links getLinks() {
        return mLinks;
    }

    public void setLinks(Links links) {
        mLinks = links;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getPortfolioUrl() {
        return mPortfolioUrl;
    }

    public void setPortfolioUrl(Object portfolioUrl) {
        mPortfolioUrl = portfolioUrl;
    }

    public ProfileImage getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        mProfileImage = profileImage;
    }

    public Long getTotalCollections() {
        return mTotalCollections;
    }

    public void setTotalCollections(Long totalCollections) {
        mTotalCollections = totalCollections;
    }

    public Long getTotalLikes() {
        return mTotalLikes;
    }

    public void setTotalLikes(Long totalLikes) {
        mTotalLikes = totalLikes;
    }

    public Long getTotalPhotos() {
        return mTotalPhotos;
    }

    public void setTotalPhotos(Long totalPhotos) {
        mTotalPhotos = totalPhotos;
    }

    public String getTwitterUsername() {
        return mTwitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        mTwitterUsername = twitterUsername;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
