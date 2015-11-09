package com.orgazmpionerki.braintracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.fragment.BaseFragment;
import com.orgazmpionerki.braintracker.fragment.WelcomeFragment;

/**
 * Created by Dmitriy on 08.11.2015.
 */
public class WelcomeActivity extends AppCompatActivity {
    public static final int AUTH_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_welcome);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.a_welcome_title);

        BaseFragment testFragment = WelcomeFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.container, testFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AUTH_REQUEST:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, getResources().getString(R.string.refused_message), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
