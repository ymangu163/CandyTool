package com.candy.tool.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.activity.MainActivity;
import com.candy.tool.bean.CandyBean;
import com.candy.tool.bean.InviteUrl;
import com.candy.tool.utils.StatConstant;
import com.candy.tool.utils.StatUtil;
import com.tool.librecycle.utils.CommonSharePref;
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
    private Dialog mProgressDialog;
    private TextView mRecommendTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        mNameEdit = rootView.findViewById(R.id.coin_name_et);
        mUrlEdit = rootView.findViewById(R.id.coin_url_et);
        mDescriptionEdit = rootView.findViewById(R.id.coin_description_et);
        mRecommendTv = rootView.findViewById(R.id.coin_recommend_tv);
        mRecommendTv.setOnClickListener(this);

        mDescriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mRecommendTv.setEnabled(true);
                } else if (start == 0) {
                    mRecommendTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.coin_recommend_tv) {
            submit();
            StatUtil.onEvent(StatConstant.CANDY_RECOMMEND_CLICK);
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
        int index = getUrlPrefixIndex(urlString);
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
                    hideProgressDialog();
                    return;
                }
                ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_success));
                mNameEdit.setText("");
                mUrlEdit.setText("");
                mDescriptionEdit.setText("");
                hideProgressDialog();
                assert getActivity() != null;
                ((MainActivity) getActivity()).refreshCandys();
            }
        });
    }

    private int getUrlPrefixIndex(String urlString) {
        int index = -1;
        String recommendPreUrl = CommonSharePref.getInstance(getContext())
                .getRecommendPreUrl();
        String[] preMasks = null;
        if (!TextUtils.isEmpty(recommendPreUrl)) {
            preMasks = recommendPreUrl.split(";");
        }
        int indexNum = 3;
        if (preMasks != null) {
            for (String preMaskUrl : preMasks) {
                if (urlString.startsWith(preMaskUrl)) {
                    indexNum = 4;
                    break;
                }
            }
        }
        index = ordinalIndexOf(urlString, "/", indexNum);
        if (index < 0) {
            index = urlString.indexOf("?");
        }
        return index;
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
        int index = getUrlPrefixIndex(urlString);
        if (index < 0) {
            index = urlString.length();
        }
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
        if (!candyBean.isCanRecommend()
                || TextUtils.isEmpty(urlContent)) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_full));
            hideProgressDialog();
            return;
        }
        List<InviteUrl> inviteUrls = candyBean.getInviteUrls();
        if (inviteUrls == null || inviteUrls.size() >= 2) {
            ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_full));
            hideProgressDialog();
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
                    hideProgressDialog();
                    return;
                }
                ToastUtils.showToastForShort(getContext(), getString(R.string.recommend_success));
                mNameEdit.setText("");
                mUrlEdit.setText("");
                mDescriptionEdit.setText("");
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
