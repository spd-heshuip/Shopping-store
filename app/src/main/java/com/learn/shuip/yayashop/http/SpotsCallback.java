package com.learn.shuip.yayashop.http;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

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


}
