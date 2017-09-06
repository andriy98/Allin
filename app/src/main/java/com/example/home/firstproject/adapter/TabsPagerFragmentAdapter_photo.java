package com.example.home.firstproject.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.home.firstproject.fragment.FB_photo_fragment;
import com.example.home.firstproject.fragment.VK_photo_fragment;

public class TabsPagerFragmentAdapter_photo  extends FragmentPagerAdapter {
    private String[] tabs;



    public TabsPagerFragmentAdapter_photo (FragmentManager fm) {
        super(fm);
        tabs = new String[]{
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
            case 0 : return VK_photo_fragment.getInstance();
            case 1 : return FB_photo_fragment.getInstance();
            default:return null;
        }

    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
