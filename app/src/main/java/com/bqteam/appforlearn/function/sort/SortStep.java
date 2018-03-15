package com.bqteam.appforlearn.function.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 * @date 2018/3/15
 */

public class SortStep {
    private List<Integer> dataList;
    private int a;
    private int b;

    public SortStep(List<Integer> dataList, int a, int b) {
        this.dataList = dataList;
        this.a = a;
        this.b = b;
    }

    public List<Integer> getDataList() {
        return dataList;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
