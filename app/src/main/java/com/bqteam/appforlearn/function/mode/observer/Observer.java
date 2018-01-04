package com.bqteam.appforlearn.function.mode.observer;

/**
 * @author charles
 * @date 2018/1/4
 */

public interface Observer {
    /**
     * 观察到数据变化
     *
     * @param temperature
     * @param humidity
     * @param pressure
     */
    void update(float temperature, float humidity, float pressure);
}
