package com.candy.tool.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.bean.CandyBean;
import com.tool.librecycle.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class CandyDetailActivity extends BaseActivity {

    private TextView mTitleTv;
    private TextView mDescriptionTv;
    private TextView mDrawTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_candy_detail;
    }

    @Override
    public void initViews() {
        mTitleTv = findViewById(R.id.detail_title_tv);
        mDescriptionTv = findViewById(R.id.detail_description_tv);
        mDrawTv = findViewById(R.id.detail_draw_tv);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        CandyBean candyBean = (CandyBean) intent.getSerializableExtra("candy");
        mTitleTv.setText(candyBean.getName());
        mDescriptionTv.setText(candyBean.getDescription());
        final String candyUrl = candyBean.getUrl();
        mDrawTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandyDetailActivity.this, DrawCandyActivity.class);
                intent.putExtra("url", getUrl(candyUrl));
                startActivity(intent);
            }
        });
    }

    private String getUrl(String urlString) {
        try {
            String[] urlArray = urlString.split(";");
            if (urlArray.length > 1) {
                List<String> urlList = new ArrayList<>();
                List<Integer> chanceList = new ArrayList<>();
                int number = new Random().nextInt(10);
                for (String myurl : urlArray) {
                    String[] probablility = myurl.split(",");
                    urlList.add(probablility[0]);
                    chanceList.add(Integer.valueOf(probablility[1]));
                }
                if (number < chanceList.get(0)) {
                    return urlList.get(0);
                } else if (number < chanceList.get(0) + chanceList.get(1)) {
                    return urlList.get(1);
                } else {
                    return urlList.get(2);
                }
            } else {
                return urlString;
            }
        } catch (NumberFormatException e) {
            return urlString;
        }
    }
}
