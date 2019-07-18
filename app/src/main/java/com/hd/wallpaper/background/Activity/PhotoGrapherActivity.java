package com.hd.wallpaper.background.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hd.wallpaper.background.Api.UserApi;
import com.hd.wallpaper.background.Model.Photographer;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;
import com.mikhaellopez.circularimageview.CircularImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PhotoGrapherActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView  img_website;
    private CircularImageView img_user_image;
    private TextView txt_user_name, txt_user_location, txt_description;
    private CardView cd_photos, cd_collection, cd_likes;
    private PhotoGrapherActivity activity;
    private ImageButton ibtn_back;
    private String user_name, name;
    private UserApi userApi;
    private Photographer photographer;
    private NestedScrollView nestedScroll;
    private LinearLayout ly_init_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grapher);
        activity = this;

        initView();
        initViewAction();
        initViewListener();
    }

    private void initView() {
        img_user_image = findViewById(R.id.img_user_image);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_user_location = findViewById(R.id.txt_user_location);
        txt_description = findViewById(R.id.txt_description);
        cd_photos = findViewById(R.id.cd_photos);
        cd_collection = findViewById(R.id.cd_collection);
        cd_likes = findViewById(R.id.cd_likes);
        img_website = findViewById(R.id.img_website);
        ibtn_back = findViewById(R.id.ibtn_back);
        nestedScroll = findViewById(R.id.nestedScroll);
        ly_init_progress = findViewById(R.id.ly_init_progress);
    }

    private void initViewAction()
    {
        user_name = getIntent().getStringExtra("userName");
        name = getIntent().getStringExtra("name");

        userApi = Unsplash.getRetrofit().create(UserApi.class);
        Call<Photographer> userApiCall = userApi.getPhotographerProfile(user_name);
        userApiCall.enqueue(new Callback<Photographer>() {
            @Override
            public void onResponse(Call<Photographer> call, Response<Photographer> response) {
                if (response.isSuccessful())
                {
                    photographer = response.body();

                    Glide.with(activity)
                            .asBitmap()
//                            .placeholder(R.drawable.error_drawable)
                            .load(photographer.getProfile_image().getLarge())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .fitCenter()
                            .apply(new RequestOptions()
                                    .priority(Priority.HIGH)
                                    .placeholder(new ColorDrawable(R.attr.colorPrimary))
                            )
//                                .transition(withCrossFade())
                            .into(img_user_image);
                    /*if (WallpaperApplication.getWallpaperApplication().getUser_drawable() != null)
                    {
                        img_user_image.setImageDrawable(WallpaperApplication.getWallpaperApplication().getUser_drawable());

                        WallpaperApplication.getWallpaperApplication().setUser_drawable(null);
                    }
                    else
                    {
                        Glide.with(activity)
                                .asBitmap()
//                            .placeholder(R.drawable.error_drawable)
                                .load(photographer.getProfile_image().getLarge())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .fitCenter()
                                .apply(new RequestOptions()
                                        .priority(Priority.HIGH)
                                        .placeholder(new ColorDrawable(R.attr.colorPrimary))
                                )
//                                .transition(withCrossFade())
                                .into(img_user_image);
                    }*/

                    txt_user_name.setText(photographer.getName());
                    txt_user_location.setText(photographer.getLocation() != null ? photographer.getLocation() : getString(R.string.unknown));
                    if(photographer.getBio()!=null)
                    {
                        txt_description.setVisibility(View.VISIBLE);
                        txt_description.setText(photographer.getBio().toString());
                    }
                    else
                    {
                        txt_description.setVisibility(View.GONE);
                    }

                    nestedScroll.setVisibility(View.VISIBLE);
                    ly_init_progress.setVisibility(View.GONE);
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<Photographer> call, Throwable t) {
                Toast.makeText(activity, "Some Error in PhotoGrapher Activity", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initViewListener() {
        ibtn_back.setOnClickListener(this);
        img_website.setOnClickListener(this);
        cd_photos.setOnClickListener(this);
        cd_likes.setOnClickListener(this);
        cd_collection.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                onBackPressed();
                break;
            case R.id.img_website:
                if(photographer!=null && photographer.getLinks()!=null && photographer.getLinks().getHtml()!=null)
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(photographer.getLinks().getHtml()+ WallpaperApplication.UNSPLASH_UTM_PARAMETERS));
                    if(intent.resolveActivity(getPackageManager())!=null)
                    {
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(activity, "Some error Occur", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(activity, "Some error Occur", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cd_photos:
                Intent intent = new Intent(activity,PhotographerPhotosActivity.class);
                intent.putExtra("photographerUsername",photographer.getUsername());
                intent.putExtra("totalPhotos",photographer.getTotal_photos());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.cd_likes:
                Intent intent1 = new Intent(activity,PhotographerLikedPhotosActivity.class);
                intent1.putExtra("photographerUsername",photographer.getUsername());
                intent1.putExtra("totalLikes",photographer.getTotal_likes());
                startActivity(intent1);
                break;
            case R.id.cd_collection:
                Intent intent2 = new Intent(activity,PhotographerCollectionActivity.class);
                intent2.putExtra("photographerUsername",photographer.getUsername());
                intent2.putExtra("totalCollection",photographer.getTotal_collections());
                startActivity(intent2);
                break;
        }
    }
}
