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
import com.hd.wallpaper.background.Model.Collection;
import com.hd.wallpaper.background.R;

import java.util.List;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class UserCollectionPhotoAdapter extends RecyclerView.Adapter<UserCollectionPhotoAdapter.MyViewHolder>
{
    private Context context;
    private List<Collection> userCollections;
    private CollectionPhotoClick collectionPhotoClick;

    public UserCollectionPhotoAdapter(Context context, List<Collection> userCollections, CollectionPhotoClick collectionPhotoClick)
    {
        this.context = context;
        this.userCollections = userCollections;
        this.collectionPhotoClick = collectionPhotoClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_collection_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        final Collection collection = userCollections.get(holder.getAdapterPosition());

        DisplayMetrics displaymetrics = WallpaperApplication.getInstance().getResources().getDisplayMetrics();

        float finalHeight = displaymetrics.widthPixels / (collection.getCover_photo().getWidth()/collection.getCover_photo().getHeight());

        ViewPropertyTransition.Animator fadeAnimation = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(500);
                fadeAnim.start();
            }
        };

        holder.txt_collectionName.setText(collection.getTitle());
        holder.txt_photos_count.setText(String.valueOf(collection.getTotal_photos())+" Photos");

        Glide.with(context)
                .load(collection.getCover_photo().getUrls().getRegular())
                .transition(GenericTransitionOptions.with(fadeAnimation))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                /*.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .fitCenter()
                .apply(new RequestOptions()
                                .priority(Priority.HIGH)
//                        .placeholder(new ColorDrawable(R.attr.colorPrimary))
                                .placeholder(new ColorDrawable(context.getResources().getColor(R.color.white)))
                )
                .transition(withCrossFade())*/
                .into(holder.img_collection);
        holder.img_collection.setMinimumHeight((int)finalHeight);
        holder.img_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collectionPhotoClick!=null)
                {
                    collectionPhotoClick.onCollectionPhotoClick(holder.getAdapterPosition(),holder.img_collection.getDrawable(),collection.getTotal_photos());
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return userCollections.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_collectionName,txt_photos_count;
        ImageView img_collection;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            txt_collectionName = itemView.findViewById(R.id.txt_collectionName);
            txt_photos_count = itemView.findViewById(R.id.txt_photos_count);
            img_collection = itemView.findViewById(R.id.img_collection);
        }
    }

    public interface CollectionPhotoClick
    {
        void onCollectionPhotoClick(int position, Drawable drawable,int totalPhotos);
    }
}
