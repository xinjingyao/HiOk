package com.yao.net.hiokdemo.network.builder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * request的builder类
 * @param <T>
 */
public abstract class RequestBuilder<T extends RequestBuilder> {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

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

    public T params(Map<String, String> params) {
        this.params = params;
        return (T) this;
    }

    public T addParam(String key, String value) {
        if (params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, value);
        return (T) this;
    }
}
