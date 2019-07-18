package com.hd.wallpaper.background.Activity;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class WallpaperApplication extends Application
{
    private Drawable drawable;

    public Drawable getUser_drawable() {
        return user_drawable;
    }

    public void setUser_drawable(Drawable user_drawable) {
        this.user_drawable = user_drawable;
    }

    private Drawable user_drawable;
    public static final String UNSPLASH_UTM_PARAMETERS = "?utm_source=resplash&utm_medium=referral&utm_campaign=api-credit";
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String DOWNLOAD_PATH = "/Pictures/Resplash/";
    public static final String DOWNLOAD_PHOTO_FORMAT = ".jpg";

    public static WallpaperApplication wallpaperApplication;

    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable)
    {
        this.drawable = drawable;
    }

    public static WallpaperApplication getWallpaperApplication()
    {
        return wallpaperApplication;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("WallpaperApplication","WallpaperApplication onCreate is called");
        wallpaperApplication = this;
    }

    public static WallpaperApplication getInstance()
    {
        return wallpaperApplication;
    }
}
