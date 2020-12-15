package com.yao.net.hiokdemo.network.callback;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * 泛型回调解析
 * @param <T>
 */
public abstract class GenericCallback<T> extends AbsCallback<T> {

    @Override
    public T parseResponse(Response response) throws Exception {
        if (response == null
                || response.body() == null
                || TextUtils.isEmpty(response.body().string())) {
            return (T) "";
        }
        String body = response.body().string();
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) body;
        }
        // 把body给解析成对应的类型class
        T t = new Gson().fromJson(body, entityClass);

        return t;
    }
}
