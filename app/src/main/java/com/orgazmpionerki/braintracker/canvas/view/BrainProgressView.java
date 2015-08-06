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
    private BrainProgressState mPreviousState;
    private BrainProgressState mCurrentState;
    private final float mGreenBoundValue = 0.3f;
    private final float mRedBoundValue = 0f;

    private class BrainProgressState {
        float mValue = 0f;
        int mColor = Color.GREEN;

        BrainProgressState(float value, int color) {
            mValue = value;
            mColor = color;
        }

        void copy(BrainProgressState state) {
            this.mValue = state.mValue;
            this.mColor = state.mColor;
        }
    }

    public BrainProgressView(Context context) {
        super(context);
        init(null);
    }

    public BrainProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BrainProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BrainProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    private void init(AttributeSet attrs) {
        int bitmapResource = R.drawable.brain_progress;

        if (attrs != null) {
            bitmapResource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawable", R.drawable.brain_progress);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        mBitmap = BitmapFactory.decodeResource(getResources(), bitmapResource, options);

        mCurrentState = new BrainProgressState(0f, Color.WHITE);
        mPreviousState = new BrainProgressState(0f, 0);
    }

    @Override
    public void setValue(float value) {
        if (mCurrentState.mValue == value) {
            return;
        }

        mPreviousState.copy(mCurrentState);

        updateProgressState(value);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPreviousState.mColor == mCurrentState.mColor) {
            onDrawDiff(canvas);
        } else {
            onDrawNewColor(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());
    }

    private void drawBitmapPixel(Bitmap bitmap, int x, int y, float progress) {
        if (y < progress) {
            bitmap.setPixel(x, y, Color.WHITE);
        } else {
            bitmap.setPixel(x, y, mCurrentState.mColor);
        }
    }

    private void onDrawDiff(Canvas canvas) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        float previousProgress = height * (1 - mPreviousState.mValue);
        float currentProgress = height * (1 - mCurrentState.mValue);
        int minProgress = (int) Math.min(previousProgress, currentProgress);
        int maxProgress = (int) Math.max(previousProgress, currentProgress);

        minProgress = minProgress > 0 ? minProgress : 0;
        maxProgress = maxProgress > height - 1 ? height - 1 : maxProgress;

        for (int x = 0; x < width; x++) {
            for (int y = minProgress; y <= maxProgress; y++) {
                if (isMaskPixelColor(mBitmap.getPixel(x, y))) {
                    drawBitmapPixel(mBitmap, x, y, currentProgress);
                }
            }
        }

        canvas.drawBitmap(mBitmap, 0f, 0f, mPaint);
    }

    private void onDrawNewColor(Canvas canvas) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isMaskPixelColor(mBitmap.getPixel(x, y))) {
                    if (mCurrentState.mColor == Color.RED) {
                        mBitmap.setPixel(x, y, mCurrentState.mColor);
                        continue;
                    }

                    drawBitmapPixel(mBitmap, x, y, height * (1 - mCurrentState.mValue));
                }
            }
        }

        canvas.drawBitmap(mBitmap, 0f, 0f, mPaint);
    }

    private boolean isMaskPixelColor(int color) {
        return color == Color.WHITE || color == Color.GREEN || color == Color.YELLOW || color == Color.RED;
    }

    private void updateProgressState(float value) {
        mCurrentState.mValue = value;

        if (value > mGreenBoundValue) {
            mCurrentState.mColor = Color.GREEN;
        } else if (value <= mGreenBoundValue && value >= mRedBoundValue) {
            mCurrentState.mColor = Color.YELLOW;
        } else {
            mCurrentState.mColor = Color.RED;
        }
    }
}
