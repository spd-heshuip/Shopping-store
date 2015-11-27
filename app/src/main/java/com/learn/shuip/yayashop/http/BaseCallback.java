package com.learn.shuip.yayashop.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 15-10-14.
 */
public abstract class BaseCallback<T> {

    public Type type;

    static Type getSuperclassTypeParameter(Class<?> subClass){
        Type superclass = subClass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback(){
        type = getSuperclassTypeParameter(getClass());
    }

    public abstract void onBeforeRequest(Request request);
    public abstract void onFailure(Request request,IOException e);

    /*
    * 请求成功时调用
    * @params:Response
    * */
    public abstract void onResponse(Response response);

    /**
     *
     * 状态码大于200，小于300 时调用此方法
     * @param response
     * @param t
     * @throws IOException
     */
    public abstract void onSuccess(Response response,T t);

    /**
     * 状态码400，404，403，500等时调用此方法
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response,int code,IOException e);


    /**
     * Token状态码401，402，403等时调用此方法
     * @param response
     * @param code
     */
    public abstract void onTokenError(Response response,int code);
}
