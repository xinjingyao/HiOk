package com.yao.net.hiokdemo.network.callback;

import android.text.TextUtils;
import android.view.TextureView;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * 泛型回调解析
 *
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
        String msg = "";
        int code = 0;
        String result = "";

        String body = response.body().string();
        if (TextUtils.isEmpty(body)) {
            return (T) msg;
        }
        JSONObject object = new JSONObject(body);
        if (object.has("code")) {
            code = object.getInt("code");
        }
        if (object.has("msg")) {
            msg = object.getString("msg");
        }
        if (object.has("result")) {
            result = object.getString("result");
        }
//        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        if (entityClass == String.class) {
//            return (T) body;
//        }
        if (code == 10000) {
            if (!TextUtils.isEmpty(result)) {
                // 把body给解析成对应的类型class
                Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                T t = new Gson().fromJson(body, entityClass);
                return t;
            }
        } else {
            return (T) msg;
        }
        return (T) msg;
    }
}
