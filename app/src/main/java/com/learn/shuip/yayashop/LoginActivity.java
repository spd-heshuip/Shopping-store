package com.learn.shuip.yayashop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.learn.shuip.yayashop.bean.LoginRespData;
import com.learn.shuip.yayashop.bean.User;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.http.SpotsCallback;
import com.learn.shuip.yayashop.system.Constant;
import com.learn.shuip.yayashop.system.ShopApplication;
import com.learn.shuip.yayashop.widget.CustomEditText;
import com.learn.shuip.yayashop.widget.CustomToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidUtils.ToastUtils;

/**
 * 作者：Create By Administrator on 15-11-24 in com.learn.shuip.yayashop.
 * 邮箱：spd_heshuip@163.com;
 */
public class LoginActivity extends BaseActivity{

    @ViewInject(R.id.toolbar_login)
    private CustomToolbar mToolbar;

    @ViewInject(R.id.phone_edit)
    private CustomEditText mPhoneNumber;

    @ViewInject(R.id.passwd_edit)
    private CustomEditText mPasswd;

    @ViewInject(R.id.login_button)
    private Button mLogin;

    private OKHttpHelper mHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        mHttpClient = OKHttpHelper.getInstance();

        initToolbar();
    }

    private void initToolbar(){
        mToolbar.setTitle(R.string.login);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.login_button)
    public void doLogin(View view){
        String phone = mPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtils.show(LoginActivity.this,"请输入手机号码", Toast.LENGTH_SHORT);
            return;
        }else {
            if (phone.length() > 11){
                ToastUtils.show(LoginActivity.this,"请输入正确的手机号码！", Toast.LENGTH_SHORT);
            }
        }
        String pwd = mPasswd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)){
            ToastUtils.show(LoginActivity.this,"请输入密码", Toast.LENGTH_SHORT);
            return;
        }

        Map<String,String> params = new HashMap<>(2);
        params.put(Constant.PHONE,phone);
        params.put(Constant.PASSWD,pwd);

        mHttpClient.post(Constant.API.LOGIN, params, new SpotsCallback<LoginRespData<User>>(LoginActivity.this) {

            @Override
            public void onSuccess(Response response, LoginRespData<User> userLoginRespData) {
                ShopApplication application = ShopApplication.getInstance();
                application.putUser(userLoginRespData.getData(), userLoginRespData.getToken());

                if (application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else {
                    startActivity(application.getIntent());
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, IOException e) {

            }
        });


    }
}
