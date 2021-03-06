package com.learn.shuip.yayashop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.WareListActivity;
import com.learn.shuip.yayashop.adapter.HomeCategoryAdapter;
import com.learn.shuip.yayashop.bean.Banner;
import com.learn.shuip.yayashop.bean.Campaign;
import com.learn.shuip.yayashop.bean.HomeCampaign;
import com.learn.shuip.yayashop.decoration.DividerItemDecoration;
import com.learn.shuip.yayashop.http.BaseCallback;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.http.SpotsCallback;
import com.learn.shuip.yayashop.system.Constant;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import androidUtils.ToastUtils;

/**
 * Created by Spd_heshuip on 15/9/25.
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";
    private SliderLayout mSliderShow;

    private RecyclerView mRecycleView;
    private HomeCategoryAdapter mAdapter;
    private OKHttpHelper mHttpHelper;

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHttpHelper = OKHttpHelper.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home, container, false);

            mSliderShow = (SliderLayout) rootView.findViewById(R.id.slider);
            initRecycleView(rootView);
            initSliderView();

            requestImages();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null){
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void requestImages() {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";

        mHttpHelper.get(url, new SpotsCallback<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                addSlider(banners);
            }

            @Override
            public void onError(Response response, int code, IOException e) {

            }
        });
    }

    private void initRecycleView(View view) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mHttpHelper.get(Constant.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtils.show(getActivity(), "无法连接到服务器，请检查网络设置", Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initRecycleViewData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, IOException e) {
                ToastUtils.show(getActivity(), "无法连接到服务器，请检查网络设置", Toast.LENGTH_SHORT);
            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    private void initRecycleViewData(List<HomeCampaign> datas) {
        mAdapter = new HomeCategoryAdapter(datas, getActivity());
        mAdapter.setOnItemClickListener(new HomeCategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {

                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Constant.CAMPAIGN_ID,campaign.getId());
                startActivity(intent);
            }
        });
        mRecycleView.setAdapter(mAdapter);

    }

    private void addSlider(List<Banner> banners) {
        if (banners != null) {
            for (Banner banner : banners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderShow.addSlider(textSliderView);
            }
        }
    }

    private void initSliderView(){
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
