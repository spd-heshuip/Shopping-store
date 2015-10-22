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

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.adapter.HotCategoryAdapter;
import com.learn.shuip.yayashop.bean.Page;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.decoration.DividerItemDecortion;
import com.learn.shuip.yayashop.http.OKHttpHelper;
import com.learn.shuip.yayashop.http.SpotsCallback;
import com.learn.shuip.yayashop.system.Contants;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment extends Fragment{

    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private HotCategoryAdapter mAdapater;
    private List<Ware> mDatas;

    private static int curPage = 1;
    private static int totalPage = 1;
    private static int pageSize = 10;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state = STATE_NORMAL;

    private OKHttpHelper mHttpClient = OKHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot,container,false);
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview);
        initRefreshLayout();
        initData();
        return view ;
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
                if (curPage <= totalPage){
                    loadMore();
                }else {

                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData(){
        curPage = 1;
        state = STATE_REFREH;
        initData();
    }

    private void loadMore(){
        curPage++;
        state = STATE_MORE;
        initData();
    }

    private void initData(){
        String url = Contants.API.WARES + "?curPage=" + curPage + "&pageSize=" + pageSize;
        mHttpClient.get(url, new SpotsCallback<Page<Ware>>(getActivity()) {
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

    private void showData(){
        switch (state){
            case STATE_NORMAL:
                mAdapater = new HotCategoryAdapter(mDatas);
                mRecycleView.setAdapter(mAdapater);

                mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecycleView.addItemDecoration(new DividerItemDecortion());
                mRecycleView.setItemAnimator(new DefaultItemAnimator());
                break;
            case  STATE_REFREH:
                mAdapater.clearData();
                mAdapater.addData(mDatas);
                mRecycleView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mAdapater.addData(mAdapater.getDatas().size(),mDatas);
                mRecycleView.scrollToPosition(mAdapater.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

}
