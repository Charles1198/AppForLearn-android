package com.bqteam.appforlearn.function.panorama;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bqteam.appforlearn.R;

/**
 * @author charles
 * @date 2018/1/23
 */

public class InfoPopView extends LinearLayout {
    private OnViewMeasuredListener onViewMeasuredListener;
    private boolean measured = false;

    private TextView nameTv;
    private TextView positionTv;
    private TextView telTv;

    public InfoPopView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_info_pop, this);
        nameTv = findViewById(R.id.info_name);
        positionTv = findViewById(R.id.info_position);
        telTv = findViewById(R.id.info_tel);
    }

    public void setInfo(String name, String position, String tel) {
        nameTv.setText(name);
        positionTv.setText(position);
        telTv.setText(tel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!measured && onViewMeasuredListener != null) {
            onViewMeasuredListener.viewMeasured(this.getMeasuredWidth());
            measured = true;
        }
    }

    public interface OnViewMeasuredListener {
        /**
         * 当 InfoPopView 的尺寸确定后，通知父布局调整位置
         * @param width
         */
        void viewMeasured(int width);
    }

    public void setOnViewMeasuredListener(OnViewMeasuredListener listener) {
        onViewMeasuredListener = listener;
    }
}
