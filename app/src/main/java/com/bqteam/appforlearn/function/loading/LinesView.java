package com.bqteam.appforlearn.function.loading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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
 * @date 2018/3/16
 */

public class LinesView extends View {
    private OnAnimatePeriodListener animatePeriodListener;

    /**
     * 画布宽高
     */
    private int viewWidth;
    private int viewHeight;

    private List<Point> linePoints = new ArrayList<>();
    private boolean pointsInitialized = false;

    private Paint paint;

    /**
     * 画面刷新周期
     */
    private int redrawPeriod = 10;

    /**
     * 定时器
     */
    private Timer timer;
    private TimerTask timerTask;
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public LinesView(Context context) {
        super(context);
        init();
    }

    public LinesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        initPoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (linePoints.size() < 8) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            Point p1 = linePoints.get(i);
            Point p2 = linePoints.get(i + 4);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
        }

        moveLines();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#414149"));
        paint.setStrokeWidth(10);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 0, redrawPeriod);
    }

    private void initPoints() {
        if (pointsInitialized) {
            return;
        }
        pointsInitialized = true;

        //四条线，每条线上顶点和下顶点x方向偏移距离
        float offsetX = viewWidth / 6;

        //定义四条线即八个点，第15、26、37、48个点分表表示一条线
        linePoints.add(new Point((int) -offsetX, 0));
        linePoints.add(new Point((int) offsetX, 0));
        linePoints.add(new Point((int) (offsetX * 3), 0));
        linePoints.add(new Point((int) (offsetX * 5), 0));

        linePoints.add(new Point(0, viewHeight));
        linePoints.add(new Point((int) (offsetX * 2), viewHeight));
        linePoints.add(new Point((int) (offsetX * 4), viewHeight));
        linePoints.add(new Point((int) (offsetX * 6), viewHeight));

        animatePeriodListener.animatePeriod((int) (offsetX / 2 * 10));
    }

    /**
     * 将线条向右移动，如果线条超出范围就将其移至左下角
     */
    private void moveLines() {
        if (linePoints.size() < 8) {
            return;
        }

        float offsetX = viewWidth / 6;

        for (int i = 0; i < 4; i++) {
            Point p = linePoints.get(i);
            if (p.x < viewWidth + offsetX) {
                p.x += 2;
            } else {
                p.x = (int) (0 - offsetX);
            }
        }

        for (int i = 4; i < 8; i++) {
            Point p = linePoints.get(i);
            if (p.x < viewWidth + offsetX * 2) {
                p.x += 2;
            } else {
                p.x = 0;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    public interface OnAnimatePeriodListener {
        void animatePeriod(int period);
    }

    public void setOnAnimatePeriodListener(OnAnimatePeriodListener listener) {
        animatePeriodListener = listener;
    }
}

