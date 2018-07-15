package com.candy.tool.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.bean.FaqEntry;

import java.util.ArrayList;
import java.util.List;


public class FaqAdapter extends BaseExpandableListAdapter {
    private List<FaqEntry> mFaqList = new ArrayList<>();

    private LayoutInflater mInflater;

    public FaqAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setFaqList(List<FaqEntry> list) {
        mFaqList.clear();
        mFaqList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mFaqList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFaqList.size() == 0 ? 0 : 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return mFaqList.size() == 0 ? null : mFaqList.get(groupPosition).getQ();
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mFaqList.size() == 0 ? null : mFaqList.get(childPosition).getA();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String content = mFaqList.get(groupPosition).getQ();
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = mInflater.inflate(R.layout.item_ac_faq_question, null);
            holder.groupTitle = convertView.findViewById(R.id.tv_faq_question);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        holder.groupTitle.setText(content);
        int drawableId = R.drawable.ic_ac_fb_collapse;
        if (isExpanded) {
            drawableId = R.drawable.ic_ac_fb_expand;
        }
        holder.groupTitle.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
        return convertView;
    }

    private static class GroupViewHolder {
        private TextView groupTitle;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String content = mFaqList.get(groupPosition).getA();

        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.item_ac_faq_answer, null);
            holder.childContext = convertView.findViewById(R.id.tv_faq_answer);
            holder.childContext.setMovementMethod(LinkMovementMethod.getInstance());

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        holder.childContext.setText(Html.fromHtml(content));
        return convertView;
    }

    private static class ChildViewHolder {
        private TextView childContext;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}