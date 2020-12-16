package com.yao.net.hiokdemo.network.request;

import android.util.Log;

import com.yao.net.hiokdemo.network.HiOk;
import com.yao.net.hiokdemo.network.call.RequestCall;
import com.yao.net.hiokdemo.network.callback.IDownloadCallback;
import com.yao.net.hiokdemo.network.log.HttpLogger;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class DownloadRequest {

    private String url;
    private Object tag;
    private String filePath;
    private String fileName;
    private boolean resume;

    protected Request.Builder builder = new Request.Builder();
    private long currentLength;
    private OkHttpClient client;

    public DownloadRequest(String url, Object tag, String filePath, String fileName, boolean resume) {
        this.url = url;
        this.tag = tag;
        this.fileName = fileName;
        this.filePath = filePath;
        this.resume = resume;
        builder.url(url);
        if (tag != null) {
            builder.tag(tag);
        }
    }

    public void execute(final IDownloadCallback callback) {
        if (resume) {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                currentLength = file.length();
                builder.header("RANGE", "bytes=" + currentLength + "-");
            }
        }
        // 开始
        callback.start();
        Request request = builder.get().build();
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.error(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                FileOutputStream fos = null;
                InputStream is = null;
                long sum = 0; // 下载总大小
                long total = 0; // 文件总大小
                int len = 0; // 每次读取的长度
                byte[] buff = new byte[1024];

                try {
                    File dir = new File(filePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(filePath, fileName);

                    is = response.body().byteStream();
                    if (resume) {
                        fos = new FileOutputStream(file, true);
                        sum = currentLength;
                        total = response.body().contentLength() + currentLength;
                    } else {
                        fos = new FileOutputStream(file);
                        sum = 0;
                        total = response.body().contentLength();
                    }
                    while ((len = is.read(buff)) != -1) {
                        fos.write(buff, 0, len);
                        sum += len;
                        // 进度
                        int progress = (int) (sum * 1.0f / total * 100);
                        callback.inProgress(total, progress);
                    }
                    // 写完刷新下
                    fos.flush();
                    callback.complete(file);
                } catch (IOException e) {
                    Log.d("IOException", e.getMessage());
                    callback.error(e);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    callback.error(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                        callback.error(e);
                    }

                }

            }
        });
    }

    private OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
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
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLogger()).setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .retryOnConnectionFailure(true)            //是否自动重连
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .build();
        }
        return client;
    }
}
