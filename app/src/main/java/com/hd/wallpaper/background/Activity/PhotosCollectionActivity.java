package com.hd.wallpaper.background.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hd.wallpaper.background.Adapter.CollectionPhotosAdapter;
import com.hd.wallpaper.background.Api.CollectionApi;
import com.hd.wallpaper.background.Model.Collection;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotosCollectionActivity extends AppCompatActivity implements View.OnClickListener, CollectionPhotosAdapter.CollectionPhotoClick {
    private PhotosCollectionActivity activity;
    private ImageButton ibtn_back;
    private TextView toolbartxt;
    private ImageView img_share,img_website;
    private Collection collection;
    private CollectionApi collectionApi;
    private int page = 1;
    private int totalPhotos;
    private ProgressBar progressBar,progress_load_more;
    private List<Photo> photoList;
    private RecyclerView rc_collection_photos;
    private CollectionPhotosAdapter collectionPhotosAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_collection);
        activity = PhotosCollectionActivity.this;

        initView();
        initViewAction();
        initViewListener();
    }

    private void initView()
    {
        ibtn_back = findViewById(R.id.ibtn_back);
        toolbartxt = findViewById(R.id.toolbartxt);
        img_share = findViewById(R.id.img_share);
        img_website = findViewById(R.id.img_website);
        progressBar = findViewById(R.id.progressBar);
        progress_load_more = findViewById(R.id.progress_load_more);
        rc_collection_photos = findViewById(R.id.rc_collection_photos);
        photoList = new ArrayList<>();
    }

    private void initViewAction()
    {
        collection = new Gson().fromJson(getIntent().getStringExtra("Collection"),Collection.class);
        totalPhotos = getIntent().getIntExtra("totalPhotos",0);
        getData();
    }

    private void getData()
    {
        if (photoList.size() < totalPhotos)
        {
            collectionApi = Unsplash.getRetrofit().create(CollectionApi.class);
            Call<List<Photo>> call = collectionApi.getCollectionPhotos(collection.getId(), page, 30);
            call.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    if (response.isSuccessful())
                    {
                        int oldPhotoSize = photoList.size();
                        photoList.addAll(response.body());
                        page += 1;
                        rc_collection_photos.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (collectionPhotosAdapter != null)
                        {
                            Log.e("Response", "If Condition");
//                        collectionPhotosAdapter.notifyItemRangeInserted(photoList.size()-30,photoList.size());
                            collectionPhotosAdapter.notifyItemInserted(oldPhotoSize);
//                        collectionPhotosAdapter.notifyDataSetChanged();
                            progress_load_more.setVisibility(View.GONE);
                        } else {
                            Log.e("Response", "Else Condition");
                            setAdapter(photoList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Toast.makeText(activity, "Error Occur during loading Collection Photos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setAdapter(List<Photo> photoList)
    {
        linearLayoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        rc_collection_photos.setLayoutManager(linearLayoutManager);

        collectionPhotosAdapter = new CollectionPhotosAdapter(activity,photoList,this);
        rc_collection_photos.setAdapter(collectionPhotosAdapter);

        progressBar.setVisibility(View.GONE);
    }

    private void initViewListener()
    {
        ibtn_back.setOnClickListener(this);
        img_share.setOnClickListener(this);
        img_website.setOnClickListener(this);
        rc_collection_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibtn_back:
                onBackPressed();
                break;
            case R.id.img_share:
                shareCollection();
                break;
            case R.id.img_website:
                openWeb();
                break;
        }
    }

    private void openWeb()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(collection.getLinks().getHtml()+ WallpaperApplication.UNSPLASH_UTM_PARAMETERS));
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    private void shareCollection()
    {
        if(collection!=null)
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.unsplash_collection));
            share.putExtra(Intent.EXTRA_TEXT, collection.getLinks().getHtml()+ WallpaperApplication.UNSPLASH_UTM_PARAMETERS);

            startActivity(Intent.createChooser(share, getString(R.string.share_via)));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCollectionPhotoClick(int position, String photo_id, Drawable drawable)
    {
        WallpaperApplication.getWallpaperApplication().setDrawable(drawable);

        Intent intent = new Intent(activity, FeaturedPhotoActivity.class);
        intent.putExtra("imageId",photo_id);
        intent.putExtra("photo",new Gson().toJson(photoList.get(position)));
        startActivity(intent);
    }
}
