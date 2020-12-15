package com.yao.net.hiokdemo.network.request;

import com.yao.net.hiokdemo.network.call.RequestCall;
import com.yao.net.hiokdemo.network.callback.AbsCallback;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

public abstract class OkHttpRequest {

    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected Request.Builder builder = new Request.Builder();

    public OkHttpRequest(String url,
                         Object tag,
                         Map<String, String> params,
                         Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        initBuilder();
    }

    /**
     * 初始化一些基本参数 url，tag，headers
     */
    private void initBuilder() {
        builder.url(url).tag(tag);

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;
        for (String key : headers.keySet()) {
            headersBuilder.add(key, headers.get(key));
        }
        builder.headers(headersBuilder.build());
    }

    public RequestCall build() {
        return new RequestCall(this);
    }

    /**
     * 生成请求
     * @param callback
     * @return
     */
    public Request generateRequest(AbsCallback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrapRequestBody = wrapRequestBody(requestBody, callback);
        return buildRequest(wrapRequestBody);
    }

    /**
     * 上传文件时会对body进行处理
     * @param requestBody
     * @param callback
     * @return
     */
    protected RequestBody wrapRequestBody(RequestBody requestBody, AbsCallback callback) {

        return requestBody;
    }

    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody wrapRequestBody);
}
