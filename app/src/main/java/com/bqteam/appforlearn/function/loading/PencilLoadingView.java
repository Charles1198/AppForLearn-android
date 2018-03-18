package com.bqteam.appforlearn.function.loading;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bqteam.appforlearn.R;

/**
 * @author charles
 * @date 2018/3/16
 */

public class PencilLoadingView extends RelativeLayout {
    private ImageView pencil;
    private LinesView linesView;

    public PencilLoadingView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PencilLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading_pencil, this);
        pencil = view.findViewById(R.id.loading_pencil);

        linesView = view.findViewById(R.id.loading_lines);
        linesView.setOnAnimatePeriodListener(new LinesView.OnAnimatePeriodListener() {
            @Override
            public void animatePeriod(int period) {
                animatePencil(period);
            }
        });
    }

    private void animatePencil(int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(pencil, "translationY",
                pencil.getMeasuredHeight(), 0);
        animator.setDuration(duration);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.start();
        Log.d("height", ": " + linesView.getMeasuredHeight());
    }
}
