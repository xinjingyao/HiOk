package com.yao.net.hiokdemo.network.builder;

import com.yao.net.hiokdemo.network.request.PostStringRequest;
import com.yao.net.hiokdemo.network.call.RequestCall;

import okhttp3.MediaType;

public class PostStringBuilder extends RequestBuilder<PostStringBuilder> implements IDefaultBuilder{

    private String content;
    private MediaType mediaType;

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType).build();
    }

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }
}
