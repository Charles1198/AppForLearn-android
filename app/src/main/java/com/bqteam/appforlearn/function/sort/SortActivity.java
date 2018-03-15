package com.bqteam.appforlearn.function.sort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bqteam.appforlearn.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author charles
 */
public class SortActivity extends AppCompatActivity {
    @BindView(R.id.sortView_insertion)
    SortView sortViewInsertion;
    @BindView(R.id.sortView_shell)
    SortView sortViewShell;
    @BindView(R.id.sortView_heap)
    SortView sortViewHeap;
    @BindView(R.id.sortView_merge)
    SortView sortViewMerge;
    @BindView(R.id.sortView_quick)
    SortView sortViewQuick;

    private List<Integer> dataList = new ArrayList<>();
    private List<Integer> dataListInsertion = new ArrayList<>();
    private List<Integer> dataListShell = new ArrayList<>();
    private List<Integer> dataListHeap = new ArrayList<>();
    private List<Integer> dataListMerge = new ArrayList<>();
    private List<Integer> dataListQuick = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        ButterKnife.bind(this);

        sortViewInsertion.setTitle("插入排序");
        sortViewShell.setTitle("希尔排序");
        sortViewHeap.setTitle("堆排序");
        sortViewMerge.setTitle("归并排序");
        sortViewQuick.setTitle("快速排序");

        initData();
    }

    @OnClick({R.id.random, R.id.sort, R.id.reverse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reverse:
                reverseData();
                break;
            case R.id.random:
                randomData();
                break;
            case R.id.sort:
                insertionSort(dataListInsertion);
                shellSort(dataListShell);
                heapSort(dataListHeap);
                mergeSort(dataListMerge);
                quickSort(dataListQuick);

                sortViewInsertion.showSort();
                sortViewShell.showSort();
                sortViewHeap.showSort();
                sortViewMerge.showSort();
                sortViewQuick.showSort();
                break;
            default:
                break;
        }
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            dataList.add(i);
        }
        initDataToView();
    }

    private void reverseData() {
        dataList.clear();
        for (int i = 0; i < 100; i++) {
            dataList.add(i);
        }
        Collections.reverse(dataList);
        initDataToView();
    }

    private void randomData() {
        Collections.shuffle(dataList);
        initDataToView();
    }

    /**
     * 插入排序
     *
     * @param list
     */
    private void insertionSort(List<Integer> list) {
        int j;
        for (int i = 1; i < list.size(); i++) {
            int temp = list.get(i);
            for (j = i; j > 0 && temp < list.get(j - 1); j--) {
                list.set(j, list.get(j - 1));
                setDataToView(sortViewInsertion, list, i, j - 1);
            }
            list.set(j, temp);
            setDataToView(sortViewInsertion, list, i, j - 1);
        }
    }

    /**
     * 希尔排序
     *
     * @param list
     */
    private void shellSort(List<Integer> list) {
        int j;
        for (int gap = list.size() / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < list.size(); i++) {
                int temp = list.get(i);
                for (j = i; j >= gap && temp < list.get(j - gap); j -= gap) {
                    list.set(j, list.get(j - gap));
                    setDataToView(sortViewShell, list, i, j - 1);
                }
                list.set(j, temp);
                setDataToView(sortViewShell, list, i, j - 1);
            }
        }
    }

    /**
     * 堆排序
     *
     * @param list
     */
    private void heapSort(List<Integer> list) {
        for (int i = list.size() / 2 - 1; i >= 0; i--) {
            percDown(list, i, list.size());
        }
        for (int i = list.size() - 1; i > 0; i--) {
            swap(list, 0, i);

            setDataToView(sortViewHeap, list, i, -1);
            percDown(list, 0, i);
        }
    }

    public static void swap(List<Integer> list, int a, int b) {
        list.set(a, list.get(a) + list.get(b));
        list.set(b, list.get(a) - list.get(b));
        list.set(a, list.get(a) - list.get(b));
    }

    private static int leftChild(int i) {
        return 2 * i + 1;
    }

    private void percDown(List<Integer> list, int i, int n) {
        int child;
        int temp;

        for (temp = list.get(i); leftChild(i) < n; i = child) {
            child = leftChild(i);
            if (child != n - 1 && list.get(child) < list.get(child + 1)) {
                child++;
            }
            if (temp < list.get(child)) {
                list.set(i, list.get(child));
                setDataToView(sortViewHeap, list, i, child);
            } else {
                break;
            }
        }
        list.set(i, temp);
        setDataToView(sortViewHeap, list, i, -1);
    }

    /**
     * 归并排序
     *
     * @param list
     */
    private void mergeSort(List<Integer> list) {
        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tempList.add(0);
        }
        mergeSort(list, tempList, 0, list.size() - 1);
    }

    private void mergeSort(List<Integer> list, List<Integer> tempList, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(list, tempList, left, center);
            mergeSort(list, tempList, center + 1, right);
            merge(list, tempList, left, center + 1, right);
        }
    }

    private void merge(List<Integer> list, List<Integer> tempList, int leftPos, int rightPos, int rightEnd) {
        int leftEnd = rightPos - 1;
        int tempPos = leftPos;
        int numElements = rightEnd - leftPos + 1;

        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (list.get(leftPos) <= list.get(rightPos)) {
                tempList.set(tempPos++, list.get(leftPos++));
            } else {
                tempList.set(tempPos++, list.get(rightPos++));
            }
            setDataToView(sortViewMerge, list, leftPos, rightPos);
        }

        while (leftPos <= leftEnd) {
            tempList.set(tempPos++, list.get(leftPos++));
            setDataToView(sortViewMerge, list, leftPos, -1);
        }

        while (rightPos <= rightEnd) {
            tempList.set(tempPos++, list.get(rightPos++));
            setDataToView(sortViewMerge, list, rightPos, -1);
        }

        for (int i = 0; i < numElements; i++, rightEnd--) {
            list.set(rightEnd, tempList.get(rightEnd));
            setDataToView(sortViewMerge, list, rightEnd, -1);
        }
    }

    /**
     * 快速排序
     * @param list
     */
    private void quickSort(List<Integer> list) {
        if (list.size() <= 1) {
            return;
        }

        List<Integer> smaller = new ArrayList<>();
        List<Integer> small = new ArrayList<>();
        List<Integer> larger = new ArrayList<>();

        int choseItem = list.get(list.size() / 2);
        for (Integer i: list) {
            if (i < choseItem) {
                smaller.add(i);
            } else if (i > choseItem) {
                larger.add(i);
            } else {
                small.add(i);
            }
            setDataToView(sortViewQuick, dataListQuick, i, list.size() / 2);
        }

        quickSort(smaller);
        quickSort(larger);

        list.clear();
        list.addAll(smaller);
        list.addAll(small);
        list.addAll(larger);
        setDataToView(sortViewQuick, dataListQuick, -1, list.size() / 2);
    }

    private void bucketSort(List<Integer> list) {
        List<Integer> count = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            count.set(i, 0);
        }

        for (int i: list) {
            count.set(i, 1);


            for (int c: count) {

            }
        }
    }

    /**
     * 为 SortView 初始化/重新设置数组
     */
    private void initDataToView() {
        dataListInsertion.clear();
        dataListShell.clear();
        dataListHeap.clear();
        dataListMerge.clear();
        dataListQuick.clear();

        dataListInsertion.addAll(dataList);
        dataListShell.addAll(dataList);
        dataListHeap.addAll(dataList);
        dataListMerge.addAll(dataList);
        dataListQuick.addAll(dataList);

        sortViewInsertion.initData(dataListInsertion);
        sortViewShell.initData(dataListShell);
        sortViewHeap.initData(dataListHeap);
        sortViewMerge.initData(dataListMerge);
        sortViewQuick.initData(dataListQuick);
    }

    /**
     * 将每次排序所得数组赋给 sortView
     *
     * @param view 目标 view
     * @param list 当前数组
     * @param a
     * @param b
     */
    private void setDataToView(SortView view, List<Integer> list, int a, int b) {
        List<Integer> l = new ArrayList<>();
        l.addAll(list);
        view.setData(l, a, b);
    }
}
