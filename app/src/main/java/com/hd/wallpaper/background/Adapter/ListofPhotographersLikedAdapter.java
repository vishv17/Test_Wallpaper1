package com.hd.wallpaper.background.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.hd.wallpaper.background.Activity.WallpaperApplication;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.R;

import java.util.List;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ListofPhotographersLikedAdapter extends RecyclerView.Adapter<ListofPhotographersLikedAdapter.MyViewHolder>
{
    private Context context;
    private List<Photo> photoList;
    private OnPhotoClick onPhotoClick;

    public ListofPhotographersLikedAdapter(Context context, List<Photo> photoList, OnPhotoClick onPhotoClick)
    {
        this.context = context;
        this.photoList = photoList;
        this.onPhotoClick = onPhotoClick;
    }

    @NonNull
    @Override
    public ListofPhotographersLikedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_photo_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListofPhotographersLikedAdapter.MyViewHolder holder, int position)
    {
        final Photo photo = photoList.get(holder.getAdapterPosition());

        DisplayMetrics displaymetrics = WallpaperApplication.getInstance().getResources().getDisplayMetrics();
        float finalHeight = displaymetrics.widthPixels / ((float)photo.getWidth()/(float)photo.getHeight());

        ViewPropertyTransition.Animator fadeAnimation = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(500);
                fadeAnim.start();
            }
        };

        holder.txt_username.setText(photo.getUser().getName());

        Glide.with(context)
                .load(photo.getUrls().getRegular())
                .transition(GenericTransitionOptions.with(fadeAnimation))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                /*.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        holder.img_photo.setVisibility(View.GONE);
                        holder.ly_progressbar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                    {
                        holder.ly_progressbar.setVisibility(View.GONE);
                        holder.img_photo.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .placeholder(new ColorDrawable(R.attr.colorPrimary))
                )
                .transition(withCrossFade())*/
                .into(holder.img_photo);
        holder.img_photo.setMinimumHeight((int) finalHeight);
        holder.img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(onPhotoClick!=null)
                {
                    onPhotoClick.onClickPhoto(holder.getAdapterPosition(),photo.getId(),holder.img_photo.getDrawable(),photo);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return photoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_username;
        ImageView img_photo;
        LinearLayout ly_progressbar;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            img_photo = itemView.findViewById(R.id.img_photo);
            ly_progressbar = itemView.findViewById(R.id.ly_progressbar);
        }
    }

    public interface OnPhotoClick
    {
        void onClickPhoto(int position, String image_id, Drawable drawable, Photo photo);
    }
}
