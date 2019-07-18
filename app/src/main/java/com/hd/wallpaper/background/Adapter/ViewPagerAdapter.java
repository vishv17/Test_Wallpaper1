package com.hd.wallpaper.background.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hd.wallpaper.background.Fragment.CollectionFragment;
import com.hd.wallpaper.background.Fragment.FeaturedFragment;
import com.hd.wallpaper.background.Fragment.NewFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new NewFragment();
            case 1:
                return new FeaturedFragment();
            case 2:
                return new CollectionFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
