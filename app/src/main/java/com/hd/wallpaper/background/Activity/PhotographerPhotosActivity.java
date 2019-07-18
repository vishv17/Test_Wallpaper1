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
import com.hd.wallpaper.background.Adapter.ListofPhotographersPhotoAdapter;
import com.hd.wallpaper.background.Api.PhotoApi;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.Model.PhotographerPhoto;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotographerPhotosActivity extends AppCompatActivity implements View.OnClickListener, ListofPhotographersPhotoAdapter.photoClick {
    private PhotographerPhotosActivity activity;
    private ImageButton ibtn_back;
    private TextView txt_total_photos;
    private RecyclerView rc_photographer_photos;
    private PhotoApi photoApi;
    private String photographerUsername;
    private int page = 1;
    private ProgressBar progressBar, progress_load_more;
    private List<Photo> photographerPhotoList;
    private ListofPhotographersPhotoAdapter listofPhotographersPhotoAdapter;
    private int totalPhotos;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_photos);
        activity = PhotographerPhotosActivity.this;

        initView();
        initViewAction();
        initViewListener();
    }

    private void initView() {
        ibtn_back = findViewById(R.id.ibtn_back);
        txt_total_photos = findViewById(R.id.txt_total_photos);
        rc_photographer_photos = findViewById(R.id.rc_photographer_photos);
        progressBar = findViewById(R.id.progressBar);
        progress_load_more = findViewById(R.id.progress_load_more);
        photographerPhotoList = new ArrayList<>();
    }

    private void initViewAction() {
        photographerUsername = getIntent().getStringExtra("photographerUsername");
        totalPhotos = getIntent().getIntExtra("totalPhotos", 0);
        txt_total_photos.setText(Integer.toString(totalPhotos) + " Photos");
        getData();
    }

    private void getData()
    {
        if (page == 1)
        {
            progressBar.setVisibility(View.VISIBLE);
            rc_photographer_photos.setVisibility(View.GONE);
            progress_load_more.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            rc_photographer_photos.setVisibility(View.VISIBLE);
            progress_load_more.setVisibility(View.VISIBLE);
        }

        if(photographerPhotoList.size()<totalPhotos)
        {
            photoApi = Unsplash.getRetrofit().create(PhotoApi.class);
            Call<List<Photo>> call = photoApi.getUserPhotos(photographerUsername, page, 30, "latest");
            call.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        photographerPhotoList.addAll(response.body());
                        page += 1;
                        rc_photographer_photos.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (listofPhotographersPhotoAdapter != null) {
                            listofPhotographersPhotoAdapter.notifyItemInserted(photographerPhotoList.size() - 30);
                            progress_load_more.setVisibility(View.GONE);
                        } else {
                            setAdapter(photographerPhotoList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Toast.makeText(activity, "Error in Photographer Photos activity", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            rc_photographer_photos.setVisibility(View.VISIBLE);
            progress_load_more.setVisibility(View.GONE);
        }

    }

    private void setAdapter(List<Photo> photographerPhotoList) {
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rc_photographer_photos.setLayoutManager(linearLayoutManager);

        listofPhotographersPhotoAdapter = new ListofPhotographersPhotoAdapter(activity, photographerPhotoList, this);
        rc_photographer_photos.setAdapter(listofPhotographersPhotoAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void initViewListener()
    {
        ibtn_back.setOnClickListener(this);
        rc_photographer_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPhotoClick(int position, String image_id, Drawable drawable, Photo photographerPhoto)
    {
//        Toast.makeText(activity, "On Photo Click Yet to implemented", Toast.LENGTH_SHORT).show();
        WallpaperApplication.getWallpaperApplication().setDrawable(drawable);

        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra("imageId",image_id);
        intent.putExtra("photo",new Gson().toJson(photographerPhoto));
        startActivity(intent);
    }
}
