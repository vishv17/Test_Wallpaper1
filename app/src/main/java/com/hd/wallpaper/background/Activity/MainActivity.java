package com.hd.wallpaper.background.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.hd.wallpaper.background.Adapter.ViewPagerAdapter;
import com.hd.wallpaper.background.R;


public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private MainActivity activity;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;

        initView();
        initViewAction();
        initViewListener();
    }

    private void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initViewAction()
    {
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        tabLayout.addTab(tabLayout.newTab().setText("New"));
        tabLayout.addTab(tabLayout.newTab().setText("Featured"));
        tabLayout.addTab(tabLayout.newTab().setText("Collections"));

        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);
    }


    private void initViewListener()
    {
        viewPager.setOnPageChangeListener(this);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        tabLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
