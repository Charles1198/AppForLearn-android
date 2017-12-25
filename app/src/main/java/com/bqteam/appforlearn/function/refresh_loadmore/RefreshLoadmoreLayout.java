package com.bqteam.appforlearn.function.refresh_loadmore;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bqteam.appforlearn.R;

/**
 * @author charles
 * @date 2017/12/25
 */

public class RefreshLoadmoreLayout extends ViewGroup {
    /**
     * 正常状态
     */
    public static final int STATUS_NORMAL = 0;

    /**
     * 下拉状态，未达到刷新高度
     */
    public static final int STATUS_PULL_TO_REFRESH = 1;

    /**
     * 下拉状态，已达到刷新高度
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 2;

    /**
     * 正在刷新状态
     */
    public static final int STATUS_REFRESHING = 3;

    /**
     * 上拉状态，未达到加载高度
     */
    public static final int STATUS_PULL_TO_LOADMORE = -1;

    /**
     * 上拉状态，已达到加载高度
     */
    public static final int STATUS_RELEASE_TO_LOADMORE = -2;

    /**
     * 正在加载状态
     */
    public static final int STATUS_LOADMOREING = -3;

    private View headerView;
    private View footerView;

    private ProgressBar refreshProgressBar;
    private ProgressBar loadmoreProgressBar;
    private ImageView refreshImg;
    private ImageView loadmoreImg;
    private TextView refreshTv;
    private TextView loadmoreTv;

    private OnRefreshListener refreshListener;
    private OnLoadmoreListener loadmoreListener;

    private boolean enableRefresh = false;
    private boolean enableLoadmore = false;

    /**
     * 当前状态刷新／加载
     */
    private int status = STATUS_NORMAL;

    private int layoutContentHeight;
    private int headerViewHeight;
    private int footerViewHeight;

    private float lastMoveY;

    public RefreshLoadmoreLayout(Context context) {
        super(context);
    }

    public RefreshLoadmoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        headerView = LayoutInflater.from(context).inflate(R.layout.view_refresh_header, null);
        footerView = LayoutInflater.from(context).inflate(R.layout.view_loadmore_footer, null);

        refreshProgressBar = headerView.findViewById(R.id.refresh_progressBar);
        loadmoreProgressBar = footerView.findViewById(R.id.loadmore_progressBar);
        refreshImg = headerView.findViewById(R.id.refresh_img);
        loadmoreImg = footerView.findViewById(R.id.loadmore_img);
        refreshTv = headerView.findViewById(R.id.refresh_tv);
        loadmoreTv = footerView.findViewById(R.id.loadmore_tv);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        headerView.setLayoutParams(lp);
        footerView.setLayoutParams(lp);
        addView(headerView);
        addView(footerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headerViewHeight = headerView.getMeasuredHeight();
        footerViewHeight = footerView.getMeasuredHeight();
        layoutContentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == headerView) {
                // 头视图隐藏在顶端
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            } else if (child == footerView) {
                // 尾视图隐藏在layout所有内容视图之后
                child.layout(0, layoutContentHeight, child.getMeasuredWidth(),
                        layoutContentHeight + child.getMeasuredHeight());
            } else {
                child.layout(0, layoutContentHeight, child.getMeasuredWidth(),
                        layoutContentHeight + child.getMeasuredHeight());
                layoutContentHeight += child.getMeasuredHeight();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMoveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = lastMoveY - event.getY();
                if (enableScroll(dy)) {
                    scrollBy(0, (int) dy);
                    setStatus(true);
                    lastMoveY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                setStatus(false);

                switch (status) {
                    case STATUS_NORMAL:
                        smoothScrollTo(0);
                        break;
                    case STATUS_REFRESHING:
                        smoothScrollTo(0 - headerViewHeight / 8);
                        break;
                    case STATUS_LOADMOREING:
                        smoothScrollTo(footerViewHeight / 8);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 控制滑动范围
     *
     * @param dy 滑动距离
     * @return
     */
    private boolean enableScroll(float dy) {

        if (!enableRefresh) {

        }
        if (!enableLoadmore) {

        }
        //如果视图向下滑动并且滑动距离超过 headerViewHeight / 4 则禁止再滑动
        //如果视图向上滑动并且滑动距离超过 footerViewHeight / 4 则禁止再滑动
        return !(dy < 0 && (0 - getScrollY()) > headerViewHeight / 4)
                && !(dy > 0 && getScrollY() > footerViewHeight / 4);
    }

    /**
     * 设置当前状态刷新／加载
     *
     * @param touchMove 是否正在滑动
     */
    private void setStatus(boolean touchMove) {
        if (touchMove) {
            int scrollY = getScrollY();
            if (scrollY < 0) {
                if ((0 - scrollY) < headerViewHeight / 8) {
                    status = STATUS_PULL_TO_REFRESH;
                }
                if ((0 - scrollY) >= headerViewHeight / 8 && scrollY < headerViewHeight / 4) {
                    status = STATUS_RELEASE_TO_REFRESH;
                }
            }
            if (scrollY > 0) {
                if (scrollY < footerViewHeight / 8) {
                    status = STATUS_PULL_TO_LOADMORE;
                }
                if (scrollY >= footerViewHeight / 8 && scrollY < footerViewHeight / 4) {
                    status = STATUS_RELEASE_TO_LOADMORE;
                }
            }
        } else {
            switch (status) {
                case STATUS_PULL_TO_REFRESH:
                case STATUS_PULL_TO_LOADMORE:
                    status = STATUS_NORMAL;
                    break;
                case STATUS_RELEASE_TO_REFRESH:
                    status = STATUS_REFRESHING;
                    break;
                case STATUS_RELEASE_TO_LOADMORE:
                    status = STATUS_LOADMOREING;
                    break;
                default:
                    break;
            }
        }
        refreshViews();
    }

    /**
     * 更新视图
     */
    private void refreshViews() {
        switch (status) {
            case STATUS_PULL_TO_REFRESH:
                refreshImg.setVisibility(VISIBLE);
                refreshProgressBar.setVisibility(GONE);
                refreshTv.setText(R.string.pull_to_refresh);
                break;
            case STATUS_RELEASE_TO_REFRESH:
                refreshImg.setVisibility(GONE);
                refreshProgressBar.setVisibility(GONE);
                refreshTv.setText(R.string.release_to_refresh);
                break;
            case STATUS_REFRESHING:
                refreshImg.setVisibility(GONE);
                refreshProgressBar.setVisibility(VISIBLE);
                refreshTv.setText(R.string.refresh_ing);
                break;
            case STATUS_PULL_TO_LOADMORE:
                loadmoreImg.setVisibility(VISIBLE);
                loadmoreProgressBar.setVisibility(GONE);
                loadmoreTv.setText(R.string.pull_to_loadmore);
                break;
            case STATUS_RELEASE_TO_LOADMORE:
                loadmoreImg.setVisibility(GONE);
                loadmoreProgressBar.setVisibility(GONE);
                loadmoreTv.setText(R.string.release_to_loadmore);
                break;
            case STATUS_LOADMOREING:
                loadmoreImg.setVisibility(GONE);
                loadmoreProgressBar.setVisibility(VISIBLE);
                loadmoreTv.setText(R.string.loadmore_ing);
                break;
            default:
                break;
        }
    }

    /**
     * 松开手，滑动到 y 处
     *
     * @param y
     */
    private void smoothScrollTo(int y) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollY(), y).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    private void scrollTo(int y) {
        scrollTo(0, y);
    }



    /////////////////////////
    // 外部方法与接口

    /**
     * 下拉刷新接口
     */
    private interface OnRefreshListener {
        /**
         * 下拉刷新方法
         */
        void refresh();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        refreshListener = listener;
        enableRefresh = true;
    }

    /**
     * 上拉加载接口
     */
    private interface OnLoadmoreListener {
        /**
         * 上拉加载方法
         */
        void loadmore();
    }

    public void setOnLoadmoreListener(OnLoadmoreListener listener) {
        loadmoreListener = listener;
        enableLoadmore = true;
    }
}
