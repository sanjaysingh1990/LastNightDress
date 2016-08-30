package com.eowise.recyclerview.stickyheaders.samples.tutorial;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by hiren on 10/01/16.
 */
public class CircleOverlayView extends LinearLayout {
    private Bitmap bitmap;
    public float radius=300;
    public int devicewith=0;
    public CircleOverlayView(Context context) {
        super(context);
    }

    public CircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame(radius);
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected void createWindowFrame(float radius) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#D9222427"));
        //paint.setAlpha(50);

        osCanvas.drawRect(outerRectangle, paint);


        Paint paint2 = new Paint();
        // PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.WHITE);
        paint2.setStrokeWidth(8);

        // paint.setXfermode(xfermode);
        paint2.setAntiAlias(true);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        osCanvas.drawCircle(centerX, centerY, radius, paint2);


        // paint.setARGB(255, 255 , 10, 21);
        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        osCanvas.drawCircle(centerX, centerY, radius, paint);


        Paint paint3 = new Paint();
        paint3.setColor(Color.TRANSPARENT);
        paint3.setStyle(Paint.Style.FILL);
        osCanvas.drawPaint(paint3);

        paint3.setColor(Color.WHITE);
        paint3.setTextSize(40);
        String text="SELECT A CATEGORY";
        Rect bounds = new Rect();
        paint3.getTextBounds(text, 0, text.length(), bounds);

        int xpos = (osCanvas.getWidth() / 2) - (bounds.width() / 2);
        Log.e("xpos",xpos+"");
        osCanvas.drawText(text ,xpos, 300, paint3);

    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }


}