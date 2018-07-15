package com.candy.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.candy.tool.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * File description
 *
 * @author gao
 * @date 2018/7/14
 */

public class ShareDialog extends Dialog implements View.OnClickListener {
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

    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.share_friends_layout) {
            shareToFriends();
        } else if (vId == R.id.share_wechat_layout) {
            shareToWX();
        }
    }

    /**
     * 分享信息到朋友
     */
    private void shareToWxFriend() {

        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(mContext.getString(R.string.app_name));
        sp.setText("区块链糖果分享与领取工具");
//        sp.setImageUrl("http://p70aghfji.bkt.clouddn.com/com.candy.tool/1.4.0/2018-06-27_1530092816251.png");
        sp.setUrl("http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
        // 执行图文分享
        wx.share(sp);

    }

    private void shareToFriends() {
//        Platform.ShareParams params = new Platform.ShareParams();
//        params.setShareType(Platform.SHARE_WEBPAGE);
//        params.setTitle(mContext.getString(R.string.app_name));
//        params.setText("区块链糖果分享与领取工具");
//        params.setUrl("http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
//        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
//        wechat.share(params);

        WechatMoments.ShareParams params = new WechatMoments.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setTitle(mContext.getString(R.string.app_name));
        params.setText("区块链糖果分享与领取工具");

        params.setUrl("http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        Platform moments = ShareSDK.getPlatform(WechatMoments.NAME);
        // 执行图文分享
        moments.share(params);

    }

    private void shareToWX() {
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
//        params.setTitle(mContext.getString(R.string.app_name));
//        params.setText("区块链糖果分享与领取工具");
        params.setUrl("http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.share(params);
    }
}
