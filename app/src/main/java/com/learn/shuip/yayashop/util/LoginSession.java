package com.learn.shuip.yayashop.util;

import android.content.Context;
import android.text.TextUtils;

import com.learn.shuip.yayashop.bean.User;
import com.learn.shuip.yayashop.system.Constant;

import androidUtils.JSONUtil;
import androidUtils.PreferencesUtils;

/**
 * 作者：Create By Administrator on 15-11-26 in com.learn.shuip.yayashop.util.
 * 邮箱：spd_heshuip@163.com;
 */
public class LoginSession {

    public static void putUserMsg(Context context,User user){
        String json = JSONUtil.toJson(user);
        PreferencesUtils.putString(context, Constant.USERMSG,json);
    }

    public static void putToken(Context context,String token){
        PreferencesUtils.putString(context,Constant.TOKEN,token);
    }

    public static User getUser(Context context){
        String json = PreferencesUtils.getString(context, Constant.USERMSG);
        if (!TextUtils.isEmpty(json)){
            User user = JSONUtil.fromJson(json,User.class);
            return user;
        }
        return null;
    }

    public static String getToken(Context context){
        String json = PreferencesUtils.getString(context,Constant.TOKEN);
        return json;
    }

    public static void clearUser(Context context){
        PreferencesUtils.putString(context,Constant.USERMSG,"");
    }

    public static void clearToken(Context context){
        PreferencesUtils.putString(context,Constant.TOKEN,"");
    }
}
