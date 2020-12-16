package com.yao.net.hiokdemo.network.log;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 日志拦截器
 * Level.body 适应于普通的接口
 * Level.headers 适用于上传文件或者下载文件（body大的情况，如果用body会 OOM）
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(@NotNull String msg) {
        Log.d("hi-ok", msg);//okHttp的详细日志会打印出来
    }
}
