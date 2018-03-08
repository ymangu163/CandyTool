package com.candy.tool.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.activity.MainActivity;
import com.candy.tool.activity.RewardActivity;
import com.candy.tool.bean.CandyBean;
import com.candy.tool.bean.InviteUrl;
import com.tool.librecycle.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private Dialog mProgressDialog;

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
        String coinName = mNameEdit.getText().toString().trim();
        String urlString = mUrlEdit.getText().toString().trim();
        String desString = mDescriptionEdit.getText().toString().trim();
        if (coinName.endsWith(getString(R.string.coin))) {
            coinName = coinName.substring(0, coinName.length() - 1);
        }
        if (TextUtils.isEmpty(coinName)) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.hint_input_coin_name));
            return;
        } else if (coinName.length() < 2 || coinName.length() > 8) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.coin_name_length));
            return;
        } else if (TextUtils.isEmpty(urlString)) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.hint_input_url));
            return;
        } else if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.url_format));
            return;
        } else if (TextUtils.isEmpty(desString) || desString.length() < 10) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.hint_input_description));
            return;
        }

        checkDatabase(coinName, urlString, desString);
    }

    private void saveRecommend(String coinName, String urlString, String desString) {
        int index = ordinalIndexOf(urlString, "/", 3);
        CandyBean candyBean = new CandyBean();
        candyBean.setName(coinName);
        candyBean.setDescription(desString);
        if (index == -1) {
            candyBean.setUrlPrefix(urlString);
        } else {
            candyBean.setUrlPrefix(urlString.substring(0, index));
            candyBean.add("inviteUrls", new InviteUrl(urlString.substring(index), -1));
        }
        candyBean.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException exception) {
                if (exception != null) {
                    ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_fail));
                    return;
                }
                ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_success));
                mNameEdit.setText("");
                mUrlEdit.setText("");
                mDescriptionEdit.setText("");
                mRewardTv.setVisibility(View.VISIBLE);
                hideProgressDialog();
                assert getActivity() != null;
                ((MainActivity) getActivity()).refreshCandys();
            }
        });
    }

    private int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(substr, pos + 1);
        }
        return pos;
    }

    private void checkDatabase(final String coinName, final String urlString, final String desString) {
        BmobQuery<CandyBean> query = new BmobQuery<>();
        int index = ordinalIndexOf(urlString, "/", 3);
        query.addWhereEqualTo("urlPrefix", urlString.substring(0, index));
        final String urlContent = urlString.substring(index);
        showProgressDialog();
        query.findObjects(new FindListener<CandyBean>() {

            @Override
            public void done(List<CandyBean> list, BmobException e) {
                if (e != null) {
                    ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_fail));
                    hideProgressDialog();
                    return;
                }
                if (list == null || list.size() == 0) {
                    saveRecommend(coinName, urlString, desString);
                    return;
                }
                CandyBean candyBean = list.get(0);
                updateRecommend(candyBean, coinName, urlContent, desString);

            }
        });


    }

    private void updateRecommend(CandyBean candyBean, String coinName, String urlContent, String desString) {
        if (!candyBean.isCanRecommend()) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_full));
            return;
        }
        List<InviteUrl> inviteUrls = candyBean.getInviteUrls();
        if (inviteUrls.size() >= 2) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_full));
            return;
        }

        if (desString.length() > candyBean.getDescription().length()) {
            candyBean.setDescription(desString);
        }
        InviteUrl inviteUrl0 = inviteUrls.get(0);
        inviteUrl0.setChance(6);

        InviteUrl inviteUrl1 = new InviteUrl(urlContent, 4);
        inviteUrls.add(inviteUrl1);
        candyBean.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_fail));
                    return;
                }
                ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_success));
                mNameEdit.setText("");
                mUrlEdit.setText("");
                mDescriptionEdit.setText("");
                mRewardTv.setVisibility(View.VISIBLE);
                hideProgressDialog();

            }
        });

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dlg_recommend, null);
            builder.setView(view);
            mProgressDialog = builder.create();
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
