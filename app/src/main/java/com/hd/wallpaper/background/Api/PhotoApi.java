package com.hd.wallpaper.background.Api;

import com.hd.wallpaper.background.Model.FeaturedPhotos;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.Model.PhotographerPhoto;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface PhotoApi
{
    @GET("photos/")
    Call<ArrayList<Photo>> getAllPhotos(@Query("page") int page, @Query("per_page") Integer per_page, @Query("order_by") String order_by);

    @GET("photos/curated")
    Call<List<FeaturedPhotos>> getCuratedPhotos(@Query("page") Integer page, @Query("per_page") Integer perPage,
                                                @Query("order_by") String orderBy);

    @GET("photos/{id}")
    Call<Photo> getPhotoById(@Path("id")String id);

    /*@GET("users/{username}/photos")
    Call<List<PhotographerPhoto>> getUserPhotos(@Path("username") String username,
                                                @Query("page") Integer page,
                                                @Query("per_page") Integer per_page,
                                                @Query("order_by") String order_by);*/

    @GET("users/{username}/photos")
    Call<List<Photo>> getUserPhotos(@Path("username") String username,
                                                @Query("page") Integer page,
                                                @Query("per_page") Integer per_page,
                                                @Query("order_by") String order_by);
    @GET("photos/{id}/download")
    Call<ResponseBody> downloadPhoto(@Path("id") String id);
}
