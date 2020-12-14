package com.yao.net.hiokdemo.network.request;

import com.yao.net.hiokdemo.network.util.Exceptions;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostStringRequest extends OkHttpRequest {

    public static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");

    private String content;
    private MediaType mediaType;

    public PostStringRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String content, MediaType mediaType) {
        super(url, tag, params, headers);
        this.content = content;
        this.mediaType = mediaType;
        if (this.content == null) {
            Exceptions.illegalArgument("content can not be null.");
        }
        // 默认纯文本
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_PLAIN;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(content, mediaType);
    }

    @Override
    protected Request buildRequest(RequestBody wrapRequestBody) {
        return builder.post(wrapRequestBody).build();
    }
}
