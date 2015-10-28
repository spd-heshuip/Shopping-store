package com.learn.shuip.yayashop.bean;

/**
 * Created by Administrator on 15-10-13.
 */
public class Category extends BaseBean{

    protected String name;

    public Category(){

    }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name ,long id){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
