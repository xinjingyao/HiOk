package com.yao.net.hiokdemo.network.call;

import com.yao.net.hiokdemo.network.HiOk;
import com.yao.net.hiokdemo.network.callback.AbsCallback;
import com.yao.net.hiokdemo.network.request.OkHttpRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求的统一封装
 */
public class RequestCall {

    private OkHttpRequest okHttpRequest;

    public RequestCall(OkHttpRequest okHttpRequest) {
        this.okHttpRequest = okHttpRequest;
    }

    public void execute(final AbsCallback callback) {
        Request request = okHttpRequest.generateRequest(callback);
        if (callback != null) {
            callback.onBefore();
        }
        HiOk.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailure(e, callback);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (callback == null) return;
                try {
                    if (call.isCanceled()) {
                        handleFailure(new IOException("Canceled"), callback);
                        return;
                    }
                    if (response.isSuccessful()) {
                        Object o = callback.parseResponse(response);
                        handleSuccess(o, callback);
                    } else {
                        handleFailure(new IOException("request failed, response's code =" + response.code()), callback);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handleFailure(e, callback);
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    /**
     * 处理成功的回调
     * @param o
     * @param callback
     */
    private void handleSuccess(final Object o, final AbsCallback callback) {
        if (callback == null) return;
        HiOk.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(o);
                callback.onAfter();
            }
        });
    }

    /**
     * 处理失败逻辑
     *
     * @param e
     * @param callback
     */
    private void handleFailure(final Exception e, final AbsCallback callback) {
        if (callback == null) return;
        HiOk.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onError(e);
                callback.onAfter();
            }
        });
    }
}
