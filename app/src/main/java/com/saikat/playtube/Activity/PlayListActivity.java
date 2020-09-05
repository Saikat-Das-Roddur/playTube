package com.saikat.playtube.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.saikat.playtube.Adapter.TabsAdapter;
import com.saikat.playtube.Adapter.VideoAdapter;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.R;


import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {


    TabLayout tabLayout;
    ViewPager viewPager;
    String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPagerTab);
        tabLayout.addTab(tabLayout.newTab().setText("Channel Video"));
        tabLayout.addTab(tabLayout.newTab().setText("Play List Video"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener( this);
        //channelId = getIntent().getStringExtra("channelId");




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


}
