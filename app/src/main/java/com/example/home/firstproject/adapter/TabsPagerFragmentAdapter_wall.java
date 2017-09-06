package com.example.home.firstproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.home.firstproject.fragment.FB_wall_fragment;
import com.example.home.firstproject.fragment.TW_wall_fragment;
import com.example.home.firstproject.fragment.VK_wall_fragment;

public class TabsPagerFragmentAdapter_wall   extends FragmentPagerAdapter {
    private String[] tabs;



    public TabsPagerFragmentAdapter_wall(FragmentManager fm) {
        super(fm);
        tabs = new String[]{
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
            case 0 : return VK_wall_fragment.getInstance();
            case 1 : return FB_wall_fragment.getInstance();
            case 2 : return TW_wall_fragment.getInstance();
            default:return null;
        }

    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
