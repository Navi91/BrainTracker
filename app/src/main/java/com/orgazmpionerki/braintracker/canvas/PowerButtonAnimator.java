package com.orgazmpionerki.braintracker.canvas;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dmitriy on 25.06.2015.
 */
public class PowerButtonAnimator implements View.OnTouchListener {
    public final static String DEBUG_TAG = "power_animation_debug";

    private PowerButtonAnimation mButtonAnimation;
    private AnimationTextView mAnimationTextView;
    private ValueAnimator mUnpressAnimator;
    private ValueAnimator mPressAnimator;
    private float mValue;
    private boolean mPressAnimationRun = false;
    private View.OnClickListener mClickListener;

    public PowerButtonAnimator(AnimationTextView textView) {
        final float startValue = 0f;
        final float finishValue = 1f;

        mAnimationTextView = textView;
        mAnimationTextView.setOnTouchListener(this);
        mButtonAnimation = new PowerButtonAnimation();
        mAnimationTextView.setAnimator(mButtonAnimation);
        mAnimationTextView.setValue(finishValue);

        mUnpressAnimator = ValueAnimator.ofFloat(startValue, finishValue);
        mUnpressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationTextView.setValue(animation.getAnimatedFraction());
                mValue = animation.getAnimatedFraction();
            }
        });

        mPressAnimator = ValueAnimator.ofFloat(startValue, finishValue);
        mPressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationTextView.setValue(finishValue - animation.getAnimatedFraction());
                mValue = finishValue - animation.getAnimatedFraction();
                if (animation.getAnimatedFraction() == finishValue) {
                    mPressAnimationRun = false;

                    if (mClickListener != null) {
                        mClickListener.onClick(mAnimationTextView);
                    }
                    startUnpressAnimation();
                }
            }
        });
    }

    public void setState(boolean start) {
        mButtonAnimation.setColor(start ? Color.RED : Color.GREEN);
    }

    public void setClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    private void startPressAnimation() {
        if (mPressAnimationRun) {
            return;
        }
        mPressAnimationRun = true;

        mPressAnimator.setDuration(mButtonAnimation.getDuration());
        mPressAnimator.setInterpolator(mButtonAnimation.getInterpolator());
        mPressAnimator.start();
    }

    private void startUnpressAnimation() {
        mUnpressAnimator.setDuration(mButtonAnimation.getDuration());
        mUnpressAnimator.setInterpolator(mButtonAnimation.getInterpolator());
        mUnpressAnimator.start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startPressAnimation();
                break;
        }

        return false;
    }
}
