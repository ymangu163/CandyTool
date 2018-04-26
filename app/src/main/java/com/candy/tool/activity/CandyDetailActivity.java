package com.candy.tool.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.bean.CandyBean;
import com.candy.tool.bean.InviteUrl;
import com.candy.tool.bean.MaskUrl;
import com.candy.tool.utils.GlobalData;
import com.tool.librecycle.activity.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

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
        if (candyBean == null) {
            return;
        }
        mTitleTv.setText(candyBean.getName());
        mDescriptionTv.setText(candyBean.getDescription());
        final String candyUrl = getSingleUrl(candyBean);
        mDrawTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandyDetailActivity.this, DrawCandyActivity.class);
                intent.putExtra("url", candyUrl.trim());
                startActivity(intent);
            }
        });

        getMaskUrls();
    }

    private String getSingleUrl(CandyBean candyBean) {
        if (candyBean == null) {
            return "";
        }
        String urlPrefix = candyBean.getUrlPrefix();
        List<InviteUrl> inviteUrls = candyBean.getInviteUrls();
        if (inviteUrls == null || inviteUrls.size() == 0) {
            return urlPrefix;
        }
        if (inviteUrls.size() == 1) {
            return urlPrefix + inviteUrls.get(0).getUrlContent();
        }
        int number = new Random().nextInt(10);
        int sum = 0;
        for (InviteUrl inviteUrl : inviteUrls) {
            if (number < inviteUrl.getChance() + sum) {
                return urlPrefix + inviteUrl.getUrlContent();
            }
            if (inviteUrl.getChance() < 0) {
                return urlPrefix + inviteUrl.getUrlContent();
            } else {
                sum += inviteUrl.getChance();
            }
        }
        return urlPrefix;
    }

    private void getMaskUrls() {
        if (!TextUtils.isEmpty(GlobalData.getMaskUrl())) {
            return;
        }
        BmobQuery<MaskUrl> query = new BmobQuery<MaskUrl>();
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateNowStr = dateFormat.format(new Date());
        dateNowStr = dateNowStr.substring(0, 11) + "00:00:00";
        try {
            date = dateFormat.parse(dateNowStr);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.addQueryKeys("maskurl");

        query.getObject("2246bc5f5d", new QueryListener<MaskUrl>() {

            @Override
            public void done(MaskUrl object, BmobException e) {
                if (e != null) {
                    return;
                }
                GlobalData.setMaskUrl(object.getMaskurl());
            }

        });
    }
}
