package com.learn.shuip.yayashop.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.adapter.BaseAdapter;
import com.learn.shuip.yayashop.adapter.CategoryListAdapter;
import com.learn.shuip.yayashop.adapter.WaresAdapter;
import com.learn.shuip.yayashop.bean.Banner;
import com.learn.shuip.yayashop.bean.Category;
import com.learn.shuip.yayashop.bean.CategoryList;
import com.learn.shuip.yayashop.bean.Page;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.decoration.DividerGridItemDecoration;
import com.learn.shuip.yayashop.decoration.DividerItemDecoration;
import com.learn.shuip.yayashop.http.BaseCallback;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.http.SpotsCallback;
import com.learn.shuip.yayashop.system.Constant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;


/**
 * Created by Ivan on 15/9/22.
 */
public class CategoryFragment extends BaseFragment {

    private static final String TAG = CategoryFragment.class.getSimpleName();
    private Gson mGson = new Gson();

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecycleView_category;

    @ViewInject(R.id.slider_catogory)
    private SliderLayout mSliderLayout;

    @ViewInject(R.id.refreshLayout_category)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recyclerview_commodity)
    private RecyclerView mRecycleView_commodity;

    private OKHttpHelper mHttpClient = OKHttpHelper.getInstance();

    private CategoryListAdapter mCategoryListAdapter;
    private WaresAdapter mWaresAdapter;

    private View rootView;

    private static int curPage = 1;
    private static int totalPage = 1;
    private static int pageSize = 10;
    private static long categoryId = 0;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this, rootView);

        initRecycleView();
        initRefreshLayout();

        requestCategoryData();
        requestBannerData();

        return rootView;
    }

    private void initRecycleView() {
        mRecycleView_commodity.setHasFixedSize(true);
        mRecycleView_commodity.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycleView_commodity.setItemAnimator(new DefaultItemAnimator());
        mRecycleView_commodity.addItemDecoration(new DividerGridItemDecoration(getContext()));

        mRecycleView_category.setHasFixedSize(true);
        mRecycleView_category.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView_category.setItemAnimator(new DefaultItemAnimator());
        mRecycleView_category.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void initRefreshLayout() {

        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (curPage <= totalPage)
                    loadMoreData();
                else {
//                    Toast.makeText()
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }


    private void refreshData() {
        curPage = 1;

        state = STATE_REFREH;
        requestWares(categoryId);
    }

    private void loadMoreData() {

        curPage = ++curPage;
        state = STATE_MORE;
        requestWares(categoryId);

    }

    private void requestCategoryData() {
        mHttpClient.get(Constant.API.CategoryList, new SpotsCallback<List<CategoryList>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<CategoryList> categories) {

                showCategoryData(categories);

                if (categories != null && categories.size() > 0)
                    categoryId = categories.get(0).getId();
                requestWares(categoryId);
            }

            @Override
            public void onError(Response response, int code, IOException e) {

            }
        });

    }

    private void showCategoryData(List<CategoryList> categories) {

        mCategoryListAdapter = new CategoryListAdapter(getContext(), categories);

        mCategoryListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = mCategoryListAdapter.getitem(position);

                categoryId = category.getId();
                curPage = 1;
                state = STATE_NORMAL;

                requestWares(categoryId);


            }
        });

        mRecycleView_category.setAdapter(mCategoryListAdapter);

    }


    private void requestBannerData() {
        String url = Constant.API.BANNER + "?type=1";

        mHttpClient.get(url, new SpotsCallback<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, IOException e) {

            }

        });

    }


    private void showSliderViews(List<Banner> banners) {
        if (banners != null) {

            for (Banner banner : banners) {
                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);

            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setDuration(3000);

    }


    private void requestWares(long categoryId) {

        String url = Constant.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + curPage + "&pageSize=" + pageSize;
        mHttpClient.get(url, new BaseCallback<Page<Ware>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Ware> waresPage) {
                curPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, IOException e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    private void showWaresData(List<Ware> wares) {
        switch (state) {
            case STATE_NORMAL:

                if (mWaresAdapter == null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);

                    mRecycleView_commodity.setAdapter(mWaresAdapter);

                } else {
                    mWaresAdapter.clearData();
                    mWaresAdapter.addData(wares);

                    mRecycleView_commodity.setAdapter(mWaresAdapter);
                }

                break;
            case STATE_REFREH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(wares);
                mRecycleView_commodity.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(), wares);
                mRecycleView_commodity.scrollToPosition(mWaresAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;

        }


    }
}



