package com.candy.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.candy.tool.R;
import com.candy.tool.bean.Config;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/7/14
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private final String APP_ID = "wx1c65e6bcf1eb9c3c";
    private final int WX_SCENE_SESSION = 0;
    private final int WX_SCENE_TIMELINE = 1;

    private String mShareUrl = "http://share.kuaikuaiyu.com/store-api/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe";
    private Context mContext;

    public ShareDialog(@NonNull Context context) {
        this(context, R.style.SheetDialogStyle);
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

        setContentView(R.layout.layout_dlg_share);

        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);

        findViewById(R.id.share_wechat_layout).setOnClickListener(this);
        findViewById(R.id.share_friends_layout).setOnClickListener(this);
        findViewById(R.id.share_others_layout).setOnClickListener(this);

        getShareUrl();
    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.share_wechat_layout) {
            shareToWeChat(WX_SCENE_SESSION);
        } else if (vId == R.id.share_friends_layout) {
            shareToWeChat(WX_SCENE_TIMELINE);
        } else if (vId == R.id.share_others_layout) {
            shareIntent();
        }

    }

    private void shareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "区块链糖果分享与领取工具");
        intent.putExtra(Intent.EXTRA_TITLE, mContext.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SUBJECT, mShareUrl);
        Intent chooserIntent = Intent.createChooser(intent, "分享到：");
        mContext.startActivity(chooserIntent);
    }

    private void getShareUrl() {
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.addQueryKeys("share");

        query.getObject("4sbCWWWv", new QueryListener<Config>() {

            @Override
            public void done(Config object, BmobException e) {
                if (e != null) {
                    return;
                }
                if (!TextUtils.isEmpty(object.getShare().trim())) {
                    mShareUrl = object.getShare().trim();
                }
            }
        });
    }

    public void shareToWeChat(int scene) {

        // 获取IWXAPI实例，IWXAPI是第三方app和微信通信的openapi接口
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, APP_ID, false);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

        // 网页
        WXWebpageObject webPageObj = new WXWebpageObject();
        webPageObj.webpageUrl = mShareUrl;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        // msg.mediaObject = textObj;
        msg.mediaObject = webPageObj;
        // 发送文本类型的消息时，title字段不起作用
        msg.title = "糖果助手";
        msg.description = "分分钟撸糖果，赚它几个亿，还免费哦！";

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        req.message = msg;
        if (scene == WX_SCENE_SESSION) {
            req.scene = SendMessageToWX.Req.WXSceneSession; // 微信好友
        } else if (scene == WX_SCENE_TIMELINE) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline; // 朋友圈
        }
        // 调用api接口发送数据到微信
        api.sendReq(req);
    }

}
