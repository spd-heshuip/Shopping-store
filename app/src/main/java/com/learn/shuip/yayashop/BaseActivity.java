package com.learn.shuip.yayashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.learn.shuip.yayashop.bean.User;
import com.learn.shuip.yayashop.system.ShopApplication;

/**
 * 作者：Create By Administrator on 15-11-27 in com.learn.shuip.yayashop.
 * 邮箱：spd_heshuip@163.com;
 */
public class BaseActivity extends AppCompatActivity{

    protected void startActivity(Intent intent,boolean isNeedLogin){
        if (isNeedLogin){
            User user = ShopApplication.getInstance().getUser();
            if (user != null){
                super.startActivity(intent);
            }else {
                ShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else {
            super.startActivity(intent);
        }
    }
}
