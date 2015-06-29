package com.orgazmpionerki.braintracker.auth;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.auth.tokens.TokensTask;
import com.orgazmpionerki.braintracker.auth.tokens.TokenRetrievedListener;
import com.orgazmpionerki.braintracker.auth.tokens.Tokens;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

/** Activity incorporates signing into YouTube and Retrieving the access_token for YouTube API access in the future */
public class AuthActivity extends FragmentActivity implements OnAuthListener, TokenRetrievedListener {

	public static final String EXTRA_TOKENS = "com.braintracker.AuthActivity.EXTRA_TOKENS";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_oauth);
		setResult(RESULT_CANCELED);

		WebView webview = (WebView) findViewById(R.id.webview);
		webview.setWebViewClient(new AuthWebViewClient(new ParamChecker(this)));
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.loadUrl(Constants.OAUTH_URL);

		Toast.makeText(this, "Loading .. just wait", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onRefused();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onAuthorized(String authCode) {
		Preferences.putAuthCode(this, authCode);
		new Thread(new TokensTask(this, authCode, this)).start();
	}

	@Override
	public void onRefused() {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onTokensRetrieved(Tokens tokens) {
		Intent intent = createSendableBundle(tokens);
		setResult(RESULT_OK, intent);
		finish();
	}

	private Intent createSendableBundle(Tokens tokens) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_TOKENS, tokens);
		return intent;
	}
}