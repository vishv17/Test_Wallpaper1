package com.hd.wallpaper.background.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hd.wallpaper.background.Adapter.UserCollectionPhotoAdapter;
import com.hd.wallpaper.background.Api.UserApi;
import com.hd.wallpaper.background.Model.Collection;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotographerCollectionActivity extends AppCompatActivity implements View.OnClickListener, UserCollectionPhotoAdapter.CollectionPhotoClick {
    private ImageButton ibtn_back;
    private TextView txt_total_photos;
    private RecyclerView rc_collection_photographer;
    private ProgressBar progressBar,progress_load_more;
    private PhotographerCollectionActivity activity;
    private String photographerUsername;
    private int totalCollection;
    private UserApi userApi;
    private List<Collection> userCollections;
    private LinearLayoutManager linearLayoutManager;
    private UserCollectionPhotoAdapter userCollectionPhotoAdapter;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_collection);
        activity = PhotographerCollectionActivity.this;

        initView();
        initViewAction();
        initViewListener();
    }
    private int page = 1;

    private void initView()
    {
        ibtn_back = findViewById(R.id.ibtn_back);
        txt_total_photos = findViewById(R.id.txt_total_photos);
        rc_collection_photographer = findViewById(R.id.rc_collection_photographer);
        progressBar = findViewById(R.id.progressBar);
        progress_load_more = findViewById(R.id.progress_load_more);
        userCollections = new ArrayList<>();
    }

    private void initViewAction()
    {
        photographerUsername = getIntent().getStringExtra("photographerUsername");
        totalCollection = getIntent().getIntExtra("totalCollection",0);

        txt_total_photos.setText(Integer.toString(totalCollection)+" Photos");
        getData();
    }

    private void getData()
    {
            if(page == 1)
            {
                progressBar.setVisibility(View.VISIBLE);
                rc_collection_photographer.setVisibility(View.GONE);
                progress_load_more.setVisibility(View.GONE);
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                rc_collection_photographer.setVisibility(View.VISIBLE);
                progress_load_more.setVisibility(View.VISIBLE);
            }

            if(userCollections.size()<totalCollection)
            {
                userApi = Unsplash.getRetrofit().create(UserApi.class);
                Call<List<Collection>> call = userApi.getUserCollection(photographerUsername,page,30,"latest");
                call.enqueue(new Callback<List<Collection>>() {
                    @Override
                    public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                        if(response.isSuccessful())
                        {
                            userCollections.addAll(response.body());
                            page += 1;
                            rc_collection_photographer.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            if(userCollectionPhotoAdapter!=null)
                            {
                                userCollectionPhotoAdapter.notifyItemInserted(userCollections.size()-30);
                                progress_load_more.setVisibility(View.GONE);
                            }
                            else
                            {
                                setAdapter(userCollections);
                            }
                        }
                        else
                        {
                            Toast.makeText(activity, "Error while loading collections calling", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Collection>> call, Throwable t)
                    {
                        Toast.makeText(activity, "Error while loading collection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                rc_collection_photographer.setVisibility(View.VISIBLE);
                progress_load_more.setVisibility(View.GONE);
            }
    }

    private void setAdapter(List<Collection> userCollections)
    {
        linearLayoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        rc_collection_photographer.setLayoutManager(linearLayoutManager);

        userCollectionPhotoAdapter = new UserCollectionPhotoAdapter(activity,userCollections,this);
        rc_collection_photographer.setAdapter(userCollectionPhotoAdapter);

        progressBar.setVisibility(View.GONE);
    }

    private void initViewListener()
    {
        ibtn_back.setOnClickListener(this);
        rc_collection_photographer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = linearLayoutManager.getChildCount();
                totalItem = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItem+scrollOutItems == totalItem))
                {
                    isScrolling = false;
                    getData();
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibtn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onCollectionPhotoClick(int position, Drawable drawable, int totalPhotos)
    {
        Intent intent = new Intent(activity, PhotosCollectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("Collection",new Gson().toJson(userCollections.get(position)));
        intent.putExtra("totalPhotos",totalPhotos);
        startActivity(intent);
    }
}
