package com.yao.net.hiokdemo.network.util;

/**
 * 异常工具
 */
public class Exceptions {

    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
