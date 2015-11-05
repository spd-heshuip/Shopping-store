package com.learn.shuip.yayashop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 15-10-29.
 */
public class ShoppingCart extends Ware implements Serializable{

    private int count;
    private boolean isChecked = true;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
