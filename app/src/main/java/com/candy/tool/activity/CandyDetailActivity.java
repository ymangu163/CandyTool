package com.candy.tool.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.bean.CandyBean;
import com.candy.tool.bean.InviteUrl;
import com.candy.tool.bean.MaskUrl;
import com.candy.tool.utils.StatConstant;
import com.candy.tool.utils.StatUtil;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.CommonSharePref;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class CandyDetailActivity extends BaseActivity implements View.OnClickListener {

    private final static long INTERVAL_MASK_URL = 24 * 3600 * 1000;
    private TextView mTitleTv;
    private TextView mDescriptionTv;
    private TextView mDrawTv;
    private ImageView mRedPacket;

    @Override
    public int getLayoutId() {
        return R.layout.activity_candy_detail;
    }

    @Override
    public void initViews() {
        mTitleTv = findViewById(R.id.detail_title_tv);
        mDescriptionTv = findViewById(R.id.detail_description_tv);
        mDrawTv = findViewById(R.id.detail_draw_tv);
        mRedPacket = findViewById(R.id.red_packet_iv);
        mRedPacket.setOnClickListener(this);
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
                StatUtil.onEvent(StatConstant.CANDY_GET_CLICK);
            }
        });

        getMaskUrls();
        StatUtil.onEvent(StatConstant.CANDY_DETAIL);
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
        final CommonSharePref sharePref = CommonSharePref.getInstance(this);
        if (System.currentTimeMillis() - sharePref.getMaskUrlTime() < INTERVAL_MASK_URL) {
            return;
        }
        BmobQuery<MaskUrl> query = new BmobQuery<MaskUrl>();
        query.addQueryKeys("maskurl,recommendPreUrl");

        query.getObject("2246bc5f5d", new QueryListener<MaskUrl>() {

            @Override
            public void done(MaskUrl object, BmobException e) {
                if (e != null) {
                    return;
                }
                sharePref.setRecommendPreUrl(object.getRecommendPreUrl());
                sharePref.setMaskUrlContent(object.getMaskurl());
                sharePref.setMaskUrlTime(System.currentTimeMillis());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.red_packet_iv) {
            String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                    "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                    "%3Dweb-other&_t=1472443966571#Intent;" +
                    "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

            startIntentUrl(CandyDetailActivity.this, INTENT_URL_FORMAT.replace("{urlCode}", "c1x05828dbb5uwtrasbia19"));

            StatUtil.onEvent(StatConstant.RED_PACKET_CLICK, "detail");
            CommonSharePref.getInstance(CandyDetailActivity.this).setPaketClickTime(System.currentTimeMillis());
            mRedPacket.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRedPacket.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    public void startIntentUrl(Context context, String intentFullUrl) {
        try {
            Intent intent = Intent.parseUri(
                    intentFullUrl,
                    Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
