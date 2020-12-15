package com.yao.net.hiokdemo.network.builder;

import com.yao.net.hiokdemo.network.request.DownloadRequest;

public class DownloadBuilder extends RequestBuilder<DownloadBuilder> implements IDownloadBuilder{

    protected String filePath;
    protected String fileName;
    protected boolean resume;

    @Override
    public DownloadRequest build() {
        return new DownloadRequest(url, tag, filePath, fileName, resume);
    }

    public DownloadBuilder filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public DownloadBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DownloadBuilder resume(boolean resume) {
        this.resume = resume;
        return this;
    }
}
