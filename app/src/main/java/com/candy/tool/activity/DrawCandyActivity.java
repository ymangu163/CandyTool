package com.candy.tool.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.candy.tool.R;
import com.tool.librecycle.activity.BaseActivity;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class DrawCandyActivity extends BaseActivity {

    private WebView mWebView;
    private View mLoadingView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_draw;
    }

    @Override
    public void initViews() {
        mWebView = findViewById(R.id.draw_webview);
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String candyUrl = intent.getStringExtra("url");

        if (!candyUrl.startsWith("http://") && !candyUrl.startsWith("https://")) {
            candyUrl = "http://" + candyUrl;
        }
        mWebView.clearHistory();
        mWebView.clearCache(true);
        WebSettings settings = mWebView.getSettings();

        mWebView.requestFocus();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        mWebView.loadUrl(candyUrl);
//        mWebView.loadUrl("https://www.baidu.com");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mLoadingView = View.inflate(DrawCandyActivity.this, R.layout.layout_loading,null);
                view.addView(mLoadingView, LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                view.removeView(mLoadingView);
            }
        });

    }
}