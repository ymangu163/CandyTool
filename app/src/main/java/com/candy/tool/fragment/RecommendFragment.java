package com.candy.tool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.activity.RewardActivity;
import com.candy.tool.bean.CandyBean;
import com.tool.librecycle.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/2/25
 */

public class RecommendFragment extends Fragment implements View.OnClickListener {

    private EditText mNameEdit;
    private EditText mUrlEdit;
    private EditText mDescriptionEdit;
    private TextView mRewardTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        mNameEdit = rootView.findViewById(R.id.coin_name_et);
        mUrlEdit = rootView.findViewById(R.id.coin_url_et);
        mDescriptionEdit = rootView.findViewById(R.id.coin_description_et);
        mRewardTv = rootView.findViewById(R.id.recommend_reward_tv);
        rootView.findViewById(R.id.coin_recommend_tv).setOnClickListener(this);
        mRewardTv.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.coin_recommend_tv) {
            submit();
        } else if (vId == R.id.recommend_reward_tv) {
            startActivity(new Intent(getActivity(), RewardActivity.class));
        }

    }

    private void submit() {
        String coinName = mNameEdit.getText().toString();
        String urlString = mUrlEdit.getText().toString();
        String desString = mDescriptionEdit.getText().toString();
        if (TextUtils.isEmpty(coinName)) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.hint_input_coin_name));
        } else if (TextUtils.isEmpty(urlString)) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.hint_input_url));
        } else if (TextUtils.isEmpty(desString) || desString.length() < 10) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.hint_input_description));
        }

        CandyBean candyBean = new CandyBean();
        //注意：不能调用gameScore.setObjectId("")方法
        candyBean.setName(coinName);
        candyBean.setUrl(urlString);
        candyBean.setDescription(desString);
        candyBean.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException exception) {
                if (exception == null) {
                    ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_success));
                    mNameEdit.setText("");
                    mUrlEdit.setText("");
                    mDescriptionEdit.setText("");
                    mRewardTv.setVisibility(View.VISIBLE);
                } else {
                    Log.i("bmob", "失败：" + exception.getMessage() + "," + exception.getErrorCode());
                    ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_fail));
                }
            }
        });

    }
}
