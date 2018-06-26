package com.candy.tool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candy.tool.R;
import com.candy.tool.activity.AboutRecommendActivity;
import com.candy.tool.activity.AboutUsActivity;
import com.candy.tool.activity.FeedbackActivity;
import com.tool.librecycle.utils.ToastUtils;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class MyInfoFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        rootView.findViewById(R.id.my_recommend_tv).setOnClickListener(this);
        rootView.findViewById(R.id.recommend_state_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_about_tv).setOnClickListener(this);
        rootView.findViewById(R.id.feedback_tv).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.my_recommend_tv) {
            ToastUtils.showToastForShort(getContext(), "敬请期待...");
        } else if (vId == R.id.recommend_state_tv) {
            Intent intent = new Intent(getActivity(), AboutRecommendActivity.class);
            startActivity(intent);
        } else if (vId == R.id.my_about_tv) {
            Intent intent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(intent);
        } else if (vId == R.id.feedback_tv) {
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
        }


    }
}
