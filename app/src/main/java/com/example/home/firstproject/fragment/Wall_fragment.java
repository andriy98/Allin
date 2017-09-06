package com.example.home.firstproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.firstproject.R;
import com.example.home.firstproject.adapter.TabsPagerFragmentAdapter_wall;


public class Wall_fragment extends Fragment {
    private static final int LAYOUT = R.layout.wall_fr;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Integer[] social;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Стіна");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout1);
        social = new Integer[]{
                R.drawable.ic_tab_vk,
                R.drawable.ic_tab_fb,
                R.drawable.ic_tab_tw,
                R.drawable.ic_tab_inst,
        };
        initTabs();
        return view;
    }

    private void initTabs() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager1);
        TabsPagerFragmentAdapter_wall adapter = new TabsPagerFragmentAdapter_wall(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        for (int i=0;i<tabLayout.getTabCount();i++){
            tabLayout.getTabAt(i).setIcon(social[i]);
        }
    }

}