package com.learn.shuip.yayashop.system;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Administrator on 15-10-15.
 */
public class ShopApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this,initProgressJPEGConfig(this,OKHttpHelper.getInstance().getHttpClient()));
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
}
