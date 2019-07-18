package com.hd.wallpaper.background.Api;

import com.hd.wallpaper.background.Model.Collection;
import com.hd.wallpaper.background.Model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CollectionApi
{
    @GET("collections/")
    Call<List<Collection>> getCollections(@Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("collections/featured/")
    Call<List<Collection>> getFeaturedCollections(@Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getCollectionPhotos(@Path("id") Integer id,
                                          @Query("page") Integer page,
                                          @Query("per_page") Integer per_page);


}
