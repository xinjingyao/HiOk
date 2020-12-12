package com.yao.net.hiokdemo.network.builder;

import com.yao.net.hiokdemo.network.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class RequestBuilder<T extends RequestBuilder> {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected int id;

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String value) {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, value);
        return (T) this;
    }

    public abstract RequestCall build();
}
