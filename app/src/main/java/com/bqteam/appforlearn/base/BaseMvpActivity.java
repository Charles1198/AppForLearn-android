package com.bqteam.appforlearn.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author charles
 * @date 2018/1/12
 * <p>
 * 基类的Activity，声明一个创建Presenter的抽象方法，因为要帮子类去绑定和解绑那么就需要拿到子类的Presenter才行，但是又不能随便一个类都能绑定的，
 * 因为只有基类的Presenter中才定义了绑定和解绑的方法，所以同样的在类上可以声明泛型在方法上使用泛型来达到目的
 */

public abstract class BaseMvpActivity<V extends BaseMvpView, P extends BaseMvpPresenter> extends AppCompatActivity
        implements BaseMvpView {
    private P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建 presenter
        if (presenter == null) {
            presenter = createPresenter();
        }
        if (presenter == null) {
            throw new NullPointerException("presenter 不能为空");
        } else {
            presenter.attachMvpView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解除绑定
        if (presenter != null) {
            presenter.detachMvpView();
        }
    }

    /**
     * 创建 presenter
     *
     * @return
     */
    public abstract P createPresenter();

    /**
     * 获取 presenter
     */
    public P getPresenter() {
        return presenter;
    }
}
