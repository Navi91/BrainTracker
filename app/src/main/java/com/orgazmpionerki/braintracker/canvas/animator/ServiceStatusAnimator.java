package com.orgazmpionerki.braintracker.canvas.animator;

import android.animation.ValueAnimator;

import com.orgazmpionerki.braintracker.canvas.animation.ProgressArcServiceOff;
import com.orgazmpionerki.braintracker.canvas.animation.ProgressArcServiceOn;
import com.orgazmpionerki.braintracker.canvas.view.AnimationTextView;

/**
 * Created by Dmitriy on 25.06.2015.
 */
public class ServiceStatusAnimator {
    private AnimationTextView mAnimationTextView;
    private ValueAnimator mAnimator;
    private ProgressArcServiceOn mProgressOnAnimator;
    private ProgressArcServiceOff mProgressOffAnimator;

    public ServiceStatusAnimator(AnimationTextView textView) {
        mAnimationTextView = textView;
        mProgressOnAnimator = new ProgressArcServiceOn();
        mProgressOffAnimator = new ProgressArcServiceOff();

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationTextView.setValue(animation.getAnimatedFraction());
            }
        });
    }

    public void setServiceStatus(boolean serviceOn) {
        mAnimator.cancel();
        mAnimationTextView.setAnimator(serviceOn ? mProgressOnAnimator : mProgressOffAnimator);

        mAnimator.setDuration(mAnimationTextView.getAnimator().getDuration());
        mAnimator.setInterpolator(mAnimationTextView.getAnimator().getInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }
}
