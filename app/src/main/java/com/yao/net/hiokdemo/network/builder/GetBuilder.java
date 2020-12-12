package com.yao.net.hiokdemo.network.builder;

import android.net.Uri;

import com.yao.net.hiokdemo.network.request.GetRequest;
import com.yao.net.hiokdemo.network.request.RequestCall;

import java.util.Map;

/**
 * get请求的参数构建
 */
public class GetBuilder extends RequestBuilder<GetBuilder> {

    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, tag, params, headers).build();
    }

    /**
     * 将参数拼接到url后面
     * @param url
     * @param params
     * @return
     */
    private String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String key : params.keySet()) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}
