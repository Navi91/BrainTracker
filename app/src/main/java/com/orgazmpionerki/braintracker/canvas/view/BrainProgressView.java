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
    private float mValue = 0f;

    public BrainProgressView(Context context) {
        super(context);
    }

    public BrainProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrainProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BrainProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setValue(float value) {
        mValue = value;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brain, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitmap.getPixel(x, y) == Color.WHITE && y >= height * (1 - mValue)) {
                    bitmap.setPixel(x, y, Color.GREEN);
                }
            }
        }

        canvas.drawBitmap(bitmap, 0f, 0f, mPaint);
    }
}
