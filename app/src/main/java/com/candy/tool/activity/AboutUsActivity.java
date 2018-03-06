package com.candy.tool.activity;

import android.widget.TextView;

import com.candy.tool.R;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.CommonUtil;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class AboutUsActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initViews() {
        TextView versionTv = findViewById(R.id.about_us_version_tv);
        String version = "V " + CommonUtil.getVersionName(this);
        versionTv.setText(version);
    }

    @Override
    public void initData() {

    }
}
