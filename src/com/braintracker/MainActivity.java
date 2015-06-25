package com.braintracker;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

import com.braintracker.auth.AuthActivity;
import com.braintracker.auth.tokens.Tokens;
import com.braintracker.controller.OnChangePointsListener;
import com.braintracker.pager.BrainTrackerPager;
import com.braintracker.receiver.WiFiReceiver;
import com.braintracker.receiver.WiFiReceiver.WiFiStateChangeListener;
import com.braintracker.service.BrainTrackerService;
import com.braintracker.util.Preferences;
import com.braintracker.util.Tracer;

public class MainActivity extends FragmentActivity implements WiFiStateChangeListener, OnChangePointsListener {

	public static final int AUTH_REQUEST = 123;

	private BrainTrackerPager mPager;
	private BrainTrackerService mBrainTrackerService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		if (Preferences.getBeginDate(this) == 0L) {
			Preferences.putBeginDate(this, Calendar.getInstance().getTimeInMillis());
		}

		setContentView(R.layout.activity_main);

		mPager = (BrainTrackerPager) findViewById(R.id.brain_tracker_pager);

		mBrainTrackerService = new BrainTrackerService();
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

	public void startAuthorisationForYouTube() {
		Intent intent = new Intent(this, AuthActivity.class);
		startActivityForResult(intent, AUTH_REQUEST);
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
		mBrainTrackerService.startService(this);
		updateFragmentsContent(null);
	}

	public void stopBrainTrackerService() {
		mBrainTrackerService.stopService(this);

		updateFragmentsContent(null);
	}

	public void updateFragmentsContent(Bundle args) {
		mPager.updateFragmentsContent(args);
	}

	private void traceResult(Tokens tokens) {
		Tracer.debug("Got access token: " + tokens.getAccessToken());
		Tracer.debug("Got refresh token: " + tokens.getRefreshToken());
		Tracer.debug("Got token type: " + tokens.getTokenType());
		Tracer.debug("Got expires in: " + tokens.getExpiresIn());
	}

	@Override
	public void onWiFiStateChange() {
		updateFragmentsContent(null);
	}

	@Override
	public void onChangePoints() {
		Tracer.debug("ON CHANGE MAIN ACTIVITY");
		updateFragmentsContent(null);
	}
}