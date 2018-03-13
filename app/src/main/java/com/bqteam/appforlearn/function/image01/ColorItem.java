package com.bqteam.appforlearn.function.image01;

/**
 * @author charles
 * @date 2018/3/13
 */

public class ColorItem {
    private int x;
    private int y;
    private int gray;

    public ColorItem(int x, int y, int gray) {
        this.x = x;
        this.y = y;
        this.gray = gray;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getGray() {
        return gray;
    }
}
