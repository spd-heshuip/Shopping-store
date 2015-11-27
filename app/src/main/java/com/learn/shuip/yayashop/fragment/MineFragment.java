package com.learn.shuip.yayashop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.learn.shuip.yayashop.LoginActivity;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.User;
import com.learn.shuip.yayashop.system.Constant;
import com.learn.shuip.yayashop.system.ShopApplication;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Ivan on 15/9/22.
 */
public class MineFragment extends BaseFragment{

    @ViewInject(R.id.img_head)
    private CircleImageView mImgHead;

    @ViewInject(R.id.txt_username)
    private TextView mUserName;

    @ViewInject(R.id.logout_btn)
    private Button mLogOut;

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mine,container,false);

        ViewUtils.inject(this,view);

        mUser = ShopApplication.getInstance().getUser();

        showUser(mUser);

        return view;
    }

    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void doLogin(View view){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Constant.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user = ShopApplication.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user){
        if (user != null){
            Picasso.with(getActivity()).load(user.getLogo_url()).into(mImgHead);
            mUserName.setText(user.getUsername());
            mLogOut.setVisibility(View.VISIBLE);
        }else {
//            mUserName.setText(R.string.login);
        }
    }
}
