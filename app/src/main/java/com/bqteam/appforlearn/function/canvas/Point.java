package com.bqteam.appforlearn.function.canvas;

import android.graphics.Color;

/**
 * @author charles
 * @date 2018/1/10
 */

public class Point {
    /**
     * 圆点坐标及半径
     */
    private float x;
    private float y;
    private float r;
    private int alpha;

    /**
     * 圆点横向和纵向移动速度
     */
    private float vx = (float) (Math.random() - 0.5);
    private float vy = (float) Math.random();

    public Point(float x, float y, float r, int alpha) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.alpha = alpha;
    }

    /**
     * 移动圆点
     *
     * @param width 画布宽
     * @param height 画布高
     */
    public void move(int width, int height) {
        if (x <= 0 || x >= width) {
            vx = 0 - vx;
        }
        if (y <= 0) {
            y += height;
        }
        this.x += vx;
        this.y -= vy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }

    public int getAlpha() {
        return alpha;
    }
}
