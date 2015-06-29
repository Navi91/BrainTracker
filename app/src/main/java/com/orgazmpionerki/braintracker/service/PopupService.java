package com.orgazmpionerki.braintracker.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.util.Tracer;

public class PopupService extends Service {
	public final static String EXTRA_VIDEO_TITLE = "com.orgazmpionerki.braintracker.service.extra_video_title";
	public final static String EXTRA_BRAIN_POINTS = "com.orgazmpionerki.braintracker.service.brain_points";

	private WindowManager mWindowManager;
	private ImageView mImageView;
	private TextView mTextView;
	private int mPoints;
	private LinearLayout mLayout;

	private OnTouchListener mTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				stopSelf();
				return true;
			case MotionEvent.ACTION_UP:
				return true;
			case MotionEvent.ACTION_MOVE:

				return true;
			}
			return false;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Tracer.debug("ON START COMMAND POPUP");

		if (intent.hasExtra(EXTRA_BRAIN_POINTS) && mTextView != null) {
			mPoints = intent.getIntExtra(EXTRA_BRAIN_POINTS, 0);

			if (mPoints < 0) {
				mTextView.setText("" + mPoints);
				mTextView.setTextColor(getResources().getColor(R.color.red));
			} else if (mPoints > 0) {
				mTextView.setText("+" + mPoints);
				mTextView.setTextColor(getResources().getColor(R.color.green));
			} else {
				stopSelf();
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Tracer.debug("CREATE POPUP");

		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		mLayout = (LinearLayout) LinearLayout.inflate(this, R.layout.popup, null);
		mImageView = (ImageView) mLayout.findViewById(R.id.image);
		mTextView = (TextView) mLayout.findViewById(R.id.title);

		mImageView.setOnTouchListener(mTouchListener);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(200, 250, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		mWindowManager.addView(mLayout, params);

		// set timer for popup notification
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				Tracer.debug("STOP POPUP");
				stopSelf();
			}
		}, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLayout != null)
			mWindowManager.removeView(mLayout);
	}
}
