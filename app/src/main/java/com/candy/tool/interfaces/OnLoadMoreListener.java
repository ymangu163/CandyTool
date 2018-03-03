package com.candy.tool.interfaces;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * File description
 *
 * @author gao
 * @date 2018/2/27
 */

public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener{
    private int mCountItem;
    private int mLastItem;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 加载接口
     *
     * @param countItem 总数量
     * @param lastItem  最后显示的position
     */
    protected abstract void onLoading(int countItem, int lastItem);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //拖拽或者惯性滑动时isScolled设置为true
//        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
//            mIsScolled = true;
//        } else {
//            mIsScolled = false;
//        }

        if (newState == RecyclerView.SCROLL_STATE_IDLE
                && mLastItem == mCountItem - 1) {

            onLoading(mCountItem, mLastItem);
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutManager = recyclerView.getLayoutManager();
            mCountItem = mLayoutManager.getItemCount();
            mLastItem = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
        }
    }
}
