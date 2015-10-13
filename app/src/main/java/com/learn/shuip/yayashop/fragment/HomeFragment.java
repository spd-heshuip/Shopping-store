package com.learn.shuip.yayashop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.adapter.HomeCategoryAdapter;
import com.learn.shuip.yayashop.bean.Banner;
import com.learn.shuip.yayashop.bean.HomeCategory;
import com.learn.shuip.yayashop.decoration.DividerDecoration;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Spd_heshuip on 15/9/25.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private Gson mGson = new Gson();
    private SliderLayout mSliderShow;

    private RecyclerView mRecycleView;
    private List<HomeCategory> mCategorys;
    private HomeCategoryAdapter mAdapter;
    private List<Banner> mBanners;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderShow = (SliderLayout) view.findViewById(R.id.slider);

        requestImages();
        initSliderView();
        initRecycleView(view);
        return view;
    }

    private void requestImages(){
        String url ="http://112.124.22.238:8081/course_api/banner/query";

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder()
                .add("type", "1")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                if(response.isSuccessful()){
                    String json =  response.body().string();
                    Log.i(TAG,json);
                    Type type = new TypeToken<List<Banner>>(){}.getType();
                    mBanners = mGson.fromJson(json,type);
                }
            }
        });

    }

    private void initRecycleView(View view) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mCategorys = new ArrayList<HomeCategory>();

        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
        mCategorys.add(category);

        category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
        mCategorys.add(category);
        category = new HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
        mCategorys.add(category);

        category = new HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
        mCategorys.add(category);

        category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
        mCategorys.add(category);

        mAdapter = new HomeCategoryAdapter(mCategorys);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new DividerDecoration());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void initSliderView() {
        if(mBanners != null){
            for (Banner banner : mBanners){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderShow.addSlider(textSliderView);
            }
        }
        mSliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderShow.setCustomAnimation(new DescriptionAnimation());
        mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderShow.setDuration(3000);

        mSliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
//                Log.i(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {
//                Log.i(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
//                Log.i(TAG,"onPageScrollStateChanged");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderShow.stopAutoCycle();
    }
}
