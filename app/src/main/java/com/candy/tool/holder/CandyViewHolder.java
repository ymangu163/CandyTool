package com.candy.tool.holder;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.candy.tool.R;

/**
 * File description
 *
 * @author gao
 * @date 2018/2/27
 */

public class CandyViewHolder  extends RecyclerView.ViewHolder {
    private final TextView mTitleTv;
    private final TextView mDescriptionTv;
    public final CardView mCardView;
    private final TextView mHeadTv;

    public CandyViewHolder(View itemView) {
        super(itemView);
        mTitleTv = itemView.findViewById(R.id.title_tv);
        mDescriptionTv = itemView.findViewById(R.id.description_tv);
        mCardView = itemView.findViewById(R.id.item_card);
        mHeadTv = itemView.findViewById(R.id.mark_tv);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
        mHeadTv.setText(title.substring(0,1));
    }

    public void setDescription(String description) {
        mDescriptionTv.setText(description);
    }

    public void setOfficialHead() {
        mHeadTv.setBackgroundResource(R.drawable.shape_head_official);
    }
}