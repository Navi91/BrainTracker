package com.orgazmpionerki.braintracker.canvas.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.braintracker.R;

/**
 * Created by Dmitriy on 30.06.2015.
 */
public class TestView extends ImageView {
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

//        Bitmap original = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.original_image);
        Bitmap mask = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.brain);
        Bitmap original = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.abc_ab_share_pack_mtrl_alpha);


        //You can change original image here and draw anything you want to be masked on it.

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        tempCanvas.drawBitmap(original, 0, 0, null);
        tempCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        //Draw result after performing masking
        canvas.drawBitmap(result, 0, 0, new Paint());
    }
}
