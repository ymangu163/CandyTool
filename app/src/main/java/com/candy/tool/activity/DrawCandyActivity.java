package com.candy.tool.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.candy.tool.R;
import com.crashlytics.android.Crashlytics;
import com.tool.librecycle.activity.BaseActivity;
import com.tool.librecycle.utils.CommonSharePref;
import com.tool.librecycle.utils.ToastUtils;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

@SuppressLint("SetJavaScriptEnabled")
public class DrawCandyActivity extends BaseActivity {

    private WebView mWebView;
    private View mLoadingView;
    private String mCandyUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_draw;
    }

    @Override
    public void initViews() {
        mWebView = findViewById(R.id.draw_webview);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mCandyUrl = intent.getStringExtra("url");
        if (TextUtils.isEmpty(mCandyUrl)) {
            return;
        }
        if (!mCandyUrl.startsWith("http://") && !mCandyUrl.startsWith("https://")) {
            mCandyUrl = "http://" + mCandyUrl;
        }
        mWebView.clearHistory();
        mWebView.clearCache(true);
        WebSettings settings = mWebView.getSettings();

        mWebView.requestFocus();
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        mWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        if (isInterceptUrl(mCandyUrl)) {
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(mCandyUrl));
            try {
                startActivity(intent1);
            } catch (Exception e) {
                Crashlytics.logException(e);
                ToastUtils.showToastForShort(this, getString(R.string.url_error));
            }
            finish();
            return;
        }
        mWebView.loadUrl(mCandyUrl);
//        mWebView.loadDataWithBaseURL(null, candyUrl, "text/html", "UTF-8", null);
//        mWebView.loadUrl("https://beta.ivery.one/I/2105967");

        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isInterceptUrl(url)) {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    try {
                        startActivity(intent1);
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        ToastUtils.showToastForShort(DrawCandyActivity.this, getString(R.string.url_error));
                    }
                    return true;
                }
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mLoadingView == null) {
                    mLoadingView = View.inflate(DrawCandyActivity.this, R.layout.layout_loading, null);
                    view.addView(mLoadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                view.removeView(mLoadingView);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                view.removeView(mLoadingView);
            }
        });

    }

    private boolean isInterceptUrl(String url) {
        if (url.contains("download")
                || url.contains(".apk")) {
            return true;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        String subUrl = url.substring(8);
        int index = subUrl.indexOf("/");
        if (index < 0) {
            index = subUrl.indexOf("?");
        }
        if (!(index < 0)) {
            subUrl = subUrl.substring(0, index);
        }
        if (CommonSharePref.getInstance(this).getMaskUrlContent().contains(subUrl)) {
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }

        super.onBackPressed();
    }

    public String getCandyUrl() {
        return mCandyUrl;
    }

}
