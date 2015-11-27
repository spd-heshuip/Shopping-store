package com.learn.shuip.yayashop.bean;

/**
 * 作者：Create By Administrator on 15-11-24 in com.learn.shuip.yayashop.bean.
 * 邮箱：spd_heshuip@163.com;
 */
public class LoginRespData<T>  extends BaseRespData {

    private String token;

    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
