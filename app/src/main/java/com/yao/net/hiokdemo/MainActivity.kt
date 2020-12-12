package com.yao.net.hiokdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yao.net.hiokdemo.network.HiOk
import com.yao.net.hiokdemo.network.callback.OkCallback
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
    }
}