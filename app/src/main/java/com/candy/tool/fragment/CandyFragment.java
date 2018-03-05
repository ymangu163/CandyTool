package com.candy.tool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candy.tool.R;
import com.candy.tool.activity.CandyDetailActivity;
import com.candy.tool.adapter.CandyAdapter;
import com.candy.tool.bean.CandyBean;
import com.tool.librecycle.utils.CommonSharePref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/2/25
 */

public class CandyFragment extends Fragment {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CandyAdapter mCandyAdapter;
    private int mCount;
    private Handler mHandler = new Handler();
    private CommonSharePref mSharePref;
    private int mLimit = 9; // 每页的数据是10条
    private int mCurPage = 0; // 当前页的编号，从0开始

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candy, container, false);
        mRefreshLayout = rootView.findViewById(R.id.candy_refresh);
        mRecyclerView = rootView.findViewById(R.id.candy_recycler);
        initRefreshLayout();
        initRecyclerView();
        queryData(0, CandyAdapter.REQUEST_REFRESH);
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePref = CommonSharePref.getInstance(getContext());
    }

    private void initRefreshLayout() {
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_light),
                getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_light));
        //设置下拉时圆圈的背景颜色（这里设置成白色）
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //设置下拉刷新时的操作
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //具体操作
                queryData(0, CandyAdapter.REQUEST_REFRESH);
            }
        });
    }

    private void queryData(int page, final int actionType) {
        BmobQuery<CandyBean> query = new BmobQuery<>();
        query.order("-createdAt");

        if (actionType == CandyAdapter.REQUEST_REFRESH) {
            query.setSkip(0);
        } else {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(mSharePref.getCandyLastTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * mLimit + 1);
        }
        // 设置每页数据个数
        query.setLimit(mLimit);

        //先判断是否有缓存
        boolean isCache = query.hasCachedResult(CandyBean.class);
        if (isCache) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 先从网络读取数据，如果没有，再从缓存中获取。
        }

        // 查找数据
        query.findObjects(new FindListener<CandyBean>() {
            @Override
            public void done(List<CandyBean> list, BmobException e) {
                if (e != null || list == null || list.size() == 0) {
                    refreshStat();
                    return;
                }
                if (actionType == CandyAdapter.REQUEST_REFRESH) {
                    // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                    mCandyAdapter.clearList();
                    mCount = 0;
                    mCurPage = 0;
                    // 获取最后时间
                    mSharePref.setCandyFirstTime(list.get(0).getCreatedAt());
                }

                // 将本次查询的数据添加到bankCards中
                mCandyAdapter.addList(list);
                mSharePref.setCandyLastTime(list.get(list.size() - 1).getCreatedAt());

                // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                mCurPage++;
                refreshStat();
            }

        });

    }

    private void refreshStat() {
        mCandyAdapter.notifyDataSetChanged();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);

        }
    }

    private void handleItemClick(CandyBean candy) {
        Intent intent = new Intent(getActivity(), CandyDetailActivity.class);
        intent.putExtra("candy", candy);
        startActivity(intent);
    }

    private void initRecyclerView() {
        mCandyAdapter = new CandyAdapter(getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mCandyAdapter.setLayoutManager(layoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mCandyAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int mLastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItemPosition == mCandyAdapter.getItemCount() - 1) {
                    if (mRefreshLayout.isRefreshing()) {
                        mCandyAdapter.notifyItemRemoved(mCandyAdapter.getItemCount());
                        return;
                    }

                    mCandyAdapter.startLoad();
                    queryData(mCurPage, CandyAdapter.REQUEST_LOADMORE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });

        mCandyAdapter.setOnItemClickListener(new CandyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CandyBean candy) {
                handleItemClick(candy);
            }

            @Override
            public void onItemLongClick(int position) {

            }
        });

    }

}
