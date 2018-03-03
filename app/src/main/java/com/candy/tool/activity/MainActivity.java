package com.candy.tool.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.adapter.ViewPagerAdapter;
import com.candy.tool.fragment.CandyFragment;
import com.candy.tool.fragment.RecommendFragment;
import com.tool.librecycle.activity.BaseActivity;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private TextView mTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        mTitle = findViewById(R.id.title);
        mBottomNavigationView = findViewById(R.id.bottom_nv);
        mViewPager = findViewById(R.id.main_viewpager);
        initListener();
    }

    @Override
    public void initData() {

    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        //系统默认选中第一个,但是系统选中第一个不执行onNavigationItemSelected(MenuItem)方法,如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mBottomNavigationView.setSelectedItemId(R.id.tab_candy);//根据具体情况调用
        mViewPager.addOnPageChangeListener(this);
        //为viewpager设置adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new CandyFragment());
        pagerAdapter.addFragment(new RecommendFragment());
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //BottomNaviationView和ViewPager联动,当BottomNaviationView的某个tab按钮被选中了,同时设置ViewPager对应的页面被选中
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.tab_candy:
                clickTabOne();
                return true;//返回true,否则tab按钮不变色,未被选中
            case R.id.tab_recommend:
                clickTabTwo();
                return true;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //ViewPager和BottomNaviationView联动,当ViewPager的某个页面被选中了,同时设置BottomNaviationView对应的tab按钮被选中
        switch (position) {
            case 0:
                mBottomNavigationView.setSelectedItemId(R.id.tab_candy);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.tab_recommend);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void clickTabOne() {
        //为防止隔页切换时,滑过中间页面的问题,去除页面切换缓慢滑动的动画效果
        mViewPager.setCurrentItem(0, false);
        mTitle.setText("One");
    }

    private void clickTabTwo() {
        mViewPager.setCurrentItem(1, false);
        mTitle.setText("Two");
    }

    private void clickTabThree() {
        mViewPager.setCurrentItem(2, false);
        mTitle.setText("Three");
    }
}