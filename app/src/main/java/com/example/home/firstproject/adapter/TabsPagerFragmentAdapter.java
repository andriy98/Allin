package com.example.home.firstproject.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.home.firstproject.fragment.FB_friends_fr;
import com.example.home.firstproject.fragment.TW_feed_fragment;
import com.example.home.firstproject.fragment.TW_wall_fragment;
import com.example.home.firstproject.fragment.VK_newsfeed_fragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter  {
    private String[] tabs;



    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[]{
                "",
                "",
                "",
                "",
        };
    }




    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return VK_newsfeed_fragment.getInstance();
            case 1 : return TW_wall_fragment.getInstance();
            case 2 : return TW_feed_fragment.getInstance();
            case 3 : return FB_friends_fr.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
