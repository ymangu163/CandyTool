package com.candy.tool.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candy.tool.R;
import com.candy.tool.activity.AboutUsActivity;
import com.candy.tool.activity.FAQActivity;
import com.candy.tool.activity.FeedbackActivity;
import com.candy.tool.dialog.ShareDialog;

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
            if (getActivity() != null) {
                new ShareDialog(getActivity()).show();
            }
        }
    }
}
