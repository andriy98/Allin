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
import com.example.home.firstproject.adapter.TabsPagerFragmentAdapter;
import com.vk.sdk.VKSdk;

public class VK_feed_fragment extends Fragment{
    private static final int LAYOUT = R.layout.vk_feed_fr;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Integer[] social = new Integer[]{
            R.drawable.ic_tab_vk,
            R.drawable.ic_tab_fb,
            R.drawable.ic_tab_tw,
            R.drawable.ic_tab_inst,
    };
    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Новини");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        initTabs();
        return view;
    }

    private void initTabs() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        if(VKSdk.isLoggedIn()==true) {
            viewPager.setCurrentItem(0);   //задання вкладки по замовчуванню !
        }
        else
        {
            viewPager.setCurrentItem(3);
        }

        tabLayout.setupWithViewPager(viewPager);
        for (int i=0;i<tabLayout.getTabCount();i++) {
            tabLayout.getTabAt(i).setIcon(social[i]);
        }
    }
}
