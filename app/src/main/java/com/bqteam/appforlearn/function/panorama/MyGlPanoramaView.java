package com.bqteam.appforlearn.function.panorama;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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


    private Handler mHandlers = new Handler();
    int yy = 0;

    public MyGlPanoramaView(Context context) {
        super(context);
        this.mContext = context;
        this.init();
    }

    public MyGlPanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.init();
    }

    public MyGlPanoramaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.initView();
    }

    private void initView() {
        LayoutInflater.from(this.mContext).inflate(com.zph.glpanorama.R.layout.panoramalayout, this);
        this.mGlSurfaceView = this.findViewById(com.zph.glpanorama.R.id.mIViews);
        this.img = this.findViewById(com.zph.glpanorama.R.id.img);
        this.img.setOnClickListener(view -> zero());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case 1:
                Log.d("down", "onTouchEvent: " + event.getX() + ", " + event.getY() + ", "
                        + mBall.getfinalMVPMatrix()[0] + ", "
                        + mBall.getfinalMVPMatrix()[1] + ", "
                        + mBall.getfinalMVPMatrix()[2]);
                break;
            case 2:
                float dy = y - this.mPreviousYs;
                float dx = x - this.mPreviousXs;
                this.mBall.yAngle += dx * 0.3F;
                this.mBall.xAngle += dy * 0.3F;
                if (this.mBall.xAngle < -50.0F) {
                    this.mBall.xAngle = -50.0F;
                } else if (this.mBall.xAngle > 50.0F) {
                    this.mBall.xAngle = 50.0F;
                }

                this.rotate();
                break;
            default:
                break;
        }


        this.mPreviousYs = y;
        this.mPreviousXs = x;
        return true;
    }

    public void setPanoramaImage(int imgId) {
        this.mGlSurfaceView.setEGLContextClientVersion(2);
        this.mBall = new Ball(this.mContext, imgId);
        this.mGlSurfaceView.setRenderer(this.mBall);
    }

    private void rotate() {
        RotateAnimation anim = new RotateAnimation(this.preDegree, -this.mBall.yAngle, 1, 0.5F, 1, 0.5F);
        anim.setDuration(200L);
        this.img.startAnimation(anim);
        this.preDegree = -this.mBall.yAngle;
    }

    private void zero() {
        this.yy = (int) ((this.mBall.yAngle - 90.0F) / 10.0F);
        this.mHandlers.post(new Runnable() {
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
}
