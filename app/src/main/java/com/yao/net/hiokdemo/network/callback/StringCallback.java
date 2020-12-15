package com.yao.net.hiokdemo.network.callback;

import okhttp3.Response;

/**
 * 字符串回调解析
 */
public abstract class StringCallback extends AbsCallback<String> {

    @Override
    public String parseResponse(Response response) throws Exception {

        if (response == null || response.body() == null) {
            return "";
        } else {
            return response.body().string();
        }
    }
}
