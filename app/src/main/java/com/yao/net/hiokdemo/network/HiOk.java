package com.yao.net.hiokdemo.network;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.yao.net.hiokdemo.network.builder.GetBuilder;
import com.yao.net.hiokdemo.network.builder.PostFormBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class HiOk {

    private static HiOk hiOk;

    private OkHttpClient okHttpClient;
    //这个handler的作用是把子线程切换主线程。
    private Handler mDelivery;
    //防止网络重复请求的tagList;
    private List<String> tagList;

    private HiOk() {
        mDelivery = new Handler(Looper.getMainLooper());
        tagList = new ArrayList<>();
        okHttpClient = new OkHttpClient.Builder()
                // 暂时不需要设置缓存和证书
                //设置缓存文件路径，和文件大小
//                .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/okhttp_cache/"), 50 * 1024 * 1024))
//                .hostnameVerifier(new HostnameVerifier() {//证书信任
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }
    public static HiOk getInstance() {
        if (hiOk == null) {
            synchronized (HiOk.class) {
                if (hiOk == null) {
                    hiOk = new HiOk();
                }
            }
        }
        return hiOk;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public GetBuilder get() {
        return new GetBuilder();
    }
    public PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void cancelTag(String tag) {
        if (TextUtils.isEmpty(tag)) return;
        Dispatcher dispatcher = okHttpClient.dispatcher();
        synchronized (dispatcher) {
            // 请求列表里面的，取消请求
            for (Call call : dispatcher.queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            // 正在请求的，取消请求
            for (Call call : dispatcher.runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
