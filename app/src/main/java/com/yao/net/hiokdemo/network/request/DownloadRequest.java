package com.yao.net.hiokdemo.network.request;

import android.util.Log;

import com.yao.net.hiokdemo.network.HiOk;
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

    private static final String TAG = "hi-ok-download";

    private String url;
    private Object tag;
    private String filePath;
    private String fileName;
    private boolean resume;
    private long speed;

    protected Request.Builder builder = new Request.Builder();
    private long currentLength;
    private OkHttpClient client;
    private Call call;

    private boolean isCancel;
    private boolean isPause;
    private IDownloadCallback callback;

    public DownloadRequest(String url, Object tag, String filePath, String fileName, boolean resume, long limitSpeed) {
        this.url = url;
        this.tag = tag;
        this.fileName = fileName;
        this.filePath = filePath;
        this.resume = resume;
        this.speed = limitSpeed;
        builder.url(url);
        if (tag != null) {
            builder.tag(tag);
        }
    }

    public DownloadRequest execute(final IDownloadCallback callback) {
        this.callback = callback;
        File file = new File(filePath, fileName);
        if (resume) {
            if (file.exists()) {
                currentLength = file.length();
                builder.header("RANGE", "bytes=" + currentLength + "-");
            }
        } else {
            if (file.exists()) {
                file.delete();
                Log.d(TAG, "==file delete");
            }
        }
        if (call != null && call.isExecuted()) {
            Log.d(TAG, "==is executing");
            return this;
        }
        // 开始
        sendStart(callback);
        Request request = builder.get().build();
        call = getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendError(e, callback);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                FileOutputStream fos = null;
                InputStream is = null;
                long sum; // 下载总大小
                long total; // 文件总大小
                int readLen; // 每次读取的长度
                byte[] buff = new byte[1024]; // 设置一个buffer
                long perStartTime = System.currentTimeMillis();

                try {
                    File dir = new File(filePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(filePath, fileName);

                    // 判断是否下载完成
                    if (currentLength >= getContentLength(url)) {
                        Log.d(TAG, "--下载已完成");
                        sendComplete(file, callback);
                        return;
                    }
                    // 计算已下载的总大小
                    long bodyLength = response.body().contentLength();
                    Log.d(TAG, "bodyLength=" + bodyLength);
                    if (resume) {
                        fos = new FileOutputStream(file, true);
                        sum = currentLength;
                        total = bodyLength + currentLength;
                    } else {
                        fos = new FileOutputStream(file);
                        sum = 0;
                        total = bodyLength;
                    }

                    is = response.body().byteStream();
                    long startReadPer = sum;
                    // 边读边写
                    while (true) {
                        if (isCancel) { // 取消
                            call.cancel();
                            sendCancel(callback);
                            return;
                        } else if (isPause) { // 暂停
//                            sendPause(callback);
                        } else { // 读写
                            long startReadTime = System.nanoTime();
                            readLen = is.read(buff);
                            // 由于计算读写时间，while()里面不再读取，这里要判断下是否读取完成
                            if (readLen == -1) {
                                Log.d(TAG, "下载完成");
                                break;
                            }
                            fos.write(buff, 0, readLen);
                            sum += readLen;
                            long endWriteTime = System.nanoTime();

                            // speed > 0 说明设置了限制速度
                            if (speed > 0) {
                                // 当前时间段内的期望时间 - 真实时间
                                long sleepDuration = (long) ((readLen * 1000.0 / speed) - ((startReadTime - endWriteTime) / 1000_000.0));

                                if (sleepDuration > 0) {
                                    Thread.sleep(sleepDuration);
                                }
                            }
                            // 计算瞬时速度
                            long timeDuration = System.currentTimeMillis() - perStartTime;
                            if (timeDuration >= 1000) {
                                long perSpeed = (long) ((sum - startReadPer) / timeDuration * 1000.0);
                                startReadPer = sum;
                                perStartTime = System.currentTimeMillis();
                                // 进度
                                int progress = (int) (sum * 1.0f / total * 100);
                                Log.d(TAG, "--total=" + total + ", progress=" + progress + ", perSpeed=" + perSpeed);
                                sendProgress(total, progress, perSpeed, callback);
                            }

                        }
                    }
                    // 写完刷新下
                    fos.flush();
                    sendComplete(file, callback);
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                    sendError(e, callback);
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                    sendError(e, callback);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                        sendError(e, callback);
                    }

                }

            }
        });
        return this;
    }

    private void sendComplete(final File file, final IDownloadCallback callback) {
        HiOk.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.complete(file);
                }
            }
        });
    }

    private void sendStart(final IDownloadCallback callback) {
        HiOk.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.start();
                }
            }
        });
    }

    private void sendError(final Exception e, final IDownloadCallback callback) {
        HiOk.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.error(e);
                }
            }
        });
    }

    private void sendProgress(final long total, final int progress, final long perSpeed, final IDownloadCallback callback) {
        HiOk.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.inProgress(total, progress, perSpeed);
                }
            }
        });
    }

    private void sendPause(final IDownloadCallback callback) {
        HiOk.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.pause();
                }
            }
        });
    }

    private void sendCancel(final IDownloadCallback callback) {
        HiOk.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.cancel();
                }
            }
        });
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        Log.d(TAG, "==cancelDownload");
        isCancel = true;
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() {
        Log.d(TAG, "==pauseDownload");
        isPause = true;
        sendPause(callback);
    }

    /**
     * 继续下载
     */
    public void continueDownload() {
        Log.d(TAG, "==continueDownload");
        isPause = false;
    }

    /**
     * 设置限速
     * @param speed 最高速度 单位Kb/s
     */
    public void setSpeed(long speed) {
        this.speed = speed;
    }

    /**
     * 得到下载内容的大小
     * @param downloadUrl 下载地址
     * @return
     */
    private long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                Log.d(TAG, "contentLength==" + contentLength);
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
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
