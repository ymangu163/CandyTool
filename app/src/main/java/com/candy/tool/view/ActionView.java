package com.candy.tool.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
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
    private String mTitle = "";
    private ImageView mShareIv;
    private boolean mBackAble;
    private boolean mRightIconAble;

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
        mShareIv = findViewById(R.id.action_share);
        mBackIv.setOnClickListener(this);
        mShareIv.setOnClickListener(this);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ActionView);
        mTitle = typedArray.getString(R.styleable.ActionView_title);
        mBackAble = typedArray.getBoolean(R.styleable.ActionView_back_enable, true);
        mRightIconAble = typedArray.getBoolean(R.styleable.ActionView_right_enable, false);
        int rightIcon = typedArray.getResourceId(R.styleable.ActionView_right_icon, R.drawable.ic_share);
        typedArray.recycle();

        mTitleTv.setText(mTitle);
        mShareIv.setImageResource(rightIcon);
        if (mBackAble) {
            mBackIv.setVisibility(View.VISIBLE);
        } else {
            mBackIv.setVisibility(View.GONE);
        }
        if (mRightIconAble) {
            mShareIv.setVisibility(View.VISIBLE);
        } else {
            mShareIv.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.action_back_iv) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        } else if (vId == R.id.action_share) {
            if (TextUtils.equals(mTitle, mContext.getString(R.string.candy_draw))) {
                copy2Clipboard(((DrawCandyActivity) mContext).getCandyUrl());
            } else {
                String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                        "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                        "%3Dweb-other&_t=1472443966571#Intent;" +
                        "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

                startIntentUrl(mContext, INTENT_URL_FORMAT.replace("{urlCode}", "c1x05828dbb5uwtrasbia19"));
            }
        }

    }

    private void handleRedPacket() {
        if (mTitle.equals("drawer")) {
            if (mContext instanceof DrawCandyActivity) {
                copy2Clipboard(((DrawCandyActivity) mContext).getCandyUrl());
                StatUtil.onEvent(StatConstant.CANDY_COPY);
            }
        } else if (mTitle.equals("candy detail")) {
            new ShareDialog(mContext).show();
        } else {
            String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                    "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                    "%3Dweb-other&_t=1472443966571#Intent;" +
                    "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

            startIntentUrl(mContext, INTENT_URL_FORMAT.replace("{urlCode}", "c1x05828dbb5uwtrasbia19"));

            StatUtil.onEvent(StatConstant.RED_PACKET_CLICK, mTitle);
        }
    }

    /**
     * 打开 Intent Scheme Url
     *
     * @param context       Parent Activity
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

    private void shareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "[分享]区块链糖果分享与领取工具\n" +
                "http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        intent.putExtra(Intent.EXTRA_TITLE, mContext.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SUBJECT, "[分享]区块链糖果分享与领取工具\n" +
                "http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        Intent chooserIntent = Intent.createChooser(intent, "分享到：");
        mContext.startActivity(chooserIntent);
    }

}
