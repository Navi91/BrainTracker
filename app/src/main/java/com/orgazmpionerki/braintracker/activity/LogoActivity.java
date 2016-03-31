package com.orgazmpionerki.braintracker.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.braintracker.R;
import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.outh2.GoogleAuthToken;

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
