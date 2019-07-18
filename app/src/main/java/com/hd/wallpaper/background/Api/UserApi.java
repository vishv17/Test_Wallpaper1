package com.hd.wallpaper.background.Api;

import com.hd.wallpaper.background.Model.Collection;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.Model.Photographer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserApi
{
    @GET("users/{username}")
    Call<Photographer> getPhotographerProfile(@Path("username") String username);

    @GET("users/{username}/likes")
    Call<List<Photo>> getUserLikes(@Path("username") String username,
                                   @Query("page") Integer page,
                                   @Query("per_page") Integer per_page,
                                   @Query("order_by") String order_by);

    @GET("users/{username}/collections")
    Call<List<Collection>> getUserCollection(@Path("username") String username,
                                             @Query("page") int page,
                                             @Query("per_page") int per_page,
                                             @Query("order_by") String order_by
                                                 );
}
