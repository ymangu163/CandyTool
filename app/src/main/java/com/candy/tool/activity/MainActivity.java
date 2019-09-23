package com.candy.tool.activity;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;

import com.candy.tool.R;
import com.candy.tool.adapter.ViewPagerAdapter;
import com.candy.tool.bean.Upgrade;
import com.candy.tool.dialog.UpgradeDialog;
import com.candy.tool.fragment.CandyMainFragment;
import com.candy.tool.fragment.MyInfoFragment;
import com.candy.tool.fragment.RecommendFragment;
import com.candy.tool.utils.AppUtil;
import com.candy.tool.utils.StatConstant;
import com.candy.tool.utils.StatUtil;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.CommonSharePref;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private CandyMainFragment mCandyFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        mBottomNavigationView = findViewById(R.id.bottom_nv);
        mViewPager = findViewById(R.id.main_viewpager);
        initListener();
    }

    @Override
    public void initData() {
        checkAppUpdate();
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        //系统默认选中第一个,但是系统选中第一个不执行onNavigationItemSelected(MenuItem)方法,如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mBottomNavigationView.setSelectedItemId(R.id.tab_candy);//根据具体情况调用
        mViewPager.addOnPageChangeListener(this);
        //为viewpager设置adapter
        mCandyFragment = new CandyMainFragment();
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(mCandyFragment);
        pagerAdapter.addFragment(new RecommendFragment());
        pagerAdapter.addFragment(new MyInfoFragment());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
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
            case R.id.tab_my:
                clickTabThree();
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
                StatUtil.onEvent(StatConstant.CANDY_RECOMMEND);
                break;
            case 2:
                mBottomNavigationView.setSelectedItemId(R.id.tab_my);
                StatUtil.onEvent(StatConstant.TAB_MY);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void refreshCandys() {
        mCandyFragment.refreshSysData();
    }

    private void clickTabOne() {
        //为防止隔页切换时,滑过中间页面的问题,去除页面切换缓慢滑动的动画效果
        mViewPager.setCurrentItem(0, false);
    }

    private void clickTabTwo() {
        mViewPager.setCurrentItem(1, false);
    }

    private void clickTabThree() {
        mViewPager.setCurrentItem(2, false);
    }


    private void checkAppUpdate() {
        if (CommonSharePref.getInstance(this).getUpgradeTime() - System.currentTimeMillis() < 48 * 3600 * 1000) {
            return;
        }

        BmobQuery<Upgrade> query = new BmobQuery<Upgrade>();
        query.getObject("2yCseeel", new QueryListener<Upgrade>() {

            @Override
            public void done(Upgrade bean, BmobException e) {
                if (e != null) {
                    return;
                }
                int currVerionCode = AppUtil.getVersionCode(AppContext.getInstance());
                if (bean.getVersioncode() > currVerionCode) {
                    showUpgradeDlg(bean);
                }
            }
        });
    }

    private void showUpgradeDlg(Upgrade bean) {
        UpgradeDialog upgradeDialog = new UpgradeDialog(this);
        upgradeDialog.setDownloadUrl(bean.getDownload());
        upgradeDialog.setMsgStr(bean.getDescription());
        upgradeDialog.setCancelable(!bean.isForce());
        upgradeDialog.show();
        CommonSharePref.getInstance(this).setUpgradeTime(System.currentTimeMillis());
    }
}