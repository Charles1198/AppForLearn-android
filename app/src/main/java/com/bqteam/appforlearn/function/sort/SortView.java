package com.bqteam.appforlearn.function.sort;

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
 * @date 2018/3/14
 */

public class SortView extends View {
    /**
     * 画布长宽
     */
    private float viewWidth;
    private float viewHeight;
    /**
     * 当前数据
     */
    private List<Integer> dataList = new ArrayList<>();
    /**
     * 当前在比较的两数的索引
     */
    private int a;
    private int b;

    private Paint paint = new Paint();
    private float paintWidth;

    public SortView(Context context) {
        super(context);
        init();
    }

    public SortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (viewWidth == 0) {
            viewWidth = widthMeasureSpec;
            viewHeight = heightMeasureSpec;
        }
    }

    private void init() {
        paint.setStrokeWidth(viewWidth / dataList.size());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < dataList.size(); i++) {
            if (i == a) {
                paint.setColor(Color.RED);
            } else if (i == b) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.WHITE);
            }
            float lineStopY = viewHeight - dataList.get(i) * viewHeight / 100;
            canvas.drawLine(i * paintWidth, viewHeight, i * paintWidth, lineStopY, paint);
        }
    }

    /**
     * 为 SortView 设置数据
     * @param dataList 当前数组
     * @param a 当前在比较的两数的索引
     * @param b 当前在比较的两数的索引
     */
    public void setData(List<Integer> dataList, int a, int b) {
        this.dataList = dataList;
        this.a = a;
        this.b = b;

        paintWidth = viewWidth / dataList.size();
        paint.setStrokeWidth(paintWidth);
        invalidate();
    }
}
