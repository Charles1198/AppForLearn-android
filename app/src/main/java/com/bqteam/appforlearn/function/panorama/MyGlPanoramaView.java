package com.bqteam.appforlearn.function.panorama;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bqteam.appforlearn.util.LogUtil;
import com.zph.glpanorama.glutils.Ball;
import com.zph.glpanorama.glutils.IViews;

/**
 * @author charles
 * @date 2018/1/20
 */

public class MyGlPanoramaView extends RelativeLayout {
    private Context mContext;
    private IViews mGlSurfaceView;
    private float mPreviousYs;
    private float mPreviousXs;
    private Ball mBall;

    private InfoPopView infoPopView;
    private LayoutParams lp;

    private int viewHeight = 0;
    private int viewWidth = 0;
    private int popViewWidth = 0;

    /**
     * 需要弹出 InfoPopView 的位置
     */
    private float targetX;
    private float targetY;

    /**
     * InfoPopView 的位置与画面中心的相对角度
     */
    private float diffDegreeX;
    private float diffDegreeY;

    public MyGlPanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(com.zph.glpanorama.R.layout.panoramalayout, this);
        mGlSurfaceView = findViewById(com.zph.glpanorama.R.id.mIViews);
        ImageView img = findViewById(com.zph.glpanorama.R.id.img);
        img.setVisibility(INVISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 显示区域的宽高计算出来后再放置 InfoPopView
        if (viewWidth == 0) {
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
            addText();
            calculateDiff();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dy = y - mPreviousYs;
            float dx = x - mPreviousXs;
            mBall.yAngle += dx * 0.3F;
            mBall.xAngle += dy * 0.3F;
            if (mBall.xAngle < -50.0F) {
                mBall.xAngle = -50.0F;
            } else if (mBall.xAngle > 50.0F) {
                mBall.xAngle = 50.0F;
            }

            moveText(mBall.xAngle, mBall.yAngle);
        }

        mPreviousYs = y;
        mPreviousXs = x;
        return true;
    }

    public void setPanoramaImage(int imgId) {
        mGlSurfaceView.setEGLContextClientVersion(2);
        mBall = new Ball(mContext, imgId);
        mGlSurfaceView.setRenderer(mBall);
    }

    public void setTarget(float x, float y) {
        targetX = x;
        targetY = y;
    }

    private void addText() {
        infoPopView = new InfoPopView(mContext);
        infoPopView.setInfo("张三", "大堂经理", "Tel:18888888888");
        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = viewWidth / 2;
        lp.topMargin = viewHeight / 2;
        addView(infoPopView, lp);
        infoPopView.setVisibility(INVISIBLE);
        infoPopView.setOnViewMeasuredListener((width) -> {
            popViewWidth = width;
        });
    }

    private void moveText(float xAngle, float yAngle) {
        yAngle += diffDegreeX;
        xAngle += diffDegreeY;
        if (yAngle > 0) {
            yAngle = yAngle % 360;
        } else {
            yAngle = yAngle % 360 + 360;
        }
        double x = 0 - 600 * Math.cos(yAngle / 180 * Math.PI);
        double y = 10 * xAngle;

        if (yAngle < 55 || yAngle > 125) {
            infoPopView.setVisibility(INVISIBLE);
        } else if (Math.abs(xAngle) > 35) {
            infoPopView.setVisibility(INVISIBLE);
        } else {
            infoPopView.setVisibility(VISIBLE);
            lp.leftMargin = (int) (viewWidth / 2 - popViewWidth / 2 + x);
            lp.topMargin = (int) (viewHeight / 2 + y);
            infoPopView.setLayoutParams(lp);
        }
    }

    private void calculateDiff() {
        diffDegreeX = (targetX / 1920 - 1) * 180;
        diffDegreeY = (targetY / 960 - 1) * 90;
    }
}
