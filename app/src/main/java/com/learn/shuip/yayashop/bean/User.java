package com.learn.shuip.yayashop.bean;

import java.io.Serializable;

/**
 * 作者：Create By Administrator on 15-11-24 in com.learn.shuip.yayashop.bean.
 * 邮箱：spd_heshuip@163.com;
 */
public class User implements Serializable{

    private Long id;

    private String mobi;

    private String email;

    private String username;

    private String logo_url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobi() {
        return mobi;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }
}
