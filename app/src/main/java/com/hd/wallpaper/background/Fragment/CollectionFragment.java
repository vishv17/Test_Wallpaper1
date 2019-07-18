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
import com.hd.wallpaper.background.Activity.PhotosCollectionActivity;
import com.hd.wallpaper.background.Adapter.ListofFeaturedCollectionAdapter;
import com.hd.wallpaper.background.Api.CollectionApi;
import com.hd.wallpaper.background.Helper.NetworkStateReceiver;
import com.hd.wallpaper.background.Model.Collection;
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
public class CollectionFragment extends Fragment implements ListofFeaturedCollectionAdapter.CollectionClick,NetworkStateReceiver.NetworkStateReceiverListener
{

    private RecyclerView rc_featured_collection;
    private ProgressBar progress_load_more;
    private ProgressDialog progressDialog;
    private View rootView;
    private List<Collection> collectionList;
    private CollectionApi collectionApi;
    private int page,per_page;
    private ListofFeaturedCollectionAdapter featuredCollectionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;
    private LinearLayout ly_details,ly_progressbar,ly_error;
    private NetworkStateReceiver networkStateReceiver;
    private boolean networkFlag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        initView();
        initViewAction();
        initViewListener();
        return rootView;
    }

    private void initView()
    {
        collectionList = new ArrayList<>();
        rc_featured_collection = rootView.findViewById(R.id.rc_featured_collection);
        rc_featured_collection.setNestedScrollingEnabled(false);
        progress_load_more = rootView.findViewById(R.id.progress_load_more);
        ly_progressbar = rootView.findViewById(R.id.ly_progressbar);
        ly_details = rootView.findViewById(R.id.ly_details);
        ly_error = rootView.findViewById(R.id.ly_error);
        page = 1;
        per_page = 10;

    }

    private void initViewAction()
    {
        collectionApi = Unsplash.getRetrofit().create(CollectionApi.class);
        getData();
    }

    private void getData()
    {
        if(page==1)
        {
            /*progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();*/

            ly_progressbar.setVisibility(View.VISIBLE);
            ly_details.setVisibility(View.GONE);
        }
        else
        {
            ly_progressbar.setVisibility(View.GONE);
            ly_details.setVisibility(View.VISIBLE);
            progress_load_more.setVisibility(View.VISIBLE);
        }
        Call<List<Collection>> call = collectionApi.getFeaturedCollections(page,25);
        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response)
            {
                if(response.isSuccessful()) {
                    page += 1;
                    collectionList.addAll(response.body());
                    if (featuredCollectionAdapter != null) {
                        featuredCollectionAdapter.notifyItemChanged(collectionList.size() - 25);
                        progress_load_more.setVisibility(View.GONE);
                    } else {
                        setAdapter(collectionList);
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
            public void onFailure(Call<List<Collection>> call, Throwable t)
            {
                ly_progressbar.setVisibility(View.GONE);
                ly_details.setVisibility(View.GONE);
                ly_error.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter(List<Collection> collectionList)
    {
        linearLayoutManager =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rc_featured_collection.setLayoutManager(linearLayoutManager);
        Log.e("FeaturedCollection","Featured Collection List:"+collectionList.size());
        featuredCollectionAdapter = new ListofFeaturedCollectionAdapter(getActivity(),collectionList,this);
        rc_featured_collection.setAdapter(featuredCollectionAdapter);
        ly_details.setVisibility(View.VISIBLE);
        ly_progressbar.setVisibility(View.GONE);
        ly_error.setVisibility(View.GONE);
    }

    private void initViewListener()
    {
        rc_featured_collection.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
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
    public void onCollectionClick(int position, int image_id, Drawable drawable,int totalPhotos)
    {
        Intent intent = new Intent(getActivity(), PhotosCollectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("Collection",new Gson().toJson(collectionList.get(position)));
        intent.putExtra("totalPhotos",totalPhotos);
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
    public void onPause()
    {
        networkStateReceiver.removeListener(this);
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        networkStateReceiver.removeListener(this);
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onDestroy();
    }

    @Override
    public void networkAvailable()
    {
        if(!networkFlag)
        {
            networkFlag = true;
            featuredCollectionAdapter = null;
            page = 1;
            collectionList.clear();
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
