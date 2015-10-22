package com.learn.shuip.yayashop.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 15-10-14.
 */
public class OKHttpHelper {

    private static OKHttpHelper mInstance;


    private OkHttpClient mHttpClient;
    private Gson mGson;
    private Handler mHandler;

    enum HttpMethodType {
        GET,
        POST,
    }

    private OKHttpHelper() {
        mHttpClient = new OkHttpClient();
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);

        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OKHttpHelper getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpHelper.class) {
                if (mInstance == null) {
                    return new OKHttpHelper();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    public void post(String url, Map<String, String> params, BaseCallback callback) {
        Request request = buildPostRequest(url, params);
        doRequest(request, callback);
    }

    public void get(String url, BaseCallback callback) {
        Request request = buildGetRequest(url);
        doRequest(request, callback);
    }

    private void doRequest(final Request request, final BaseCallback baseCallback) {
        baseCallback.onBeforeRequest(request);
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callbackFailure(baseCallback, null, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                baseCallback.onResponse(response);
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (baseCallback.type == String.class) {
                        callbackSuccess(baseCallback, response, result);
                    } else {
                        try {
                            Object object = mGson.fromJson(result, baseCallback.type);
                            callbackSuccess(baseCallback, response, object);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    callbackError(baseCallback, response, null);
                }
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    private RequestBody buildFormDate(Map<String, String> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private Request buildPostRequest(String url, Map<String, String> params) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        return request;
    }

    private Request buildGetRequest(String url) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        return request;
    }

    private Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType) {

        Request.Builder builder = new Request.Builder().url(url);
        if (methodType == HttpMethodType.GET)
            builder.get();
        else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildFormDate(params);
            builder.post(body);
        }

        return builder.build();
    }


}
