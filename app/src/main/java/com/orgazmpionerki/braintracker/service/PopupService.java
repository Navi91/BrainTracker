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
import android.widget.LinearLayout;
import android.widget.TextSwitcher;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.canvas.animator.BrainProgressAnimator;
import com.orgazmpionerki.braintracker.canvas.view.BrainProgressView;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.util.Tracer;

public class PopupService extends Service {
    private static final String DEBUG_TAG = "popup_service_debug";

    public final static String EXTRA_VIDEO_TITLE = "com.orgazmpionerki.braintracker.service.extra_video_title";
    public final static String EXTRA_BRAIN_POINTS_BEFORE = "com.orgazmpionerki.braintracker.service.brain_points_before";
    public final static String EXTRA_BRAIN_POINTS_AFTER = "com.orgazmpionerki.braintracker.service.brain_points_after";

    private final long SHOW_BEFORE_POINTS_TIME = 2000;
    private final long SHOW_AFTER_POINTS_TIME = 5000;

    private WindowManager mWindowManager;
    private BrainProgressView mBrainProgressView;
    private BrainProgressAnimator mProgressAnimator;
    private TextSwitcher mPopupTextSwitcher;
    private LinearLayout mLayout;

    private int mBeforePoint = 0;
    private int mAfterPoints = 0;

    private Handler mBeforeHandler;
    private Handler mAfterHandler;

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
        Tracer.methodEnter(DEBUG_TAG);

        mBeforePoint = intent.getIntExtra(EXTRA_BRAIN_POINTS_BEFORE, 0);
        mAfterPoints = intent.getIntExtra(EXTRA_BRAIN_POINTS_AFTER, 0);

        if (mPopupTextSwitcher != null) {
            // show points before
            mPopupTextSwitcher.setCurrentText(Integer.toString(mBeforePoint));
        }

        // show progress before
        mBrainProgressView.setValue((float) mBeforePoint / (float) Preferences.getTargetValue(PopupService.this));

        mProgressAnimator = new BrainProgressAnimator(mBrainProgressView, mBeforePoint, mAfterPoints, Preferences.getTargetValue(this));

        // switch text points after delay time
        mBeforeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // show new points
                mPopupTextSwitcher.setText(Integer.toString(mAfterPoints));
                mProgressAnimator.start();

                // finish service after showing new points
                mAfterHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // stop service
                        stopSelf();
                    }
                }, SHOW_AFTER_POINTS_TIME);
            }
        }, SHOW_BEFORE_POINTS_TIME);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Tracer.methodEnter(DEBUG_TAG);

        mBeforeHandler = new Handler();
        mAfterHandler = new Handler();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mLayout = (LinearLayout) LinearLayout.inflate(this, R.layout.v_popup, null);
        mBrainProgressView = (BrainProgressView) mLayout.findViewById(R.id.image);
        mPopupTextSwitcher = (TextSwitcher) mLayout.findViewById(R.id.title);
        mPopupTextSwitcher.setInAnimation(this, R.anim.text_slide_in_top);
        mPopupTextSwitcher.setOutAnimation(this, R.anim.text_slide_out_bot);

        mBrainProgressView.setOnTouchListener(mTouchListener);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(200, 250, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager.addView(mLayout, params);
    }

    @Override
    public void onDestroy() {
        Tracer.methodEnter(DEBUG_TAG);
        super.onDestroy();
        if (mLayout != null)
            mWindowManager.removeView(mLayout);
    }
}
