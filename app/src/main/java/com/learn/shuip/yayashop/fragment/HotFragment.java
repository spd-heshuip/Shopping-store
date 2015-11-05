package com.learn.shuip.yayashop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.adapter.BaseAdapter;
import com.learn.shuip.yayashop.adapter.HotWaresAdapter;
import com.learn.shuip.yayashop.bean.Page;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.decoration.DividerItemDecoration;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.http.SpotsCallback;
import com.learn.shuip.yayashop.system.Constant;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import androidUtils.ToastUtils;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment extends Fragment {

    private static final String TAG = HotFragment.class.getSimpleName();

    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private HotWaresAdapter mAdapater;
    private List<Ware> mDatas;

    private View rootView;

    private static int curPage = 1;
    private static int totalPage = 1;
    private static int pageSize = 10;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private OKHttpHelper mHttpClient = OKHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Log.d(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_hot, container, false);

        mRefreshLayout = (MaterialRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        initRecycleView();
        initRefreshLayout();
        requestData();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.d(TAG, "onDestroyView");
        mDatas = null;
        mAdapater = null;
    }

    private void initRecycleView() {
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                if (curPage <= totalPage) {
                    loadMore();
                } else {
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {
        curPage = 1;
        state = STATE_REFREH;
        requestData();
    }

    private void loadMore() {
        curPage++;
        state = STATE_MORE;
        requestData();
    }

    private void requestData() {
        String url = Constant.API.WARES + "?curPage=" + curPage + "&pageSize=" + pageSize;
        mHttpClient.get(url, new SpotsCallback<Page<Ware>>(getContext()) {
            @Override
            public void onSuccess(Response response, Page<Ware> warePage) {
                mDatas = warePage.getList();
                curPage = warePage.getCurrentPage();
                totalPage = warePage.getTotalPage();
                showData();
            }

            @Override
            public void onError(Response response, int code, IOException e) {

            }
        });
    }

    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                if (mAdapater == null) {
                    mAdapater = new HotWaresAdapter(getContext(), mDatas);
                    mRecycleView.setAdapter(mAdapater);
                    mAdapater.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ToastUtils.show(getContext(), "position:" + position + "click!", Toast.LENGTH_SHORT);
                        }
                    });
                } else {
                    mAdapater.clearData();
                    mAdapater.addData(mDatas);
                    mRecycleView.setAdapter(mAdapater);
                }
                break;
            case STATE_REFREH:
                mAdapater.clearData();
                mAdapater.addData(mDatas);
                mRecycleView.setAdapter(mAdapater);
                mRecycleView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mAdapater.addData(mAdapater.getDatas().size(), mDatas);
                mRecycleView.scrollToPosition(mAdapater.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

}
