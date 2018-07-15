package com.candy.tool.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.candy.tool.R;
import com.candy.tool.bean.Config;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.CommonUtil;
import com.tool.librecycle.utils.ToastUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initViews() {
        TextView versionTv = findViewById(R.id.about_us_version_tv);
        String version = "V " + CommonUtil.getVersionName(this);
        versionTv.setText(version);

        findViewById(R.id.textViewVersion).setOnClickListener(this);
        findViewById(R.id.privacy_tv).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.privacy_tv) {
            startActivity(new Intent(AboutUsActivity.this, PrivacyPolicyActivity.class));
        } else if (vId == R.id.textViewVersion) {
            checkAppUpdate();
        }

    }

    private void checkAppUpdate() {
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.addQueryKeys("versionCode");

        query.getObject("4sbCWWWv", new QueryListener<Config>() {

            @Override
            public void done(Config object, BmobException e) {
                if (e != null) {
                    ToastUtils.showToastForShort(AboutUsActivity.this, getString(R.string.candy_fail));
                    return;
                }
                int currVerionCode = CommonUtil.getVersionCode(AboutUsActivity.this);
                if (object.getVersionCode() > currVerionCode) {
                    Uri uri = Uri.parse("http://www.beestore.io/share/share.html?appId=43948e1d5e044714a394dd3a10a9bcfe");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    ToastUtils.showToastForShort(AboutUsActivity.this, getString(R.string.last_verion));
                }
            }
        });
    }

}
