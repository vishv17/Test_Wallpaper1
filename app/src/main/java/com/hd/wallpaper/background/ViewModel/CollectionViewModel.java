package com.hd.wallpaper.background.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.hd.wallpaper.background.Api.CollectionApi;
import com.hd.wallpaper.background.Model.Collection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionViewModel extends ViewModel
{
    private MutableLiveData<List<Collection>> collectionList;
    private CollectionApi collectionApi;
    private int page = 1;
    private int per_page = 10;

    private LiveData<List<Collection>> getCollections()
    {
        if(collectionList == null)
        {
            collectionList = new MutableLiveData<List<Collection>>();

            loadCollections();
        }
        else
        {
            updateCollections();
        }

        return collectionList;
    }

    private void updateCollections()
    {

    }

    private void loadCollections()
    {
        Call<List<Collection>> call = collectionApi.getCollections(page,per_page);
        call.enqueue(new Callback<List<Collection>>()
        {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response)
            {
                if(response.isSuccessful())
                {

                }
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t)
            {

            }
        });
    }
}
