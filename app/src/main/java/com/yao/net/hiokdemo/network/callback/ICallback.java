package com.yao.net.hiokdemo.network.callback;

public interface ICallback<T> {

    void onBefore();
    void onSuccess(T response);
    void onError(Exception e);
    void onAfter();
}
