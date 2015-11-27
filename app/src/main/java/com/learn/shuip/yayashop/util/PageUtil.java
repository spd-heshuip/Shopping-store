package com.learn.shuip.yayashop.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.learn.shuip.yayashop.bean.Page;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.http.SpotsCallback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidUtils.ToastUtils;

/**
 * 作者：Create By Administrator on 15-11-9 in com.learn.shuip.yayashop.util.
 * 邮箱：spd_heshuip@163.com;
 */
public class PageUtil {

    private static final String TAG = PageUtil.class.getSimpleName();

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private OKHttpHelper mHttpClient;

    private static Builder builder;

    private PageUtil(){
        mHttpClient = OKHttpHelper.getInstance();
        initRefreshLayout();
    }

    public void request(){
        requestData();
    }

    private void initRefreshLayout() {
        builder.mRefreshLayout.setLoadMore(builder.isLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.isLoadMore);
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.curPage < builder.totalPage) {
                    loadMore();
                } else {
                    ToastUtils.show(builder.mContext, "没有更多的数据了！", Toast.LENGTH_SHORT);
                    builder.mRefreshLayout.finishRefreshLoadMore();
                    builder.mRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    private void refreshData() {
        builder.curPage = 1;
        state = STATE_REFREH;
        requestData();
    }

    private void loadMore() {
        builder.curPage = ++builder.curPage;
        state = STATE_MORE;
        requestData();
    }

    private void requestData() {
        String url = buildRequestUrl(builder.url);
        Log.i(TAG, url);
        mHttpClient.get(url, new RequestCallback(builder.mContext));
    }

    private String buildRequestUrl(String url){
        return buildUrl(url);
    }

    private String buildUrl(String url){
        HashMap<String,Object> params = builder.params;
        params.put("curPage",builder.curPage);
        params.put("pageSize",builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,Object> entry : params.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")){
            s = s.substring(0,s.length() - 1);
        }
        return url + "?" + s;
    }

    private <T> void  showData(List<T> datas,int totalPage,int totalCount) {
        if (datas == null || datas.size() <= 0){
            ToastUtils.show(builder.mContext,"加载不到数据！",Toast.LENGTH_SHORT);
            return;
        }
        switch (state) {
            case STATE_NORMAL:
                if (builder.mOnPageListener != null){
                    builder.mOnPageListener.load(datas,totalPage,totalCount);
                }
                break;
            case STATE_REFREH:
                builder.mRefreshLayout.finishRefresh();

                if (builder.mOnPageListener != null){
                    Log.i(TAG, "refresh");
                    builder.mOnPageListener.refresh(datas, totalPage, totalCount);
                }
                break;
            case STATE_MORE:
                builder.mRefreshLayout.finishRefreshLoadMore();

                if (builder.mOnPageListener != null){
                    Log.i(TAG, "loadMore");
                    builder.mOnPageListener.loadMore(datas, totalPage, totalCount);
                }
                break;
        }
    }

    public static Builder newBuilder(){
        builder = new Builder();
        return builder;
    }

    public void setParams(String key,Object value){
        builder.params.put(key, value);
    }

    public static class Builder{

        private String url;
        private HashMap<String,Object> params = new HashMap<>();
        private boolean isLoadMore;
        private  int curPage = 1;
        private  int totalPage = 1;
        private  int pageSize = 10;

        private OnPageListener mOnPageListener;

        private MaterialRefreshLayout mRefreshLayout;

        private Context mContext;

        private Type mType;

        public  Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout mRefreshLayout) {
            this.mRefreshLayout = mRefreshLayout;
            return builder;
        }

        public Builder setIsLoadMore(boolean isLoadMore) {
            this.isLoadMore = isLoadMore;
            return builder;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }

        public Builder setParams(String key,Object value) {
            this.params.put(key, value);
            return builder;
        }

        public Builder setOnPageListener(OnPageListener listener) {
            this.mOnPageListener = listener;
            return builder;
        }

        public PageUtil builder(Context context,Type type){
            this.mContext = context;
            this.mType = type;
            valid();
            return new PageUtil();
        }

        private void valid(){
            if (mContext == null){
                throw new RuntimeException("context can not be null");
            }
            if (this.url == null){
                throw new RuntimeException("url can not be null");
            }
            if (this.mRefreshLayout == null){
                throw new RuntimeException("mRefreshLayout can not be null");
            }
        }
    }

    private class RequestCallback<T> extends SpotsCallback<Page<T>>{

        public RequestCallback(Context context) {
            super(context);
            super.type = builder.mType;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            hideDialog();
            ToastUtils.show(builder.mContext,"加载数据失败！",Toast.LENGTH_SHORT);
            if (state == STATE_REFREH){
                builder.mRefreshLayout.finishRefresh();
            }else if (state == STATE_MORE){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            Log.i(TAG, "onSuccess");
            builder.curPage = page.getCurrentPage();
            builder.totalPage = page.getTotalPage();
            showData(page.getList(),page.getTotalPage(),page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, IOException e) {
            hideDialog();
            ToastUtils.show(builder.mContext,"加载数据失败！",Toast.LENGTH_SHORT);
            if (state == STATE_REFREH){
                builder.mRefreshLayout.finishRefresh();
            }else if (state == STATE_MORE){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }

    public interface OnPageListener<T>{
        void load(List<T> datas,int totalPage,int totalCount);
        void refresh(List<T> datas,int totalPage,int totalCount);
        void loadMore(List<T> datas,int totalPage,int totalCount);
    }

}
