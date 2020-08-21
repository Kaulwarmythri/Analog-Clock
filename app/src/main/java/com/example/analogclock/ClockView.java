package com.example.analogclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class ClockView extends View {
    private static final String TAG = "ClockView";
    private int width,height = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int radius = 0;
    private int numerical_spacing = 0;
    private int minuteHandTruncation,secondHandTruncation = 0;
    private Paint paint;
    private boolean isInIt;
    private int[] numbers = {5,10,15,20,25,30,35,40,45,50,55,60};
    private Rect rect = new Rect();

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initValues(){
        width = getWidth();
        height = getHeight();
        padding = 0;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14,getResources().getDisplayMetrics());
        int min = Math.min(width,height);
        radius = min/2 - padding;
        secondHandTruncation = min/20;
        minuteHandTruncation = min/7;
        paint = new Paint();
        isInIt = true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInIt){
            initValues();
        }
        canvas.drawColor(Color.BLACK);
        getCircle(canvas);
        getCenter(canvas);
        getNumerals(canvas);
        moveHands(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void getCircle(Canvas canvas){
        Log.d(TAG, "drawCircle: drawing circle");
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width/2,height/2,radius+padding-10,paint);

    }
    private void getCenter(Canvas canvas){
        Log.d(TAG, "drawCenter: drawing center");
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/2,height/2,12,paint);

    }
    private void drawHand(Canvas canvas,double loc,boolean isMinute){
        Log.d(TAG, "drawHand: drawing hands");
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        double angle = Math.PI*loc/30-Math.PI/2;
        int handRadius = isMinute?radius-secondHandTruncation-minuteHandTruncation:radius-secondHandTruncation;
        canvas.drawLine(width/2,
                height/2,
                (float) (width/2 + Math.cos(angle)*handRadius),
                (float) (height/2 +Math.sin(angle)*handRadius),
                paint);
    }
    private void moveHands(Canvas canvas){
        Calendar calendar = Calendar.getInstance();
        drawHand(canvas, calendar.get(Calendar.MINUTE),true);
        drawHand(canvas,calendar.get(Calendar.SECOND),false);

    }
    private void getNumerals(Canvas canvas){
        Log.d(TAG, "drawNumerals: drawing numbers");
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(fontSize);
        for (int number : numbers){
            String num = String.valueOf(number);
            paint.getTextBounds(num,0,num.length(),rect);
            double angle = Math.PI/6*(number-3);
            int x = (int) (width/2 + Math.cos(angle)*radius-rect.width()/2);
            int y = (int) (height/2 + Math.sin(angle)*radius+rect.height()/2);
            canvas.drawText(num,x,y,paint);
        }
    }
}
