package com.candy.tool.holder;

import android.support.v7.widget.RecyclerView;
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
    public TextView textView;
    public CandyViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textItem);
    }


}