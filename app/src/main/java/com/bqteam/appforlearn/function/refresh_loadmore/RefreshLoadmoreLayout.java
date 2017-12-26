package com.bqteam.appforlearn.function.refresh_loadmore;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
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

    /**
     * 加载完毕，没有更多数据
     */
    public static final int STATUS_LOADMORE_NO_MORE_DATA = -4;

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
    private boolean loadmoreNoMoreData = false;

    /**
     * 当前状态刷新／加载
     */
    private int status = STATUS_NORMAL;
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
        /*
        按照 onFinishInflate() 方法中的添加顺序
        getChildAt(0)： contentView
        getChildAt(1)： headerView
        getChildAt(2)： footerView
         */
        int viewWidth = headerView.getMeasuredWidth();
        headerViewHeight = headerView.getMeasuredHeight();
        footerViewHeight = footerView.getMeasuredHeight();

        // 内容视图放在中间
        View contentView = getChildAt(0);
        int contentHeight = contentView.getMeasuredHeight();
        contentView.layout(0, 0, viewWidth, contentHeight);
        // 头视图隐藏在顶端
        headerView.layout(0, 0 - headerViewHeight, viewWidth, 0);
        // 尾视图隐藏在layout所有内容视图之后
        footerView.layout(0, contentHeight, footerViewHeight, contentHeight + footerViewHeight);
    }

    /**
     * 是否需要拦截子 view 滑动事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                lastMoveY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                View child = getChildAt(0);
                if (ev.getY() > lastMoveY) {
                    //下滑操作，如果子 view 还能下滑就不拦截
                    intercept = !child.canScrollVertically(-1);
                }
                if (ev.getY() < lastMoveY) {
                    //上滑操作，如果子 view 还能上滑就不拦截
                    intercept = !child.canScrollVertically(1);
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }
        return intercept;
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
                    lastMoveY = event.getY();

                    setStatus(true);
                    refreshViews();
                }
                break;
            case MotionEvent.ACTION_UP:
                setStatus(false);
                refreshViews();

                switch (status) {
                    case STATUS_REFRESHING:
                        smoothScrollTo(0 - headerViewHeight / 8);
                        if (enableRefresh) {
                            refreshListener.refresh();
                        }
                        break;
                    case STATUS_LOADMOREING:
                        smoothScrollTo(footerViewHeight / 8);
                        if (enableLoadmore) {
                            loadmoreListener.loadmore();
                        }
                        break;
                    default:
                        smoothScrollTo(0);
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
        if (!enableRefresh && !enableLoadmore) {
            return false;
        }

        //如果视图向下滑动并且滑动距离超过 headerViewHeight / 4 则禁止再滑动
        if (dy < 0 && (0 - getScrollY()) > headerViewHeight / 4) {
            return false;
        }

        //如果视图向上滑动并且滑动距离超过 footerViewHeight / 4 则禁止再滑动
        if (dy > 0 && getScrollY() > footerViewHeight / 4) {
            return false;
        }

        if (!enableRefresh) {
            //禁止刷新时不能下拉
            if (getScrollY() <= 0 && dy < 0) {
                return false;
            }
        }
        if (!enableLoadmore) {
            //禁止加载时不能上拉
            if (getScrollY() >= 0 && dy > 0) {
                return false;
            }
        } else {
            if (loadmoreNoMoreData) {
                //如果未禁止加载，并且没有更多数据了，上滑距离超过 headerViewHeight / 8 禁止再滑动
                if (getScrollY() > footerViewHeight / 8) {
                    return false;
                }
            }
        }

        return true;
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
                if (!loadmoreNoMoreData) {
                    if (scrollY < footerViewHeight / 8) {
                        status = STATUS_PULL_TO_LOADMORE;
                    }
                    if (scrollY >= footerViewHeight / 8 && scrollY < footerViewHeight / 4) {
                        status = STATUS_RELEASE_TO_LOADMORE;
                    }
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
            case STATUS_LOADMORE_NO_MORE_DATA:
                loadmoreImg.setVisibility(GONE);
                loadmoreProgressBar.setVisibility(GONE);
                loadmoreTv.setText(R.string.loadmore_no_more_data);
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
    public interface OnRefreshListener {
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
    public interface OnLoadmoreListener {
        /**
         * 上拉加载方法
         */
        void loadmore();
    }

    public void setOnLoadmoreListener(OnLoadmoreListener listener) {
        loadmoreListener = listener;
        enableLoadmore = true;
    }

    /**
     * 刷新结束
     */
    public void refreshEnd() {
        status = STATUS_NORMAL;
        refreshViews();
        smoothScrollTo(0);

        if (enableLoadmore) {
            loadmoreNoMoreData = false;
        }
    }

    /**
     * 加载结束
     *
     * @param noMoreData 还有没有数据
     */
    public void loadmoreEnd(boolean noMoreData) {
        loadmoreNoMoreData = noMoreData;
        status = noMoreData ? STATUS_LOADMORE_NO_MORE_DATA : STATUS_NORMAL;
        refreshViews();
        smoothScrollTo(0);
    }
}
