package com.orgazmpionerki.braintracker.canvas;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by Dmitriy on 25.06.2015.
 */
public class PowerButtonAnimation implements ITextViewAnimator {

    // animation params
    private long mDuration = 350;
    private TimeInterpolator mInterpolator = new LinearInterpolator();

    private float mPressedRadius;
    private float mUnpressedRadius;
    private float mValue = 1f;
    private Paint mPaint;
    private float mStrokeWidth = 10f;
    private float mRadiusesDiff = 30f;

    public PowerButtonAnimation() {
        mPaint = new Paint();
        mPaint.setAntiAlias(false);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(TextView textView, Canvas canvas) {
        canvas.drawCircle(textView.getWidth() / 2, textView.getHeight() / 2, getCurrentRadius(mValue, mPressedRadius, mUnpressedRadius), mPaint);
    }

    @Override
    public void setValue(TextView textView, float value) {
        mValue = value;
        invalidate(textView);
    }

    private void invalidate(TextView textView) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(textView.getText().toString(), 0, textView.getText().toString().length(), bounds);
        int height = bounds.height();
        int width = bounds.width();
        int max = Math.max(height, width);

        mPressedRadius = (float) max + mStrokeWidth;
        mUnpressedRadius = mPressedRadius + mRadiusesDiff;

        textView.setHeight((int) mUnpressedRadius * 2 + (int) mStrokeWidth * 2);
        textView.setWidth((int) mUnpressedRadius * 2+ (int) mStrokeWidth * 2);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    private float getCurrentRadius(float value, float minRadius, float maxRadius) {
        float resultRadius = minRadius;

        float diff = (maxRadius - minRadius) * value;
        resultRadius = minRadius + diff;

        return resultRadius;
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    @Override
    public TimeInterpolator getInterpolator() {
        return mInterpolator;
    }

    @Override
    public boolean havePriorityDraw() {
        return true;
    }
}
