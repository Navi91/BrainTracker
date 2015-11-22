package com.orgazmpionerki.braintracker.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.braintracker.R;
import com.dkrasnov.util_android_lib.Tracer;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.orgazmpionerki.braintracker.auth.AuthActivity;
import com.orgazmpionerki.braintracker.auth.tokens.Tokens;
import com.orgazmpionerki.braintracker.fragment.BaseFragment;
import com.orgazmpionerki.braintracker.fragment.ServiceFragment;
import com.orgazmpionerki.braintracker.fragment.SettingsFragment;
import com.orgazmpionerki.braintracker.fragment.StatisticsFrament;
import com.orgazmpionerki.braintracker.notification.OnChangePointsListener;
import com.orgazmpionerki.braintracker.receiver.WiFiReceiver;
import com.orgazmpionerki.braintracker.receiver.WiFiReceiver.WiFiStateChangeListener;
import com.orgazmpionerki.braintracker.service.BrainTrackerService;
import com.orgazmpionerki.braintracker.service.controllers.BrainTrackerServiceController;
import com.orgazmpionerki.braintracker.service.controllers.IBrainServiceController;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements WiFiStateChangeListener, OnChangePointsListener {
    private static final String BUNDLE_CURRENT_DRAWER_ITEM = "com.orgazmpionerki.braintracker.bundle_current_drawer_item";

    public static final int AUTH_REQUEST = 123;

    private static final int DRAWER_IDENTIFIER_TRACKER = 0;
    private static final int DRAWER_IDENTIFIER_STATISTICS = 1;
    private static final int DRAWER_IDENTIFIER_SETTINGS = 2;
    private static final int DRAWER_IDENTIFIER_ABOUT = 3;

    private Drawer.Result mDrawerResult = null;
    private int mCurrentDrawerItem = 0;
    private IBrainServiceController mBrainServiceController;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_tracker).withIcon(FontAwesome.Icon.faw_youtube).withIdentifier(DRAWER_IDENTIFIER_TRACKER),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_statistics).withIcon(FontAwesome.Icon.faw_bar_chart).withIdentifier(DRAWER_IDENTIFIER_STATISTICS),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(DRAWER_IDENTIFIER_SETTINGS),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_info).withIdentifier(DRAWER_IDENTIFIER_ABOUT)
                )
                .withOnDrawerItemClickListener((adapterView, view, position, id, iDrawerItem) -> {
                            if (iDrawerItem != null) {
                                onDrawerItemClicked(iDrawerItem.getIdentifier());
                            } else {
                                onDrawerItemClicked(DRAWER_IDENTIFIER_TRACKER);
                                mDrawerResult.setSelection(DRAWER_IDENTIFIER_TRACKER);
                            }
                        }
                )
                .build();

        if (Preferences.getBeginDate(this) == 0L) {
            Preferences.putBeginDate(this, Calendar.getInstance().getTimeInMillis());
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_CURRENT_DRAWER_ITEM)) {
            onDrawerItemClicked(savedInstanceState.getInt(BUNDLE_CURRENT_DRAWER_ITEM, 0));
        } else {
            BaseFragment fragment = ServiceFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

//            BaseFragment testFragment = TestFragment.newInstance();
//            getFragmentManager().beginTransaction().replace(R.id.container, testFragment).commit();
        }

        mBrainServiceController = new BrainTrackerServiceController();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        WiFiReceiver.addListener(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WiFiReceiver.removeListener(this, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState == null) {
            outState = new Bundle();
        }

        outState.putInt(BUNDLE_CURRENT_DRAWER_ITEM, mCurrentDrawerItem);
    }

    public void startAuthorisationForYouTube() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, AUTH_REQUEST);

//        String[] accountTypes = new String[]{"com.google"};
//        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
//                null, false, null, null, null, null);
//        startActivityForResult(intent, AUTH_REQUEST);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AUTH_REQUEST:
                if (resultCode == RESULT_OK) {
                    processAuthorisationResult(data);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, getResources().getString(R.string.refused_message), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerResult != null && mDrawerResult.isDrawerOpen()) {
            mDrawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void processAuthorisationResult(Intent data) {
        Tokens tokens = (Tokens) data.getSerializableExtra(AuthActivity.EXTRA_TOKENS);
        traceResult(tokens);

        startBrainTrackerService();

        updateFragmentsContent(null);
    }

    public void attemptToStartBrainTrackerService() {
        if (Preferences.getAccessKey(this) != null) {
            startBrainTrackerService();
        } else {
            startAuthorisationForYouTube();
        }
    }

    private void startBrainTrackerService() {
        Tracer.methodEnter(BrainTrackerService.DEBUG_TAG);
        mBrainServiceController.startService(this);
        updateFragmentsContent(null);
    }

    public void stopBrainTrackerService() {
        mBrainServiceController.stopService(this);
        updateFragmentsContent(null);
    }

    public void updateFragmentsContent(Bundle args) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            try {
                ((BaseFragment) fragment).updateContent(args);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWiFiStateChange() {
        updateFragmentsContent(null);
    }

    @Override
    public void onChangePoints() {
        updateFragmentsContent(null);
    }

    private void onDrawerItemClicked(int identifier) {
        switch (identifier) {
            case DRAWER_IDENTIFIER_TRACKER:
                mToolbar.setTitle(R.string.app_name);
                BaseFragment serviceFragment = ServiceFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.container, serviceFragment).commit();
                mCurrentDrawerItem = identifier;
                break;
            case DRAWER_IDENTIFIER_STATISTICS:
                mToolbar.setTitle(R.string.statistics_fragment_title);
                BaseFragment statisticsFragment = StatisticsFrament.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.container, statisticsFragment).commit();
                mCurrentDrawerItem = identifier;
                break;
            case DRAWER_IDENTIFIER_SETTINGS:
                mToolbar.setTitle(R.string.setting_fragment_title);
                BaseFragment settingsFragment = SettingsFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                mCurrentDrawerItem = identifier;
                break;
            case DRAWER_IDENTIFIER_ABOUT:
                showAboutDialog();
                mDrawerResult.setSelection(mCurrentDrawerItem);
                break;
        }
    }

    private void showAboutDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View aboutView = inflater.inflate(R.layout.d_about, null, false);
        TextView versionTextView = (TextView) aboutView.findViewById(R.id.about_version);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            versionTextView.setText(version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.about_dialog_title);
        builder.setView(aboutView);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int color) {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(color));
    }

    private void traceResult(Tokens tokens) {
        Tracer.debug("Got access token: " + tokens.getAccessToken());
        Tracer.debug("Got refresh token: " + tokens.getRefreshToken());
        Tracer.debug("Got token type: " + tokens.getTokenType());
        Tracer.debug("Got expires in: " + tokens.getExpiresIn());
    }
}