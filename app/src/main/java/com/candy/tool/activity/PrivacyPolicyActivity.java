package com.candy.tool.activity;

import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.candy.tool.R;
import com.tool.librecycle.activity.BaseActivity;



public class PrivacyPolicyActivity extends BaseActivity {

	private WebView wvPrivacyPolicy;

	@Override
	public int getLayoutId() {
		return R.layout.activity_privacy_policy;
	}

	@Override
	public void initViews() {
		wvPrivacyPolicy = findViewById(R.id.privacy_policy_webview);
		wvPrivacyPolicy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	@Override
	public void initData() {
		load();
	}

	@SuppressWarnings("deprecation")
	private void load() {
		WebSettings settings = wvPrivacyPolicy.getSettings();

		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		settings.setTextSize(WebSettings.TextSize.LARGEST);
		settings.setJavaScriptEnabled(true);
		settings.setDefaultZoom(ZoomDensity.FAR);

		wvPrivacyPolicy.setBackgroundColor(Color.TRANSPARENT);
		wvPrivacyPolicy.loadUrl("file:///android_asset/privacy_policy.html");

		wvPrivacyPolicy.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

	}
}
