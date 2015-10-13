package com.learn.shuip.yayashop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 15-10-13.
 */
public class BaseBean implements Serializable {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
