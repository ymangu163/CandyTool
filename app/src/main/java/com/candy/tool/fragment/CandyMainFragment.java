package com.candy.tool.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candy.tool.R;
import com.candy.tool.adapter.ViewPagerAdapter;
import com.candy.tool.view.TabTitleView;

/**
 * File description
 *
 * @author gao
 * @date 2018/6/20
 */

public class CandyMainFragment extends Fragment {

    private View mRootView;
    private CandyUserFragment mCandyFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_candy_main, null);
        initView();

        return mRootView;
    }

    private void initView() {
        final TabLayout mTabLayout = mRootView.findViewById(R.id.candy_tab_layout);
        ViewPager mViewPager = mRootView.findViewById(R.id.candy_viewpager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setCustomView(new TabTitleView(getActivity(), getString(R.string.title_user_candy)));
        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setCustomView(new TabTitleView(getActivity(), getString(R.string.title_official_candy)));
        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);

        mCandyFragment = new CandyUserFragment();
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(mCandyFragment);
        pagerAdapter.addFragment(new CandyOfficialFragment());
        mViewPager.setAdapter(pagerAdapter);

    }

}
