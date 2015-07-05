package com.orgazmpionerki.braintracker.canvas.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.braintracker.R;

/**
 * Created by Dmitriy on 05.07.2015.
 */
public class BrainProgressView extends View implements IValueView {
    private final Paint mPaint = new Paint();
    private Bitmap mBitmap;
    private float mValue = 0f;
    private final float mGreenBoundValue = 0.4f;
    private final float mRedBoundValue = 0f;
    private int mProgressColor = Color.RED;

    public BrainProgressView(Context context) {
        super(context);
        init();
    }

    public BrainProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrainProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BrainProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brain_progress, options);
    }

    @Override
    public void setValue(float value) {
        mValue = value;
        updateProgressState(value);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isMaskPixelColor(mBitmap.getPixel(x, y))) {
                    if (mProgressColor == Color.RED) {
                        mBitmap.setPixel(x, y, mProgressColor);
                        continue;
                    }

                    if (y < height * (1 - mValue)) {
                        mBitmap.setPixel(x, y, Color.WHITE);
                    } else {
                        mBitmap.setPixel(x, y, mProgressColor);
                    }
                }
            }
        }

        canvas.drawBitmap(mBitmap, 0f, 0f, mPaint);
    }

    private boolean isMaskPixelColor(int color) {
        return color == Color.WHITE || color == Color.GREEN || color == Color.YELLOW || color == Color.RED;
    }

    private void updateProgressState(float value) {
        if (value > mGreenBoundValue) {
            mProgressColor = Color.GREEN;
        } else if (value <= mGreenBoundValue && value >= mRedBoundValue) {
            mProgressColor = Color.YELLOW;
        } else {
            mProgressColor = Color.RED;
        }
    }
}
