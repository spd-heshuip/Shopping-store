package com.learn.shuip.yayashop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.system.Constant;
import com.learn.shuip.yayashop.util.CartProvider;
import com.learn.shuip.yayashop.widget.CustomToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import androidUtils.ToastUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

/**
 * 作者：Create By Administrator on 15-11-14 in com.learn.shuip.yayashop.
 * 邮箱：spd_heshuip@163.com;
 */
public class WareDetailActivity extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.customtoolbar_waredetail)
    private CustomToolbar mToolBar;

    @ViewInject(R.id.webview)
    private WebView mWebView;

    private Ware mWare;

    private WebAppInterface mWebAppInterface;

    private CartProvider mCartProvider;

    private SpotsDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waredetail);
        ViewUtils.inject(this);

        Serializable serializable = getIntent().getSerializableExtra(Constant.WARE);
        if (serializable == null)
            this.finish();

        mWare = (Ware) serializable;
        mCartProvider = CartProvider.getInstance();
        mDialog = new SpotsDialog(this,"loading.....");
        mDialog.show();

        initToolBar();
        initWebView();
    }

    private void initToolBar(){
        mToolBar.setNavigationIcon(R.drawable.back);
        mToolBar.setTitle(R.string.detail);
        mToolBar.setNavigationOnClickListener(this);

        mToolBar.setRightButtonIcon(R.drawable.share);
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOption();
            }
        });

    }

    private void shareOption(){
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(mWare.getImgUrl());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private void initWebView(){
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        mWebView.loadUrl(Constant.API.WARE_DETAIL);

        mWebAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mWebAppInterface,"appInterface");
        mWebView.setWebViewClient(new MyClient());
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }

    class MyClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            mWebAppInterface.showDetail();
        }
    }

    class WebAppInterface{

        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail("+mWare.getId()+")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id){
            mCartProvider.put(mWare);
            ToastUtils.show(mContext,"已添加到购物车",Toast.LENGTH_SHORT);
        }

        @JavascriptInterface
        public void addFavorites(long id){

        }
    }
}
