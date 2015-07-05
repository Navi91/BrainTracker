package com.orgazmpionerki.braintracker.canvas.animator;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

import com.orgazmpionerki.braintracker.canvas.view.IValueView;

/**
 * Created by Dmitriy on 05.07.2015.
 */
public class BrainProgressAnimator {
    private IValueView mBrainProgressView;
    private ValueAnimator mAnimator;

    private int mDuration = 2000;

    public BrainProgressAnimator(IValueView valueView, int beforePoints, int afterPoints, int targetPoints) {
        mBrainProgressView = valueView;
        initAnimation(beforePoints, afterPoints, targetPoints);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    private void initAnimation(int beforePoints, int afterPoints, int targetPoints) {
        final float startValue = (float) beforePoints / (float) targetPoints;
        final float finishValue = (float) afterPoints / (float) targetPoints;

        mAnimator = ValueAnimator.ofFloat(startValue, finishValue);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBrainProgressView.setValue((float) animation.getAnimatedValue());
            }
        });
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(mDuration);
    }

    public void start() {
        mAnimator.start();
    }

}
