package com.learn.shuip.yayashop.system;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.learn.shuip.yayashop.bean.User;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.util.LoginSession;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * Created by Administrator on 15-10-15.
 */
public class ShopApplication extends Application{

    private static Context mContext;
    private User mUser;
    private String mToken;
    private Intent mIntent;
    private File mCacheFile;

    private static ShopApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mCacheFile = getExternalCacheDir();
        mInstance = this;
        mContext = getApplicationContext();
        initUser();

        Fresco.initialize(this,initProgressJPEGConfig(this,OKHttpHelper.getInstance().getHttpClient()));
    }

    public static ShopApplication getInstance(){
        return mInstance;
    }

    private ImagePipelineConfig initProgressJPEGConfig(Context context,OkHttpClient okHttpClient){
        ProgressiveJpegConfig jpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            @Override
            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient)
                .setProgressiveJpegConfig(jpegConfig)
                .build();
        return config;
    }

    private void initUser(){
        mUser = LoginSession.getUser(this);
        mToken = LoginSession.getToken(this);
    }

    public User getUser(){
        return mUser;
    }

    public String getToken(){
        return mToken;
    }

    public void putUser(User user,String token){
        this.mUser = user;
        LoginSession.putUserMsg(this,user);
        LoginSession.putToken(this,token);
    }

    public void clearUser(){
        this.mUser = null;
        LoginSession.clearUser(this);
        LoginSession.clearToken(this);
    }

    public void putIntent(Intent intent){
        this.mIntent = intent;
    }

    public Intent getIntent(){
        return this.mIntent;
    }

    public void jumptoTargetActivity(Context context){
        context.startActivity(mIntent);
        this.mIntent = null;
    }

    public File getCacheFile(){
        return this.mCacheFile;
    }

    public  static Context getContext(){
        return mContext;
    }
}
