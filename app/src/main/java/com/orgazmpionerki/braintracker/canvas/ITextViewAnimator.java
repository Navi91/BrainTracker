package com.orgazmpionerki.braintracker.canvas;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.widget.TextView;

/**
 * Created by Dmitriy on 21.06.2015.
 */
public interface ITextViewAnimator {
    public void onDraw(TextView textView, Canvas canvas);

    public void setValue(TextView textView, float value);

    public long getDuration();

    public TimeInterpolator getInterpolator();

    public boolean havePriorityDraw();
}
