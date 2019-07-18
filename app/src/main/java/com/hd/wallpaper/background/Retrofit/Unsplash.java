package com.hd.wallpaper.background.Retrofit;

import com.hd.wallpaper.background.Retrofit.HeaderInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Unsplash
{
    private static final String BASE_URL = "https://api.unsplash.com/";

    public static final String ORIENTATION_PORTRAIT = "portrait";
    public static final String ORIENTATION_LANDSCAPE = "landscape";
    public static final String ORIENTATION_SQUARISH = "squarish";

    private static Retrofit retrofit = null;
    private static final String client_id = "d5b355d46b4f3911f5bc7a9d5c935c06f672254517f2c5680552ef65bbd1238f";

    public static Retrofit getRetrofit()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(client_id)).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
