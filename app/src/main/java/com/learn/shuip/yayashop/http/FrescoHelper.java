package com.learn.shuip.yayashop.http;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by Administrator on 15-10-16.
 */
public class FrescoHelper {

    public static void loadImage(String url,SimpleDraweeView draweeView){
        Uri uri = Uri.parse(url);
        draweeView.setImageURI(uri);
    }

    public static void loadImage(String url,SimpleDraweeView draweeView,ControllerListener controllerListener){
        if (draweeView != null){
            Uri uri = Uri.parse(url);
            draweeView.setController(buildControllser(uri,draweeView,controllerListener));
        }

    }

    public static void loadImage(String url,SimpleDraweeView draweeView,ControllerListener controllerListener
            ,Postprocessor postprocessor){
        if (draweeView != null){
            Uri uri = Uri.parse(url);
            draweeView.setController(buildControllser(draweeView,buildRequest(uri,postprocessor,null,true,false),controllerListener));
        }
    }

    public static void loadImage(String url,SimpleDraweeView draweeView,ControllerListener controllerListener,
                                  Postprocessor postprocessor,ResizeOptions resizeOptions,
                                  boolean isAutoRotateEnabled,boolean isProgressiveRenderingEnabled){
        if (draweeView != null) {
            Uri uri = Uri.parse(url);
            ImageRequest request = buildRequest(uri, postprocessor,resizeOptions,isAutoRotateEnabled,isProgressiveRenderingEnabled);
            PipelineDraweeController controller = (PipelineDraweeController) buildControllser(draweeView,request,controllerListener);
            draweeView.setController(controller);
        }
    }

    private static DraweeController buildControllser(Uri uri,SimpleDraweeView draweeView,ControllerListener controllerListener){
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setOldController(draweeView.getController())
                .setControllerListener(controllerListener)
                .build();
        return controller;
    }

    private static DraweeController buildControllser(SimpleDraweeView draweeView,ImageRequest imageRequest,ControllerListener controllerListener){
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setTapToRetryEnabled(true)
                .setOldController(draweeView.getController())
                .setControllerListener(controllerListener)
                .build();
        return controller;
    }

    private static ImageRequest buildRequest(Uri uri,Postprocessor postprocessor,ResizeOptions resizeOptions,boolean isAutoRotateEnabled,
                                             boolean isProgressiveRenderingEnabled){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(postprocessor)
                .setResizeOptions(resizeOptions)
                .setAutoRotateEnabled(isAutoRotateEnabled)
                .setProgressiveRenderingEnabled(isProgressiveRenderingEnabled)
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();
        return request;
    }
}
