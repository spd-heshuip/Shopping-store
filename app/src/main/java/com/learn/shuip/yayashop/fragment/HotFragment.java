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

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.WareDetailActivity;
import com.learn.shuip.yayashop.adapter.BaseAdapter;
import com.learn.shuip.yayashop.adapter.HotWaresAdapter;
import com.learn.shuip.yayashop.bean.Page;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.decoration.DividerItemDecoration;
import com.learn.shuip.yayashop.system.Constant;
import com.learn.shuip.yayashop.util.PageUtil;

import java.util.List;


/**
 * Created by Ivan on 15/9/22.
 */
public class HotFragment extends BaseFragment {

    private static final String TAG = HotFragment.class.getSimpleName();

    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private HotWaresAdapter mAdapater;
    private PageUtil mPageUtil;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_hot, container, false);

        mRefreshLayout = (MaterialRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        initRecycleView();

        init();

        return rootView;
    }

    private void init(){
        mPageUtil = PageUtil.newBuilder().setUrl(Constant.API.WARES)
                .setIsLoadMore(true)
                .setPageSize(10)
                .setRefreshLayout(mRefreshLayout)
                .setOnPageListener(new PageUtil.OnPageListener() {
                    @Override
                    public void load(List datas, int totalPage, int totalCount) {
                        if (mAdapater == null) {
                            mAdapater = new HotWaresAdapter(getActivity(), datas);
                            mRecycleView.setAdapter(mAdapater);
                            mAdapater.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Ware ware = mAdapater.getitem(position);
                                    Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                                    intent.putExtra(Constant.WARE,ware);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            mAdapater.clearData();
                            mAdapater.addData(datas);
                            mRecycleView.setAdapter(mAdapater);
                        }
                    }

                    @Override
                    public void refresh(List datas, int totalPage, int totalCount) {
                        mAdapater.clearData();
                        mAdapater.addData(datas);
                        mRecycleView.setAdapter(mAdapater);
                        mRecycleView.scrollToPosition(0);
                    }

                    @Override
                    public void loadMore(List datas, int totalPage, int totalCount) {
                        mAdapater.addData(mAdapater.getDatas().size(), datas);
                        mRecycleView.scrollToPosition(mAdapater.getDatas().size());
                    }
                }).builder(getContext(), new TypeToken<Page<Ware>>() {
                }.getType());

        mPageUtil.request();
    }

    private void initRecycleView() {
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapater = null;
        mRefreshLayout = null;
        mPageUtil = null;
    }
}
