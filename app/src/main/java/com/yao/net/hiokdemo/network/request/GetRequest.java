package com.yao.net.hiokdemo.network.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * get请求
 */
public class GetRequest extends OkHttpRequest {

    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody wrapRequestBody) {
        return builder.get().build();
    }
}
