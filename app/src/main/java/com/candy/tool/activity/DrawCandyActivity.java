package com.candy.tool.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
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

public class DrawCandyActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private View mLoadingView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_draw;
    }

    @Override
    public void initViews() {
        mWebView = findViewById(R.id.draw_webview);
        findViewById(R.id.draw_back_iv).setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String candyUrl = intent.getStringExtra("url");
        if (TextUtils.isEmpty(candyUrl)) {
            return;
        }
        if (!candyUrl.startsWith("http://") && !candyUrl.startsWith("https://")) {
            candyUrl = "http://" + candyUrl;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        mWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        if (isInterceptUrl(candyUrl)) {
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(candyUrl));
            try {
                startActivity(intent1);
            } catch (Exception e) {
                Crashlytics.logException(e);
                ToastUtils.showToastForShort(this, getString(R.string.url_error));
            }
            finish();
            return;
        }
        mWebView.loadUrl(candyUrl);
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
                mLoadingView = View.inflate(DrawCandyActivity.this, R.layout.layout_loading, null);
                view.addView(mLoadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= 21) {
                    Log.e("gao", request.getUrl().toString());
                }
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

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.draw_back_iv) {
            finish();
        }
    }
}
