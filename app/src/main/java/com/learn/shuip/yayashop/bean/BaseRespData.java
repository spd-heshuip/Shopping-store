package com.learn.shuip.yayashop.bean;

import java.io.Serializable;

/**
 * 作者：Create By Administrator on 15-11-24 in com.learn.shuip.yayashop.bean.
 * 邮箱：spd_heshuip@163.com;
 */
public class BaseRespData implements Serializable{

    protected int status;

    protected String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
