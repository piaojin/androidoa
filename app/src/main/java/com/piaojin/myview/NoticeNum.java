package com.piaojin.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class NoticeNum extends View {
    private TextPaint mTextPaint;
    private MyShape s;
    private ShapeDrawable sd;
    private Canvas canvas;

    public NoticeNum(Context context) {
        super(context);
    }

    public NoticeNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoticeNum(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas=canvas;
        sd.draw(canvas);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        //canvas.drawText("6", 25, 35, mTextPaint);
        canvas.drawText("99+",25,35,mTextPaint);
    }
    private void init(){
        mTextPaint=new TextPaint();
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.WHITE);
        s=new MyShape();
		 /*小圆被放入ShapeDrawable里面，坐标由
		 * setBounds(left, top, right, bommon)定，
		 * 即是一个矩形，坐标点为（left,top）,小圆在这个矩形中
		 * setBounds会改变矩形的位置
		 */
        sd=new ShapeDrawable(s);
        sd.getPaint().setColor(Color.RED);
        sd.setBounds(0,0,0,0);
    }

    private class MyShape extends RectShape {
        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawCircle(25, 25, 25, paint);
        }
    }

    public void setNum(){

    }

    public void clear()
    {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        invalidate();
    }
}
