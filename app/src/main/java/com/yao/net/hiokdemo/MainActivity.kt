package com.yao.net.hiokdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.yao.net.hiokdemo.network.HiOk
import com.yao.net.hiokdemo.network.callback.AbsCallback
import com.yao.net.hiokdemo.network.callback.GenericCallback
import com.yao.net.hiokdemo.network.callback.IDownloadCallback
import com.yao.net.hiokdemo.network.callback.StringCallback
import com.yao.net.hiokdemo.network.request.DownloadRequest
import com.yao.net.hiokdemo.network.request.PostStringRequest.MEDIA_TYPE_JSON
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var manager: DownloadRequest? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start.setOnClickListener { startDownload() }
        btn_pause.setOnClickListener { pauseDownload() }
        btn_continue.setOnClickListener { continueDownload() }
        btn_cancel.setOnClickListener { cancelDownload() }
        btn_get.setOnClickListener { requestGet() }
        btn_post.setOnClickListener { requestPost() }
        btn_post_string.setOnClickListener { requestPostString() }

    }

    private fun requestPostString() {
        HiOk.getInstance().postString()
            .url("http://microgame-test.haimawan.com/microCloud/game/into/microCloud/loginForSdk")
            .addHeader("authInfo", "De6ClA1hCGCacrlwyvmpAP9rr0Mfn5u73v4yRIJ9/kGmbrGjhhNouHmH0Sq8xv2l7A6KQAK9kuFhOw7DXz6bPidPJuDZ6NxwftoGy1ts8B+bLvntndkd2z7u5pcUpLcbIKQqcXS3zss7+zYNjHEa395iMnxk65SZ46YDAvQ3H6ZaGXfvgfdV1BmYx3qdqZHXqtaGy9UW1OM+dt/0EzFiivcwsfD+6rmLd1hy/O9qgn8=")
            .content("NWSkixg2SeWGxiZ95aVydxw1+wPYs90LGO2Agw6BLUGxzpUwDQgBBcmhhsP1+nFifHHwcgwX5bWaciC8WbFDteB9nBkLGA29R03f+sm5eLicnMhNugYcAacUc3+mU8lVq2+E/bppR26I66s/CLD4j7SQAHunhP16XRJIsziNV/g=")
            .mediaType(MEDIA_TYPE_JSON)
            .build()
            .execute(object : StringCallback() {
                override fun onBefore() {
                    println("onBefore")
                }

                override fun onError(e: Exception?) {
                    println("onError::$e")
                }

                override fun onAfter() {
                    println("onAfter")
                }

                override fun onSuccess(response: String?) {
                    println("onSuccess==$response")
                }
            })
    }

    private fun requestPost() {
        HiOk.getInstance()
            .post()
            .url("http://microgame-test.haimawan.com/microCloud/game/into")
            .addParam("name", "yao")
            .build()
            .execute(object : GenericCallback<String>() {
                override fun onBefore() {
                    println("onBefore")
                }

                override fun onError(e: Exception?) {
                    println("onError::$e")
                    tv_response.text = "onError= $e"
                }

                override fun onAfter() {
                    println("onAfter")
                }

                override fun onSuccess(response: String?) {
                    println("onSuccess==$response")
                    tv_response.text = "onSuccess= $response"
                }

            })
    }

    private fun requestGet() {
        HiOk.getInstance()
            .get()
            .url("http://microgame-test.haimawan.com/microCloud/game/into")
            .params(null)
            .build()
            .execute(object: StringCallback() {
                override fun onSuccess(response: String?) {
                    println("onSuccess==$response")
                    tv_response.text = "onSuccess= $response"
                }

                override fun onError(e: Exception?) {
                    println("onError::$e")
                    tv_response.text = "onError= $e"
                }
            })
    }

    private fun continueDownload() {
        manager?.continueDownload()
    }

    private fun cancelDownload() {
        manager?.cancelDownload()
    }

    private fun pauseDownload() {
        manager?.pauseDownload()
    }

    private fun startDownload() {
        manager = HiOk.getInstance().download()
            .url("http://fzdldownload.zlongame.com/FZDL/Clientdown/pd_fzdl_moblie.apk")
            .tag("download")
            .filePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaaa")
            .fileName("123.apk")
            .resume(true)
            .build()
            .execute(object : IDownloadCallback {
                override fun start() {

                }

                override fun inProgress(total: Long, progress: Int) {
                    println("inProgress==total$total===progress$progress")
                    tv_download_info.text = "total=$total, progress=$progress %"
                }

                override fun pause() {

                }

                override fun cancel() {

                }

                override fun complete(file: File?) {

                }

                override fun error(e: Exception?) {

                }
            })
    }
}