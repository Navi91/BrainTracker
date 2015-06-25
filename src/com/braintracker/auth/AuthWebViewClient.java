package com.braintracker.auth;

import com.braintracker.util.Constants;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** Our webview client takes care of capturing any URL that is loaded, it keeps a lookout for the redirect url and when this is loaded we inform the listener (our ParamChecker) that either "allow access" or "no thanks" has been clicked ( we don't know which yet) */
public class AuthWebViewClient extends WebViewClient {

	private final OnAuthCallbackListener mAuthCallbackListener;

	public AuthWebViewClient(OnAuthCallbackListener authCallbackListener) {
		this.mAuthCallbackListener = authCallbackListener;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (weHaveReceivedAnAuthCallback(url)) {
			// Because the oAuthCallback is our redirect url
			// and this url is not a real webpage
			// the webview shows 'page not found' for a split second,
			// to hide this, we hide the webview (we've finished with it anyway)
			view.setVisibility(View.GONE);
			String reply = retrieveParamaters(url);
			mAuthCallbackListener.onAuthCallback(reply);
		}
		return false;
	}

	private boolean weHaveReceivedAnAuthCallback(String url) {
		return url.startsWith(Constants.CALLBACK_URL);
	}

	private String retrieveParamaters(String url) {
		return url.replace(Constants.CALLBACK_URL, "");
	};
}