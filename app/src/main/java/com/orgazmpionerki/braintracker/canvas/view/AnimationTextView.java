package com.orgazmpionerki.braintracker.canvas.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.orgazmpionerki.braintracker.canvas.animation.ITextViewAnimation;

/**
 * Created by Dmitriy on 21.06.2015.
 */
public class AnimationTextView extends TextView {
    private ITextViewAnimation mAnimator;

    public AnimationTextView(Context context) {
        super(context);
        initAnimation();
    }

    public AnimationTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
    }

    public AnimationTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimationTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAnimation();
    }

    protected void initAnimation() {
    }

    public void setAnimator(ITextViewAnimation animator) {
        mAnimator = animator;
    }

    public ITextViewAnimation getAnimator() {
        return mAnimator;
    }

    public void setValue(float value) {
        if (mAnimator != null) {
            mAnimator.setValue(this, value);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAnimator != null && mAnimator.havePriorityDraw()) {
            mAnimator.onDraw(this, canvas);
        }

        super.onDraw(canvas);

        if (mAnimator != null && !mAnimator.havePriorityDraw()) {
            mAnimator.onDraw(this, canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
