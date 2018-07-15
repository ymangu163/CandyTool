package com.candy.tool.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.activity.DrawCandyActivity;
import com.candy.tool.dialog.ShareDialog;
import com.candy.tool.utils.StatConstant;
import com.candy.tool.utils.StatUtil;
import com.tool.librecycle.utils.ToastUtils;

/**
 * File description
 *
 * @author gao
 * @date 2018/6/24
 */

public class ActionView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private TextView mTitleTv;
    private ImageView mBackIv;
    private ImageView mPacketIv;
    private String mScene = "";
    private ImageView mShareIv;

    public ActionView(Context context) {
        this(context, null);
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.layout_action_view, this, true);
        setBackgroundColor(getResources().getColor(R.color.white));

        mTitleTv = findViewById(R.id.action_title_tv);
        mBackIv = findViewById(R.id.action_back_iv);
        mPacketIv = findViewById(R.id.action_packet_iv);
        mShareIv = findViewById(R.id.action_share);
        mBackIv.setOnClickListener(this);
        mPacketIv.setOnClickListener(this);
        mShareIv.setOnClickListener(this);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ActionView);
        mScene = typedArray.getString(R.styleable.ActionView_scene);
        typedArray.recycle();
        updateActionView(mScene);
    }

    private void updateActionView(String scene) {
        switch (scene.trim()) {
            case "recommend":
                mBackIv.setVisibility(View.GONE);
                mTitleTv.setText(R.string.recommend_submit);
                break;
            case "candy detail":
                mBackIv.setVisibility(View.VISIBLE);
                mPacketIv.setImageResource(R.drawable.ic_share);
                mShareIv.setVisibility(View.GONE);
                mTitleTv.setText(R.string.candy_detail);
                break;
            case "drawer":
                mBackIv.setVisibility(View.VISIBLE);
                mPacketIv.setVisibility(View.VISIBLE);
                mPacketIv.setImageResource(R.drawable.ic_copy);
                mTitleTv.setText(R.string.candy_draw);
                break;
            case "feedback":
                mBackIv.setVisibility(View.VISIBLE);
                mPacketIv.setVisibility(View.GONE);
                mShareIv.setVisibility(View.GONE);
                mTitleTv.setText(R.string.action_feedback);
                break;
            case "my infor":
                mBackIv.setVisibility(View.GONE);
                mPacketIv.setVisibility(View.GONE);
                mShareIv.setVisibility(View.GONE);
                mTitleTv.setText(R.string.tab_three);
                break;
            case "Policy":
                mTitleTv.setText(R.string.privacy_policy);
                mPacketIv.setVisibility(View.GONE);
                mShareIv.setVisibility(View.GONE);
                break;
            case "FAQ":
                mTitleTv.setText(R.string.action_help);
                mShareIv.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.action_back_iv) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        } else if (vId == R.id.action_packet_iv) {
            handleRedPacket();
        } else if (vId == R.id.action_share) {
            showShareDialog();

        }

    }

    private void handleRedPacket() {
        if (mScene.equals("drawer")) {
            if (mContext instanceof DrawCandyActivity) {
                copy2Clipboard(((DrawCandyActivity) mContext).getCandyUrl());
                StatUtil.onEvent(StatConstant.CANDY_COPY);
            }
        } else if (mScene.equals("candy detail")) {
            showShareDialog();
        } else {
            String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                    "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                    "%3Dweb-other&_t=1472443966571#Intent;" +
                    "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

            startIntentUrl(mContext, INTENT_URL_FORMAT.replace("{urlCode}", "c1x05828dbb5uwtrasbia19"));

            StatUtil.onEvent(StatConstant.RED_PACKET_CLICK, mScene);
        }
    }

    /**
     * 打开 Intent Scheme Url
     *
     * @param context      Parent Activity
     * @param intentFullUrl Intent 跳转地址
     * @return 是否成功调用
     */
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

    private void copy2Clipboard(String value) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("url", value);
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToastForShort(mContext, mContext.getString(R.string.string_copied));
        }
    }

    private void showShareDialog() {
        new ShareDialog(mContext).show();
    }
}
