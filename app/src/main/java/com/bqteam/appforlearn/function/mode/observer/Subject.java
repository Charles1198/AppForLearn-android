package com.bqteam.appforlearn.function.mode.observer;

/**
 * @author charles
 * @date 2018/1/4
 */

public interface Subject {
    /**
     * 注册观察者
     * @param observer
     */
    void registerObserver(Observer observer);

    /**
     * 注册观察者
     * @param observer
     */
    void removeObserver(Observer observer);

    /**
     * 注册观察者
     */
    void notifyObserver();
}
