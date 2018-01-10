package com.bqteam.appforlearn.function.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author charles
 * @date 2018/1/10
 */

public class MovingPointView extends View {
    /**
     * 画布长宽
     */
    private int width;
    private int height;

    /**
     * 画笔
     */
    private Paint paint;

    private int pointCount = 1000;
    private List<Point> pointList = new ArrayList<>();

    /**
     * 定时器
     */
    private Timer timer;

    public MovingPointView(Context context) {
        super(context);
        initPatin();
    }

    public MovingPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPatin();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMeasuredWidth();
        height = this.getMeasuredHeight();

        initPoint();
    }

    private void initPatin() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initTimer();
    }

    private void initTimer() {
        @SuppressLint("HandlerLeak")
        final Handler mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                invalidate();
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainHandler.sendMessage(new Message());
            }
        }, 60, 60);
    }

    private void initPoint() {
        if (pointList.size() > 0) {
            return;
        }
        for (int i = 0; i < pointCount; i++) {
            Point p = new Point((float) (Math.random() * width), (float) (Math.random() * height),
                    (float) (Math.random() * 15), (int) (Math.random() * 120 + 20));
            pointList.add(p);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Point p : pointList) {
            paint.setARGB(p.getAlpha(), 0, 0, 0);
            paint.setStrokeWidth(p.getR());
            canvas.drawCircle(p.getX(), p.getY(), p.getR(), paint);

            p.move(width, height);
        }
    }
}
