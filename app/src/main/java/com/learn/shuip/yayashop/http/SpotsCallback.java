package com.learn.shuip.yayashop.http;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.learn.shuip.yayashop.LoginActivity;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.system.ShopApplication;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import androidUtils.ToastUtils;
import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 15-10-14.
 */
public abstract class SpotsCallback<T> extends BaseCallback<T>{

    private Context mContext;
    private SpotsDialog mDialog;

    public SpotsCallback(Context context) {
        this.mContext = context;
        initSpotDialog();
    }

    private void initSpotDialog(){
        mDialog = new SpotsDialog(mContext,"拼命加载中...");
    }

    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }

    public void showDialog(){
        mDialog.show();
    }

    public void hideDialog(){
        mDialog.dismiss();
    }

    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        hideDialog();
    }

    @Override
    public void onResponse(Response response) {
        hideDialog();
    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, R.string.token_error, Toast.LENGTH_SHORT);

        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        ShopApplication.getInstance().clearUser();
    }
}
