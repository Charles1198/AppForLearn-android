package com.bqteam.appforlearn.base;

/**
 * @author charles
 * @date 2018/1/12
 * <p>
 * 基类的 Presenter，在类上规定 View 泛型，然后定义绑定和解绑的方法，对外提供一个获取 View 的方法，让子类直接通过方法来获取 View
 */

public abstract class BaseMvpPresenter<V extends BaseMvpView> {
    private V mvpView;

    /**
     * 绑定 view
     *
     * @param view
     */
    public void attachMvpView(V view) {
        mvpView = view;
    }

    /**
     * 解除绑定 view
     */
    public void detachMvpView() {
        mvpView = null;
    }

    /**
     * 获取 view
     *
     * @return
     */
    public V getMvpView() {
        return mvpView;
    }
}
