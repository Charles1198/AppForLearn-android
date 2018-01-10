package com.bqteam.appforlearn.function.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 * @date 2018/1/10
 */

public class MovingPointSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
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

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private boolean running;

    public MovingPointSurfaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPatin();
        init();
    }

    private void initPatin() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running = true;
        new Thread(this).start();
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        initPoint();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            draw();
        }
    }

    private void draw() {
        try {
            //获得canvas对象
            canvas = surfaceHolder.lockCanvas();
            //绘制背景
            canvas.drawColor(Color.WHITE);
            //绘制圆点
            for (Point p : pointList) {
                paint.setARGB(p.getAlpha(), 1, 1, 1);
                paint.setStrokeWidth(p.getR());
                canvas.drawCircle(p.getX(), p.getY(), p.getR(), paint);
                p.move(width, height);
            }
        } catch (Exception e) {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
