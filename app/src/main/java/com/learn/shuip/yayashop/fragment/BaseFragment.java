package com.learn.shuip.yayashop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.shuip.yayashop.LoginActivity;
import com.learn.shuip.yayashop.bean.User;
import com.learn.shuip.yayashop.system.ShopApplication;

/**
 * 作者：Create By Administrator on 15-11-27 in com.learn.shuip.yayashop.fragment.
 * 邮箱：spd_heshuip@163.com;
 */
public class BaseFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void startActivity(Intent intent,boolean isNeedLogin){
        if (isNeedLogin){
            User user = ShopApplication.getInstance().getUser();
            if (user != null){
                super.startActivity(intent);
            }else {
                ShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else {
            super.startActivity(intent);
        }
    }
}
