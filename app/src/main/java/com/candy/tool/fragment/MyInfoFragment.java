package com.candy.tool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candy.tool.R;
import com.candy.tool.activity.AboutUsActivity;
import com.candy.tool.activity.FAQActivity;
import com.candy.tool.activity.FeedbackActivity;

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

        rootView.findViewById(R.id.help_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_about_tv).setOnClickListener(this);
        rootView.findViewById(R.id.feedback_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_share_tv).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.help_tv) {
            Intent intent = new Intent(getActivity(), FAQActivity.class);
            startActivity(intent);
        } else if (vId == R.id.my_about_tv) {
            Intent intent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(intent);
        } else if (vId == R.id.feedback_tv) {
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
        } else if (vId == R.id.my_share_tv) {
            shareIntent();
        }


    }

    private void shareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "[分享]区块链糖果分享与领取工具\n" +
                "http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SUBJECT, "http://p70aghfji.bkt.clouddn.com/com.candy.tool/1.4.0/2018-06-27_1530092816251.png");
        Intent chooserIntent = Intent.createChooser(intent, "分享到：");
        startActivity(chooserIntent);
    }
}
