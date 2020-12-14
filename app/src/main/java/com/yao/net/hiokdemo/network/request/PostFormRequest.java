package com.yao.net.hiokdemo.network.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * post 表单请求
 */
public class PostFormRequest extends OkHttpRequest {

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (params == null || params.isEmpty()) return null;

        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        return builder.build();
    }

    @Override
    protected Request buildRequest(RequestBody wrapRequestBody) {
        return builder.post(wrapRequestBody).build();
    }
}
