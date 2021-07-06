package com.webbrowser.bigwhite.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class httpUtils {
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoYmA" +
            "JFNJlmpMgK9hFRsMOFH9osOybRQ8fkUgnRVk3a9GVKCXQL6WRY+rQDlmv7s5irhzjHeVZ2q" +
            "y+iaRETMDoGvWIm7s2jk+LB2+4m30hBDrnOFcjzWeZjuRSqKEPeaInyN8S" +
            "bPiLSVmcjG5HkrtHLJHcqjl2ei9t8hjTgj4+2wIDAQAB";

    //登录
    public static void loginWithOkHttp(String address, String account, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", account)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //验证
    public static void verifyWithOkHttp(String address, String email, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();

        client.newCall(request).enqueue(callback);
    }

    //注册
    public static void registerWithOkHttp(String address, String email, String verify, String account, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("verifyCode", verify)
                .add("username", account)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /*加密方法*/
    public static String encrypt(String code) {
        RSA rsa = new RSA(null, PUBLIC_KEY);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(code, CharsetUtil.CHARSET_UTF_8),
                KeyType.PublicKey);
        return Base64.encode(encrypt);
    }

    /*上传历史记录*/
    public static void putHistory(String head, String address, String title, String url, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("title", title)
                .add("url", url)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Authorization", head)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void putBookMark(String head, String address, String tag, String title, String url, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("title", title)
                .add("tag", tag)
                .add("url", url)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Authorization", head)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getHistoryOrCollectionFromBack(String address,String head, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization",head)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteHis(String address,String head,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Authorization", head)
                .delete()
                .build();
        client.newCall(request).enqueue(callback);
    }
}