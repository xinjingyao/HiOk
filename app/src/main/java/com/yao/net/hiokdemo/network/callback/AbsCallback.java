package com.yao.net.hiokdemo.network.callback;

import okhttp3.Response;

public abstract class AbsCallback<T> implements ICallback<T> {

    public abstract T parseResponse(Response response) throws Exception;

    @Override
    public void onBefore() {

    }

    @Override
    public void onAfter() {

    }
}
