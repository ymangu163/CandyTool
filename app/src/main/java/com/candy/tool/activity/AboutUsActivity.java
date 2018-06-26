package com.candy.tool.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.utils.CandyUtil;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.CommonUtil;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initViews() {
        TextView versionTv = findViewById(R.id.about_us_version_tv);
        String version = "V " + CommonUtil.getVersionName(this);
        versionTv.setText(version);

        findViewById(R.id.textViewVersion).setOnClickListener(this);
        findViewById(R.id.privacy_tv).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.privacy_tv) {
            startActivity(new Intent(AboutUsActivity.this, PrivacyPolicyActivity.class));
        } else if (vId == R.id.textViewVersion) {
            CandyUtil.viewApp(AboutUsActivity.this, getPackageName());
        }

    }
}
