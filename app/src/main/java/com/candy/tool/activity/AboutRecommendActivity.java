package com.candy.tool.activity;

import android.content.Intent;
import android.view.View;

import com.candy.tool.R;
import com.tool.librecycle.activity.BaseActivity;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class AboutRecommendActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_about_recommend;
    }

    @Override
    public void initViews() {
        findViewById(R.id.about_reward_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutRecommendActivity.this, RewardActivity.class));
            }
        });
    }

    @Override
    public void initData() {

    }
}
