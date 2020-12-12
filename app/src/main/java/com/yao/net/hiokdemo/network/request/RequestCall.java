package com.yao.net.hiokdemo.network.request;

import com.yao.net.hiokdemo.network.HiOk;
import com.yao.net.hiokdemo.network.callback.OkCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class RequestCall {

    private OkHttpRequest okHttpRequest;

    public RequestCall(OkHttpRequest okHttpRequest) {
        this.okHttpRequest = okHttpRequest;
    }

    public void execute(OkCallback callback) {
        Request request = okHttpRequest.generateRequest(callback);
        if (callback != null) {
            callback.onBefore();
        }
        HiOk.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }
}
