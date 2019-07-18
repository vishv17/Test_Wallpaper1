package com.hd.wallpaper.background.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.hd.wallpaper.background.Activity.FeaturedPhotoActivity;
import com.hd.wallpaper.background.Activity.WallpaperApplication;
import com.hd.wallpaper.background.Adapter.ListOfFeaturedPhotoAdapter;
import com.hd.wallpaper.background.Api.PhotoApi;
import com.hd.wallpaper.background.Helper.NetworkStateReceiver;
import com.hd.wallpaper.background.Model.FeaturedPhotos;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.NetworkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedFragment extends Fragment implements ListOfFeaturedPhotoAdapter.photoClick , Connectable , Disconnectable , Bindable , NetworkStateReceiver.NetworkStateReceiverListener
{

    private View rootView;
    private RecyclerView rc_featured_photos;
    private ProgressBar progress_load_more;
    private int page = 1;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;
    private LinearLayoutManager linearLayoutManager;
    private List<FeaturedPhotos> featuredPhotosList;
    private PhotoApi photoApi;
    private ProgressDialog progressDialog;
    private ListOfFeaturedPhotoAdapter listOfFeaturedPhotoAdapter;
    private LinearLayout ly_progressbar,ly_details,ly_error;
//    private MerlinsBeard merlinsBeard;
//    private Merlin merlin;
    private NetworkStateReceiver networkStateReceiver;
    private boolean networkFlag;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*merlin = new Merlin.Builder().withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(getActivity());
        merlinsBeard = new MerlinsBeard.Builder().build(getActivity());
        merlin.bind();
        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);
        merlin.registerBindable(this);*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        initView();
        initViewAction();
        initViewListener();
        return rootView;
    }

    private void initView()
    {
        rc_featured_photos = rootView.findViewById(R.id.rc_featured_photos);
        progress_load_more = rootView.findViewById(R.id.progress_load_more);
        ly_details = rootView.findViewById(R.id.ly_details);
        ly_progressbar = rootView.findViewById(R.id.ly_progressbar);
        ly_error = rootView.findViewById(R.id.ly_error);
        featuredPhotosList = new ArrayList<>();
    }

    private void initViewAction()
    {
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rc_featured_photos.setLayoutManager(linearLayoutManager);
        photoApi = Unsplash.getRetrofit().create(PhotoApi.class);
        getData();
    }

    private void getData()
    {
        if(page == 1)
        {
            ly_progressbar.setVisibility(View.VISIBLE);
            ly_details.setVisibility(View.GONE);
        }
        else
        {
            ly_progressbar.setVisibility(View.GONE);
            ly_details.setVisibility(View.VISIBLE);
            progress_load_more.setVisibility(View.VISIBLE);
        }

        Call<List<FeaturedPhotos>> call = photoApi.getCuratedPhotos(page,10,"");
        call.enqueue(new Callback<List<FeaturedPhotos>>() {
            @Override
            public void onResponse(Call<List<FeaturedPhotos>> call, Response<List<FeaturedPhotos>> response)
            {
                if(response.isSuccessful())
                {
                    page+=1;
                    featuredPhotosList.addAll(response.body());
                    if(listOfFeaturedPhotoAdapter !=null)
                    {
                        listOfFeaturedPhotoAdapter.notifyItemRangeInserted(featuredPhotosList.size()-10, featuredPhotosList.size());
                        progress_load_more.setVisibility(View.GONE);
                    }
                    else
                    {
                        setAdapter(featuredPhotosList);
                    }
                }
                else
                {
                    ly_progressbar.setVisibility(View.GONE);
                    ly_details.setVisibility(View.GONE);
                    ly_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedPhotos>> call, Throwable t)
            {
                ly_progressbar.setVisibility(View.GONE);
                ly_details.setVisibility(View.GONE);
                ly_error.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter(List<FeaturedPhotos> photoArrayList)
    {

        Log.e("featuredPhotosList","featuredPhotosList size:"+photoArrayList.size());
        listOfFeaturedPhotoAdapter = new ListOfFeaturedPhotoAdapter(getActivity(),photoArrayList,this);
        rc_featured_photos.setAdapter(listOfFeaturedPhotoAdapter);
        ly_details.setVisibility(View.VISIBLE);
        ly_progressbar.setVisibility(View.GONE);
        ly_error.setVisibility(View.GONE);
    }

    private void initViewListener()
    {
        rc_featured_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    public void onPhotoClick(int position, String image_id, Drawable drawable)
    {
        WallpaperApplication.getWallpaperApplication().setDrawable(drawable);

        Intent intent = new Intent(getActivity(), FeaturedPhotoActivity.class);
        intent.putExtra("imageId",image_id);
        intent.putExtra("photo",new Gson().toJson(featuredPhotosList.get(position)));
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        /*if(NetworkManager.isInternetConnected(Objects.requireNonNull(getActivity())) || NetworkManager.isWifiConnected(getActivity()))
        {
            ly_error.setVisibility(View.GONE);
            ly_progressbar.setVisibility(View.VISIBLE);
        }
        else
        {
            ly_progressbar.setVisibility(View.GONE);
            ly_details.setVisibility(View.GONE);
            ly_error.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onConnect()
    {
        if(!networkFlag)
        {
            networkFlag = true;
            listOfFeaturedPhotoAdapter = null;
            displayConnect();
            page = 1;
            featuredPhotosList.clear();
            getData();
        }
    }

    @Override
    public void onDisconnect()
    {
        networkFlag = false;
        displayDisconnect();
    }

    private void displayConnect()
    {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly_error.setVisibility(View.GONE);
                ly_progressbar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void displayDisconnect()
    {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                ly_progressbar.setVisibility(View.GONE);
                ly_details.setVisibility(View.GONE);
                ly_error.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBind(NetworkStatus networkStatus)
    {
        if(!networkStatus.isAvailable())
        {
            onDisconnect();
        }
    }

    @Override
    public void onPause()
    {
//        merlin.unbind();
        networkStateReceiver.removeListener(this);
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
//        merlin.unbind();
        networkStateReceiver.removeListener(this);
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onDestroy();
    }




    @Override
    public void networkAvailable()
    {
        Log.e("Internet","Internet is Connected");
        listOfFeaturedPhotoAdapter = null;
        page = 1;
        featuredPhotosList.clear();
        displayConnect();
        getData();
    }

    @Override
    public void networkUnavailable()
    {
        Log.e("Internet","network is disconnected");
        displayDisconnect();
    }
}
