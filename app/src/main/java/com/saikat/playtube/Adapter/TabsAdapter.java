package com.saikat.playtube.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.saikat.playtube.Fragments.ChannelVideosFragment;
import com.saikat.playtube.Fragments.PlayListVideoFragment;


public class TabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;

    public TabsAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ChannelVideosFragment();
            case 1:
                return new PlayListVideoFragment();
            default:
                return new ChannelVideosFragment();

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
