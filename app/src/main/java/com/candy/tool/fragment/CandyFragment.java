package com.candy.tool.fragment;

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
import com.candy.tool.adapter.CandyAdapter;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candy, container, false);
        mRefreshLayout = rootView.findViewById(R.id.candy_refresh);
        mRecyclerView = rootView.findViewById(R.id.candy_recycler);
        initRefreshLayout();
        initRecyclerView();
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                requestData(CandyAdapter.REQUEST_REFRESH);
            }
        });
    }

    private void requestData(int code) {
        if (code == CandyAdapter.REQUEST_REFRESH) {
            mCandyAdapter.clearList();
            mCount = 0;
            for (int i = 0; i < 10; i++) {
                mCount += 1;
                mCandyAdapter.addItemData(mCount);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                mCount += 1;
                mCandyAdapter.addItemData(mCount);
            }
        }
        mCandyAdapter.notifyDataSetChanged();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);

        }
    }

    private void initRecyclerView() {
        mCandyAdapter = new CandyAdapter(getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mCandyAdapter.setLayoutManager(layoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mCandyAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

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
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCandyAdapter.startLoad();
                            requestData(CandyAdapter.REQUEST_LOADMORE);
                        }
                    },3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });

    }

}
