package com.orgazmpionerki.braintracker.activity;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.braintracker.R;
import com.dkrasnov.util_android_lib.Tracer;
import com.dkrasnov.util_android_lib.taskexecutor.RequestExecutor;
import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTask;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.orgazmpionerki.braintracker.requests.TokenRequest;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;

/**
 * Created by Dmitriy on 08.11.2015.
 */
public class LogoActivity extends AppCompatActivity {
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/youtube";

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    private static final long LOGO_SHOWING_TIME = 1500L;

    private RequestExecutor mRequestExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_logo);
        findViewById(R.id.logo).setOnClickListener(v -> pickUserAccount());

        mRequestExecutor = new RequestExecutor();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
//            if (Preferences.getAccessKey(LogoActivity.this) != null) {
//                startActivity(new Intent(LogoActivity.this, MainActivity.class));
//            } else {
//                startActivity(new Intent(LogoActivity.this, WelcomeActivity.class));
//            }
//            finish();
        }, LOGO_SHOWING_TIME);
    }


    private void pickUserAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    String mEmail;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_PICK_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Tracer.debug("account_trace", "email " + mEmail);

                    mRequestExecutor.asyncRequest(createTokenRequest());
                }
                break;
            case REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR:
                if (resultCode == RESULT_OK) {
                    mRequestExecutor.asyncRequest(createTokenRequest());
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private void handleError(Exception e) {
        if (e instanceof GooglePlayServicesAvailabilityException) {
            int statusCode = ((GooglePlayServicesAvailabilityException) e).getConnectionStatusCode();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                    LogoActivity.this,
                    REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
            dialog.show();
        } else if (e instanceof UserRecoverableAuthException) {
            Intent intent = ((UserRecoverableAuthException) e).getIntent();
            startActivityForResult(intent, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
        }
    }

    private RequestTask<String> createTokenRequest() {
        return new TokenRequest(this, mEmail, SCOPE, new HandleErrorRequestCallback<String>() {
            @Override
            public void onResult(RequestTask<String> requestTask) {
                Tracer.debug("account_trace", "token " + requestTask.getResult());


            }

            @Override
            public void onError(Exception e) {
                handleError(e);
            }
        });
    }
}
