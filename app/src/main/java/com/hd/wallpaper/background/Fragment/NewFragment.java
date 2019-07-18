package com.hd.wallpaper.background.Fragment;


import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hd.wallpaper.background.Activity.PhotoActivity;
import com.hd.wallpaper.background.Activity.WallpaperApplication;
import com.hd.wallpaper.background.Adapter.ListOfPhotosAdapter;
import com.hd.wallpaper.background.Api.PhotoApi;
import com.hd.wallpaper.background.Helper.NetworkStateReceiver;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment implements ListOfPhotosAdapter.photoClick,NetworkStateReceiver.NetworkStateReceiverListener
{

    private View rootView;
    private RecyclerView rc_photos;
    private ListOfPhotosAdapter listOfPhotosAdapter;
    private int page = 1;
    private ProgressBar progress_load_more;
    private List<Photo> photoArrayList;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout ly_progressbar,ly_details,ly_error;
    private PhotoApi photoApi;
    private NetworkStateReceiver networkStateReceiver;
    private boolean networkFlag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setUpWindowAnimations();
    }

    private void setUpWindowAnimations()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new,container,false);
        initView();
        initViewAction();
        initViewListener();
        return rootView;
    }

    private void initView()
    {
        rc_photos = rootView.findViewById(R.id.rc_photos);
        rc_photos.setNestedScrollingEnabled(false);
        rc_photos.getItemAnimator().setChangeDuration(0);
        ly_details = rootView.findViewById(R.id.ly_details);
        ly_progressbar = rootView.findViewById(R.id.ly_progressbar);
        progress_load_more = rootView.findViewById(R.id.progress_load_more);
        ly_error = rootView.findViewById(R.id.ly_error);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rc_photos.setLayoutManager(linearLayoutManager);
        photoArrayList = new ArrayList<>();
    }

    private void initViewAction()
    {
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
        Call<ArrayList<Photo>> call = photoApi.getAllPhotos(page,30,"latest");
        call.enqueue(new Callback<ArrayList<Photo>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response)
            {
                if(response.isSuccessful())
                {
                    ly_error.setVisibility(View.GONE);
                    photoArrayList.addAll(response.body());
                    page+=1;
                    if(listOfPhotosAdapter!=null)
                    {
                        listOfPhotosAdapter.notifyItemInserted(photoArrayList.size()-30);
                        progress_load_more.setVisibility(View.GONE);
                    }
                    else
                    {
                        setAdapter(photoArrayList);
                    }
                }
                else
                {
                    Log.e("response","error in response"+response.toString());
                    Toast.makeText(getActivity(), "response error" + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t)
            {
                ly_progressbar.setVisibility(View.GONE);
                ly_details.setVisibility(View.GONE);
                ly_error.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setAdapter(List<Photo> photoArrayList)
    {

        Log.e("photoArrayList","photoArrayList size:"+photoArrayList.size());
        listOfPhotosAdapter = new ListOfPhotosAdapter(getActivity(),photoArrayList,this);
        rc_photos.setAdapter(listOfPhotosAdapter);
        ly_details.setVisibility(View.VISIBLE);
        ly_progressbar.setVisibility(View.GONE);
        ly_error.setVisibility(View.GONE);
    }

    private void initViewListener()
    {
        rc_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("imageId",image_id);
        intent.putExtra("photo",new Gson().toJson(photoArrayList.get(position)));
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
    public void onDestroy()
    {
        networkStateReceiver.removeListener(this);
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        networkStateReceiver.removeListener(this);
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    public void networkAvailable()
    {
        if(!networkFlag)
        {
            networkFlag = true;
            listOfPhotosAdapter = null;
            page = 1;
            photoArrayList.clear();
            displayConnect();
            getData();
        }
    }

    @Override
    public void networkUnavailable()
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
}
