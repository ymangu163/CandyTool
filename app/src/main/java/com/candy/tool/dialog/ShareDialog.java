package com.candy.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.candy.tool.R;

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

    }

//    /**
//     * 分享信息到朋友
//     */
//    private void shareToWxFriend() {
//
//        Wechat.ShareParams sp = new Wechat.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//        sp.setTitle(mContext.getString(R.string.app_name));
//        sp.setText("区块链糖果分享与领取工具");
////        sp.setImageUrl("http://p70aghfji.bkt.clouddn.com/com.candy.tool/1.4.0/2018-06-27_1530092816251.png");
//        sp.setUrl("http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
//        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
//        // 执行图文分享
//        wx.share(sp);
//
//    }

    private void shareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "区块链糖果分享与领取工具");
        intent.putExtra(Intent.EXTRA_TITLE, mContext.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SUBJECT, "http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
        Intent chooserIntent = Intent.createChooser(intent, "分享到：");
        mContext.startActivity(chooserIntent);
    }

}
