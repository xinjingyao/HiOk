package com.yao.net.hiokdemo.network.callback;

import java.io.File;

public interface IDownloadCallback {

    void start();
    void inProgress(long total, int progress, long perSpeed);
    void pause();
    void cancel();
    void complete(File file);
    void error(Exception e);
}
