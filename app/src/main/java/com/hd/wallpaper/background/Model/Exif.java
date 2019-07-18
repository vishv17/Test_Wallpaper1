package com.hd.wallpaper.background.Model;

import com.google.gson.annotations.SerializedName;

public class Exif
{
    @SerializedName("make")
    private String make;
    @SerializedName("model")
    private String model;
    @SerializedName("exposure_time")
    private String exposure_time;
    @SerializedName("aperture")
    private String aperture;
    @SerializedName("focal_length")
    private String focal_length;
    @SerializedName("iso")
    private String iso;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getExposure_time() {
        return exposure_time;
    }

    public void setExposure_time(String exposure_time) {
        this.exposure_time = exposure_time;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getFocal_length() {
        return focal_length;
    }

    public void setFocal_length(String focal_length) {
        this.focal_length = focal_length;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }
}
