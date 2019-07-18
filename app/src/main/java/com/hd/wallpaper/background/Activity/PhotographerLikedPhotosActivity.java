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
import com.hd.wallpaper.background.Adapter.ListofPhotographersLikedAdapter;
import com.hd.wallpaper.background.Api.UserApi;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotographerLikedPhotosActivity extends AppCompatActivity implements View.OnClickListener, ListofPhotographersLikedAdapter.OnPhotoClick {

    private PhotographerLikedPhotosActivity activity;
    private ImageButton ibtn_back;
    private TextView txt_total_likes;
    private RecyclerView rc_liked_photos;
    private ProgressBar progressBar,progress_load_more;
    private UserApi userApi;
    private List<Photo> photoList;
    private LinearLayoutManager linearLayoutManager;
    private int totalLikes;
    private String photographerUsername;
    private int page = 1;
    private ListofPhotographersLikedAdapter photographersLikedAdapter;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_liked_photos);
        activity = PhotographerLikedPhotosActivity.this;

        initView();
        initViewAction();
        initViewListener();
    }

    private void initView()
    {
        ibtn_back = findViewById(R.id.ibtn_back);
        txt_total_likes = findViewById(R.id.txt_total_likes);
        rc_liked_photos = findViewById(R.id.rc_liked_photos);
        progressBar = findViewById(R.id.progressBar);
        progress_load_more = findViewById(R.id.progress_load_more);
        photoList = new ArrayList<>();
    }

    private void initViewAction()
    {
        photographerUsername = getIntent().getStringExtra("photographerUsername");
        totalLikes = getIntent().getIntExtra("totalLikes",0);

        txt_total_likes.setText(Integer.toString(totalLikes)+" Likes");
        getData();
    }

    private void getData()
    {
        if(page == 1)
        {
            progressBar.setVisibility(View.VISIBLE);
            rc_liked_photos.setVisibility(View.GONE);
            progress_load_more.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            rc_liked_photos.setVisibility(View.VISIBLE);
            progress_load_more.setVisibility(View.VISIBLE);
        }

        if(photoList.size()<totalLikes)
        {
            userApi = Unsplash.getRetrofit().create(UserApi.class);
            Call<List<Photo>> call = userApi.getUserLikes(photographerUsername, page, 30, "latest");
            call.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        photoList.addAll(response.body());
                        page += 1;
                        rc_liked_photos.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (photographersLikedAdapter != null) {
                            photographersLikedAdapter.notifyItemInserted(photoList.size() - 30);
                            progress_load_more.setVisibility(View.GONE);
                        } else {
                            setAdapter(photoList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Toast.makeText(activity, "Error While Loading Images", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            rc_liked_photos.setVisibility(View.VISIBLE);
            progress_load_more.setVisibility(View.GONE);
        }
    }

    private void setAdapter(List<Photo> photoList)
    {
        linearLayoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        rc_liked_photos.setLayoutManager(linearLayoutManager);
        photographersLikedAdapter = new ListofPhotographersLikedAdapter(activity,photoList,this);
        rc_liked_photos.setAdapter(photographersLikedAdapter);
        progressBar.setVisibility(View.GONE);

    }

    private void initViewListener()
    {
        ibtn_back.setOnClickListener(this);
        rc_liked_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    public void onClickPhoto(int position, String image_id, Drawable drawable, Photo photo)
    {
//        Toast.makeText(activity, "OnPhoto Click Yet to be implement", Toast.LENGTH_SHORT).show();
        WallpaperApplication.getWallpaperApplication().setDrawable(drawable);

        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra("imageId",image_id);
        intent.putExtra("photo",new Gson().toJson(photo));
        startActivity(intent);
    }
}
