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
    private ImageView img;
    private float mPreviousYs;
    private float mPreviousXs;
    private float preDegree = 0.0F;
    private Ball mBall;

    private TextView testTv;

    private int imageHeight = 0;
    private int imageWidth = 0;

    private int targetX;
    private int targetY;
    private float diffDegreeX;
    private float diffDegreeY;


    private Handler mHandlers = new Handler();
    int yy = 0;

    public MyGlPanoramaView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyGlPanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyGlPanoramaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(com.zph.glpanorama.R.layout.panoramalayout, this);
        mGlSurfaceView = findViewById(com.zph.glpanorama.R.id.mIViews);
        img = findViewById(com.zph.glpanorama.R.id.img);
        img.setOnClickListener(view -> zero());

        addText();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (imageWidth == 0) {
            imageWidth = getMeasuredWidth();
            imageHeight = getMeasuredHeight();
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

            rotate();
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

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }

    private void rotate() {
        RotateAnimation anim = new RotateAnimation(preDegree, -mBall.yAngle, 1, 0.5F, 1, 0.5F);
        anim.setDuration(200L);
        img.startAnimation(anim);
        preDegree = -mBall.yAngle;
    }

    private void zero() {
        yy = (int) ((mBall.yAngle - 90.0F) / 10.0F);
        mHandlers.post(new Runnable() {
            @Override
            public void run() {
                if (yy != 0) {
                    if (yy > 0) {
                        mBall.yAngle -= 10.0F;
                        mHandlers.postDelayed(this, 16L);
                        --yy;
                    }

                    if (yy < 0) {
                        mBall.yAngle += 10.0F;
                        mHandlers.postDelayed(this, 16L);
                        ++yy;
                    }
                } else {
                    mBall.yAngle = 90.0F;
                }

                mBall.xAngle = 0.0F;
            }
        });
    }

    private void addText() {
        testTv = new TextView(mContext);
        testTv.setText("test");
        testTv.setTextColor(Color.WHITE);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = imageWidth / 2;
        lp.topMargin = imageHeight / 2;
        addView(testTv, lp);
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

        if (yAngle < 50 || yAngle > 130) {
            testTv.setVisibility(INVISIBLE);
        } else if (Math.abs(xAngle) > 40) {
            testTv.setVisibility(INVISIBLE);
        } else {
            testTv.setVisibility(VISIBLE);
            LayoutParams lp = (LayoutParams) testTv.getLayoutParams();
            lp.leftMargin = (int) (imageWidth / 2 + x);
            lp.topMargin = (int) (imageHeight / 2 + y);
            testTv.setLayoutParams(lp);
        }
    }

    private void calculateDiff() {
        diffDegreeX = (targetX / (imageWidth / 2) - 1) * 180;
        diffDegreeY = (targetY / (imageHeight / 2) - 1) * 90;
        LogUtil.d(diffDegreeX, diffDegreeY);
    }
}
