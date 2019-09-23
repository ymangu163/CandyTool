package com.candy.tool.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.candy.tool.R;


/**
 * File description
 *
 * @author gao
 * @date 2018/7/16
 */

public class UpgradeDialog extends AppCompatDialog implements View.OnClickListener {
    private TextView mOkBtn;
    private Context mContext;
    private TextView mUpgradeDesc;
    private String mDownloadUrl;

    public UpgradeDialog(Context context) {
        this(context, R.style.AppDilogTheme);
    }

    public UpgradeDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.layout_upgrade);

        this.mContext = context;
        mOkBtn = findViewById(R.id.upgrade_ok);
        mOkBtn.setOnClickListener(this);
        mUpgradeDesc = findViewById(R.id.upgrade_msg_tv);

        setWindowParams();
    }

    //设置Dialog 全屏显示
    private void setWindowParams() {
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    public void setMsgStr(String msgStr) {
        mUpgradeDesc.setText(mContext.getString(R.string.upgrade_desc, msgStr));
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.upgrade_ok) {
            Uri uri = Uri.parse(mDownloadUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
            dismiss();
        }
    }
}
