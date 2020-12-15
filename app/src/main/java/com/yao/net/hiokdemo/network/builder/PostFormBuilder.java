package com.yao.net.hiokdemo.network.builder;

import com.yao.net.hiokdemo.network.request.PostFormRequest;
import com.yao.net.hiokdemo.network.call.RequestCall;

/**
 * post 表单构建
 */
public class PostFormBuilder extends RequestBuilder<PostFormBuilder>  implements IDefaultBuilder{
    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers).build();
    }
}
