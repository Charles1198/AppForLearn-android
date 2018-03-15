package com.bqteam.appforlearn.function.sort;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
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
    private List<SortStep> stepList = new ArrayList<>();
    /**
     * 当前在比较的两数的索引
     */
    private int a;
    private int b;

    private int index = 0;

    private String title = "";

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
            viewWidth = this.getMeasuredWidth();
            viewHeight = this.getMeasuredHeight();
            paintWidth = viewWidth / dataList.size();
            paint.setStrokeWidth(paintWidth);
        }
    }

    private void init() {
        paint.setStrokeWidth(viewWidth / dataList.size());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        for (int i = 0; i < dataList.size(); i++) {
            if (i == a) {
                paint.setColor(Color.RED);
            } else if (i == b) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.WHITE);
            }
            float lineStopY = viewHeight - dataList.get(i) * (viewHeight - 40) / 100;
            canvas.drawLine(i * paintWidth + paintWidth / 2, viewHeight, i * paintWidth + paintWidth / 2,
                    lineStopY, paint);
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        canvas.drawText(title + "    执行第" + index + "次", 10, 30, paint);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void initData(List<Integer> dataList) {
        index = 0;
        this.dataList = dataList;
        this.stepList.clear();
        this.a = -1;
        this.b = -1;
        invalidate();
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

        stepList.add(new SortStep(dataList, a, b));
    }

    /**
     * 每隔一定时间画出排序第 index 次的结果
     */
    public void showSort() {
        new Handler().postDelayed(() -> {
            if (index < stepList.size()) {
                dataList = stepList.get(index).getDataList();
                a = stepList.get(index).getA();
                b = stepList.get(index).getB();
                index++;
                invalidate();
                showSort();
            } else {
                a = 0;
                b = 0;
                invalidate();
            }
        }, 20);
    }
}
