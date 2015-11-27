package com.learn.shuip.yayashop;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.learn.shuip.yayashop.adapter.HotWaresAdapter;
import com.learn.shuip.yayashop.bean.Page;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.decoration.DividerItemDecoration;
import com.learn.shuip.yayashop.system.Constant;
import com.learn.shuip.yayashop.util.PageUtil;
import com.learn.shuip.yayashop.widget.CustomToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 作者：Create By Administrator on 15-11-10 in com.learn.shuip.yayashop.
 * 邮箱：spd_heshuip@163.com;
 */
public class WareListActivity extends BaseActivity implements PageUtil.OnPageListener<Ware> ,TabLayout.OnTabSelectedListener{

    @ViewInject(R.id.toolbar_warelist)
    private CustomToolbar mToolbar;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.txt_summary)
    private TextView mSummary;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recycler_view_warelist)
    private RecyclerView mRecycleView;

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;

    private PageUtil mPagerUtil;

    private HotWaresAdapter mAdapter;

    private int orderBy = 0;
    private long campaignId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warelist);
        ViewUtils.inject(this);

        campaignId = getIntent().getLongExtra(Constant.CAMPAIGN_ID,0);

        initToolbar();

        initTab();

        initRecyleView();

        getData();
    }

    private void initRecyleView(){
        mRecycleView.addItemDecoration(new DividerItemDecoration(WareListActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager((WareListActivity.this)));
    }

    private void getData(){
        mPagerUtil = PageUtil.newBuilder()
                .setUrl(Constant.API.WARES_CAMPAIN_LIST)
                .setIsLoadMore(true)
                .setPageSize(10)
                .setRefreshLayout(mRefreshLayout)
                .setParams("campaignId",campaignId)
                .setParams("orderBy",orderBy)
                .setOnPageListener(this)
                .builder(WareListActivity.this,new TypeToken<Page<Ware>>(){}.getType());

        mPagerUtil.request();
    }

    private void initToolbar(){
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);

        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = (int) v.getTag();
                if (action == ACTION_LIST) {
                    mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
                    mToolbar.getRightButton().setTag(ACTION_GIRD);

                    mAdapter.resetLayout(R.layout.template_hot_grid_wares);

                    mRecycleView.setLayoutManager(new GridLayoutManager(WareListActivity.this, 2));
                    mRecycleView.setAdapter(mAdapter);

                } else if (action == ACTION_GIRD) {
                    mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
                    mToolbar.getRightButton().setTag(ACTION_LIST);

                    mAdapter.resetLayout(R.layout.template_hot_cardview);

                    mRecycleView.setLayoutManager(new LinearLayoutManager(WareListActivity.this));

                    mRecycleView.setAdapter(mAdapter);
                }
            }
        });

    }

    private void initTab(){
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText(R.string.order_default);
        tab.setTag(TAG_DEFAULT);

        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText(R.string.order_price);
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText(R.string.order_sales);
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void load(List<Ware> datas, int totalPage, int totalCount) {
        mSummary.setText("共有" + totalCount + "件商品");
        if (mAdapter == null){
            mAdapter = new HotWaresAdapter(WareListActivity.this,datas);

            mRecycleView.setAdapter(mAdapter);
        }else {
            mAdapter.refreshData(datas);

            mRecycleView.setAdapter(mAdapter);
        }
    }

    @Override
    public void refresh(List<Ware> datas, int totalPage, int totalCount) {
        mAdapter.refreshData(datas);

        mRecycleView.setAdapter(mAdapter);

        mRecycleView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Ware> datas, int totalPage, int totalCount) {
        mAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        mPagerUtil.setParams("orderBy",orderBy);
        mPagerUtil.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
