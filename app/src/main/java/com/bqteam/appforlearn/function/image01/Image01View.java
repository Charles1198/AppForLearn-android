package com.bqteam.appforlearn.function.image01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 * @date 2018/3/13
 */

public class Image01View extends View {
    private int watershed = 128;
    private List<ColorItem> colorList = new ArrayList<>();

    Paint paint = new Paint();

    public Image01View(Context context) {
        super(context);
        init();
    }

    public Image01View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.GREEN);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setColorList(List<ColorItem> colorList) {
        this.colorList = colorList;
        invalidate();
    }

    public void increaseWatershed() {
        watershed++;
        invalidate();
    }

    public void decreaseWatershed() {
        watershed--;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colorList.size() == 0) {
            return;
        }

        canvas.drawColor(Color.BLACK);

        for (ColorItem item : colorList) {
            String s = item.getGray() > watershed ? "0" : "1";
            canvas.drawText(s, item.getX(), item.getY(), paint);
        }
    }
}
