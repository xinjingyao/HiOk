package com.yao.net.hiokdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yao.net.hiokdemo.network.HiOk
import com.yao.net.hiokdemo.network.callback.OkCallback
import com.yao.net.hiokdemo.network.request.PostStringRequest.MEDIA_TYPE_JSON
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HiOk.getInstance()
            .get()
            .url("")
            .params(null)
            .build()
            .execute(object: OkCallback<String>() {
                override fun onBefore() {

                }

                override fun onSuccess(response: Any?) {

                }

                override fun onError(e: Exception?) {

                }

                override fun onAfter() {

                }
            })

        HiOk.getInstance()
            .post()
            .url("")
            .addParam("name", "yao")
            .build()
            .execute(object : OkCallback<String>() {
                override fun onBefore() {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(response: Any?) {
                    TODO("Not yet implemented")
                }

                override fun onError(e: Exception?) {
                    TODO("Not yet implemented")
                }

                override fun onAfter() {
                    TODO("Not yet implemented")
                }

            })

        HiOk.getInstance().postString()
            .url("")
            .content("xxx")
            .mediaType(MEDIA_TYPE_JSON)
            .build()
            .execute(object : OkCallback<String>() {
                override fun onBefore() {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(response: Any?) {
                    TODO("Not yet implemented")
                }

                override fun onError(e: Exception?) {
                    TODO("Not yet implemented")
                }

                override fun onAfter() {
                    TODO("Not yet implemented")
                }
            })
    }
}