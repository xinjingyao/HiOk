package com.yao.net.hiokdemo.network.callback;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * 字符串回调解析
 */
public abstract class StringCallback extends AbsCallback<String> {

    @Override
    public String parseResponse(Response response) throws Exception {

        if (response == null || response.body() == null) {
            return "";
        } else {
            String msg = "";
            int code = 0;
            String result = "";

            String body = response.body().string();
            if (TextUtils.isEmpty(body)) {
                return msg;
            }
            JSONObject object = new JSONObject(body);
            if (object.has("code")) {
                code = object.getInt("code");
            }
            if (object.has("msg")) {
                msg = object.getString("msg");
            }
            if (object.has("result")) {
                result = object.getString("result");
            }

            Log.d("==parseResponse=", "code=" + code + ", message=" + msg + ", result=" + result);
            if (code == 10000) {
                return result;
            } else {
                return msg;
            }
        }
    }
}
