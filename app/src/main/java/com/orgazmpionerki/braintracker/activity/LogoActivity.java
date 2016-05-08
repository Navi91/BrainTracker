package com.orgazmpionerki.braintracker.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.outh2.GoogleAuthToken;
import com.orgazmpionerki.braintracker.service.TaskService;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.Calendar;

/**
 * Created by Dmitriy on 08.11.2015.
 */
public class LogoActivity extends AppCompatActivity {
    private static final long LOGO_SHOWING_TIME = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        turnOffServerIfNotExecutedTooLong();
    }

    private void turnOffServerIfNotExecutedTooLong() {
        if (Calendar.getInstance().getTimeInMillis() - Preferences.getLastUpdateTime(this) > TaskService.DELAY_BETWEEN_REQUEST_MILLISECONDS * 2) {
            Preferences.setServerRunning(this, false);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (GoogleAuthToken.exist(this)) {
            startActivity(new Intent(LogoActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(LogoActivity.this, WelcomeActivity.class));
        }
    }

}
