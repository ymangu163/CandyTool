package com.candy.tool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.candy.tool.utils.GsonUtil;
import com.tool.librecycle.utils.CommonSharePref;
import com.tool.librecycle.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

public class CandyUserFragment extends Fragment {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CandyAdapter mCandyAdapter;
    private CommonSharePref mSharePref;
    private int mLimit = 10; // 每页的数据是10条
    private int mCurPage = 0; // 当前页的编号，从0开始
    private boolean mLoadedAll;
    private int mLastPage;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            refreshStat();
            return false;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candy, container, false);
        mRefreshLayout = rootView.findViewById(R.id.candy_refresh);
        mRecyclerView = rootView.findViewById(R.id.candy_recycler);
        initRefreshLayout();
        initRecyclerView();
        initData();
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePref = CommonSharePref.getInstance(getContext());
    }

    private void initData() {
        String candyString = mSharePref.getRefreshResult();
        List<CandyBean> list = GsonUtil.gson2List(candyString, CandyBean.class);
        if (list == null || list.isEmpty()) {
            mRefreshLayout.setRefreshing(true);
            queryData(0, CandyAdapter.REQUEST_REFRESH, true);
            return;
        }
        mCandyAdapter.clearList();
        // 将本次查询的数据添加到bankCards中
        mCandyAdapter.addList(list);
        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
        mCurPage++;
        mCandyAdapter.notifyDataSetChanged();
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
                queryData(0, CandyAdapter.REQUEST_REFRESH, true);
            }
        });
    }

    public void refreshSysData() {
        queryData(0, CandyAdapter.REQUEST_REFRESH, false);
    }

    private void queryData(int page, final int actionType, boolean useCache) {
        BmobQuery<CandyBean> query = new BmobQuery<>();
        query.order("-updatedAt");
        // 处理时间查询
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        if (actionType == CandyAdapter.REQUEST_REFRESH) {
            query.setSkip(0);
            mLoadedAll = false;
            if (useCache) {
                String dateNowStr = dateFormat.format(new Date());
                dateNowStr = dateNowStr.substring(0, 17) + "00";
                try {
                    date = dateFormat.parse(dateNowStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                date = new Date();
            }
            query.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(date));
        } else {
            if (page == mLastPage) {
                return;
            }
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * mLimit);
            mLastPage = page;
        }
        // 设置每页数据个数
        query.setLimit(mLimit);

        //先判断是否有缓存
        boolean isCache = query.hasCachedResult(CandyBean.class);
        if (isCache && useCache) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 先从网络读取数据，如果没有，再从缓存中获取。
        }

        //设定15秒超时
        mHandler.sendEmptyMessageDelayed(0, 15000);
        // 查找数据
        query.findObjects(new FindListener<CandyBean>() {
            @Override
            public void done(List<CandyBean> list, BmobException e) {
                if (e != null) {
                    refreshStat();
                    ToastUtils.showToastForShort(getContext(), getString(R.string.candy_fail));
                    return;
                }

                if (actionType == CandyAdapter.REQUEST_LOADMORE
                        && (list == null || list.size() == 0)) {
                    refreshStat();
                    ToastUtils.showToastForShort(getContext(), getString(R.string.load_more_end));
                    mLoadedAll = true;
                    return;
                }
                if (actionType == CandyAdapter.REQUEST_REFRESH) {
                    // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                    mCandyAdapter.clearList();
                    mCurPage = 0;
                    // 获取最后时间
                    mSharePref.setCandyFirstTime(list.get(0).getCreatedAt());
                    mSharePref.setRefreshResult(GsonUtil.getGsonString(list));
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
        } else {
            mCandyAdapter.finishLoad();
        }
        if (mCandyAdapter.mIsLoadMore) {
            mCandyAdapter.finishLoad();
        }
    }

    private void handleItemClick(CandyBean candy) {
        Intent intent = new Intent(getActivity(), CandyDetailActivity.class);
        intent.putExtra("candy", candy);
        startActivity(intent);
    }

    private void initRecyclerView() {
        mCandyAdapter = new CandyAdapter<CandyBean>(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                        && mLastVisibleItemPosition >= mCandyAdapter.getItemCount() - 3) {
                    if (mRefreshLayout.isRefreshing()) {
                        mCandyAdapter.notifyItemRemoved(mCandyAdapter.getItemCount());
                        return;
                    }

                    if (mLoadedAll) {
                        ToastUtils.showToastForShort(getContext(), getString(R.string.candy_fail));
                        return;
                    }

                    mCandyAdapter.startLoad();
                    queryData(mCurPage, CandyAdapter.REQUEST_LOADMORE, true);
                }
                mLastVisibleItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }


        });

        mCandyAdapter.setOnItemClickListener(new CandyAdapter.OnItemClickListener<CandyBean>() {
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
