package com.orgazmpionerki.braintracker.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.auth.tokens.TokenRetrievedListener;
import com.orgazmpionerki.braintracker.auth.tokens.Tokens;
import com.orgazmpionerki.braintracker.auth.youtube.YoutubeHttpTokensTask;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;

/**
 * Activity incorporates signing into YouTube and Retrieving the access_token for YouTube API access in the future
 */
public class AuthActivity extends AppCompatActivity implements OnAuthListener, TokenRetrievedListener {

    public static final String EXTRA_TOKENS = "com.braintracker.AuthActivity.EXTRA_TOKENS";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_oauth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        new Thread(new YoutubeHttpTokensTask(this, authCode, this)).start();
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