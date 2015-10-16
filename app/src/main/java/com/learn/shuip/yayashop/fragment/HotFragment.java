package com.learn.shuip.yayashop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.learn.shuip.yayashop.R;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment extends Fragment{


    private SimpleDraweeView mDrawerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot,container,false);
        mDrawerView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
        return view ;

    }

}
