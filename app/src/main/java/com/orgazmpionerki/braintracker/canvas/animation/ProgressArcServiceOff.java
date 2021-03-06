package com.orgazmpionerki.braintracker.canvas.animation;

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
 * Created by Dmitriy on 24.06.2015.
 */
public class ProgressArcServiceOff implements ITextViewAnimation {
    private Path mPath;
    private float mRectHeight = 400f;
    private float mRectWidth = 400f;
    private float mValue = 0f;
    private RectF mRect;
    private Paint mPaint;

    // animation params
    private float mProgressAngle = 50f; // must be >0 and < 360
    private float mStrokeWidth = 10f;
    private float mPadding = 20f;
    private long mDuration = 5000L;
    private TimeInterpolator mInterpolator = new LinearInterpolator();

    public ProgressArcServiceOff() {
        mPath = new Path();
        mRect = new RectF(0f, 0f, mRectWidth, mRectHeight);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    public void setRect(RectF rect) {
        mRect = rect;
    }

    private void invalidate(TextView textView) {
        mPath.reset();

        float currentProgress = 360 * mValue;
        float startAngle = currentProgress - mProgressAngle / 2;

        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(textView.getText().toString(), 0, textView.getText().toString().length(), bounds);
        int height = bounds.height();
        int width = bounds.width();
        int max = Math.max(height, width);

        textView.setHeight((int) max + (int) mStrokeWidth * 2 + (int) mPadding);
        textView.setWidth((int) max + (int) mStrokeWidth * 2 + (int) mPadding);

        setRect(new RectF(mStrokeWidth, mStrokeWidth, max + (int) mStrokeWidth + mPadding, max + (int) mStrokeWidth + mPadding));

        mPath.addArc(mRect, startAngle - 90, mProgressAngle);
    }

    @Override
    public void onDraw(TextView textView, Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setValue(TextView textView, float value) {
        mValue = value;
        invalidate(textView);
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
        return false;
    }
}

