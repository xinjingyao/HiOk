package com.yao.net.hiokdemo.network.entity;

public class RespnoseData {
    private int code;
    private String msg;
    private String result;

    public boolean isSuccess() {
        return code == 10000;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
