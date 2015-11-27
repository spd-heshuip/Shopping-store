package com.learn.shuip.yayashop.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.learn.shuip.yayashop.system.ShopApplication;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 15-10-14.
 */
public class OKHttpHelper {

    private static final String TAG = OKHttpHelper.class.getSimpleName();

    private static final int TOKEN_ERROR = 402;
    private static final int TOKEN_MISSIONG = 401;
    private static final int TOKEN_UPADATE = 403;


    private static OKHttpHelper mInstance;

    private OkHttpClient mHttpClient;

    private Gson mGson;

    private File mCacheFile;

    private int mCacheSize = 10 * 1024 * 1024;

    private Handler mHandler;

    enum HttpMethodType {
        GET,
        POST,
    }

    private OKHttpHelper() {
        mCacheFile = ShopApplication.getInstance().getCacheFile();

        mHttpClient = new OkHttpClient();
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);

        if (mCacheFile != null){
            mHttpClient.setCache(new Cache(mCacheFile.getAbsoluteFile(), mCacheSize));
        }

        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static final OKHttpHelper getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpHelper.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpHelper();
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

    public void get(String url, BaseCallback callback,Map<String,String> params) {
        Request request = buildGetRequest(url, params);
        doRequest(request, callback);
    }

    public void get(String url, BaseCallback callback) {
        get(url, callback, null);
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
//                    Log.d(TAG,result);
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
                } else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_MISSIONG || response.code() == TOKEN_UPADATE) {
                    callbackTokenError(baseCallback,response);
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

    private void callbackTokenError(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }



    private Request buildPostRequest(String url, Map<String, String> params) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        return request;
    }

    private Request buildGetRequest(String url,Map<String,String> params) {
        Request request = buildRequest(url, params, HttpMethodType.GET);
        return request;
    }

    private Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType) {

        Request.Builder builder = new Request.Builder().url(url);
        if (methodType == HttpMethodType.GET){
            url = buildParams(url,params);
            builder.url(url);
            builder.get();
        }
        else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildFormDate(params);
            builder.post(body);
        }

        return builder.cacheControl(new CacheControl.Builder().maxStale(30,TimeUnit.MINUTES).build()).build();
    }

    private String buildParams(String url ,Map<String,String> params){
        if (params == null){
            params = new HashMap<>(1);
        }

        String token =ShopApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token)){
            params.put("token",token);
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,String> entry : params.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")){
            s = s.substring(0,s.length() - 1);
        }
        if (url.indexOf("?") > 0){
            url = url + "&" + s;
        }else {
            url = url + "?" + s;;
        }
        return url;
    }
    private RequestBody buildFormDate(Map<String, String> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            String token = ShopApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
                builder.add("token",token);
        }
        return builder.build();
    }
}
