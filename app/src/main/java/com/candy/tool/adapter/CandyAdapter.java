package com.candy.tool.adapter;

import android.content.Context;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candy.tool.R;
import com.candy.tool.bean.CandyBean;
import com.candy.tool.bean.OfficialCandy;
import com.candy.tool.holder.CandyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * File description
 *
 * @author gao
 * @date 2018/2/26
 */

public class CandyAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_CONTENT = 0;//正常内容
    private final static int TYPE_FOOTER = 1;//下拉刷新
    public final static int REQUEST_REFRESH = 1;//更新
    public final static int REQUEST_LOADMORE = 2;//加载更多
    private List<T> mListData = new ArrayList<>();
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    public boolean mIsLoadMore;

    public CandyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_main_footer, parent, false);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_candy_item, parent, false);
            CandyViewHolder myViewHolder = new CandyViewHolder(view);
            return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CandyViewHolder) {
            CandyViewHolder viewHolder = (CandyViewHolder) holder;
            T tBean = mListData.get(position);
            if (tBean instanceof CandyBean) {
                CandyBean candyBean = (CandyBean) tBean;
                viewHolder.setTitle(candyBean.getName());
                viewHolder.setDescription(candyBean.getDescription());
                if (tBean instanceof OfficialCandy) {
                    viewHolder.setOfficialHead();
                }
            }
            if (onItemClickListener != null) {
                viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(mListData.get(position));
                    }
                });
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && mIsLoadMore) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        if (mIsLoadMore) {
            return mListData.size() + 1;
        }
        return mListData.size();
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public void setListData(List<T> data) {
        mListData.clear();
        mListData.addAll(data);
    }

    public void addItemData(T item) {
        mListData.add(item);
    }

    public void addList(List<T> data) {
        mListData.addAll(data);
    }

    public void clearList() {
        mListData.clear();
    }

    public void startLoad() {
        mIsLoadMore = true;
        notifyDataSetChanged();
    }

    public void finishLoad() {
        mIsLoadMore = false;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T candy);

        void onItemLongClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
        }
    }
}
