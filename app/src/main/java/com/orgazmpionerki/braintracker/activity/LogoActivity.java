package com.orgazmpionerki.braintracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.util.Preferences;

/**
 * Created by Dmitriy on 08.11.2015.
 */
public class LogoActivity extends AppCompatActivity {
    private static final long LOGO_SHOWING_TIME = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_logo);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (Preferences.getAccessKey(LogoActivity.this) != null) {
                startActivity(new Intent(LogoActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(LogoActivity.this, WelcomeActivity.class));
            }
            finish();
        }, LOGO_SHOWING_TIME);
    }
}
