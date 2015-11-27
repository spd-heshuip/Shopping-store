package com.learn.shuip.yayashop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.learn.shuip.yayashop.MainActivity;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.adapter.CartAdapter;
import com.learn.shuip.yayashop.bean.ShoppingCart;
import com.learn.shuip.yayashop.decoration.DividerItemDecoration;
import com.learn.shuip.yayashop.util.CartProvider;
import com.learn.shuip.yayashop.widget.CustomToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;


/**
 * Created by Tracy on 15/9/22.
 */
public class CartFragment extends BaseFragment{

    private static final String TAG = CartFragment.class.getSimpleName();

    private static final int ACTION_EDIT = 1;
    private static final int ACTION_COMPLETE = 2;

    @ViewInject(R.id.recyclerview_cart)
    private RecyclerView mRecycleView;

    @ViewInject(R.id.check_all)
    private CheckBox mCheckAll;

    @ViewInject(R.id.text_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_settlement)
    private Button mButtonSettlement;

    @ViewInject(R.id.btn_delete)
    private Button mButtonDelete;

    private CartProvider mCartProvider;
    private CartAdapter mAdapter;
    private CustomToolbar mToolBar;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_cart,container,false);

            ViewUtils.inject(this, rootView);

            initRecycleView();

            mCartProvider = CartProvider.getInstance();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null){
            parent.removeView(rootView);
        }
        showData();
        hideEditControl();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mToolBar != null){
            changeToolbar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_delete)
    public void delCart(View view){
        mAdapter.delCartItem();
    }

    private void initRecycleView(){
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showData(){
        List<ShoppingCart> datas = mCartProvider.getAll();

        mAdapter = new CartAdapter(getContext(),datas,mCheckAll,mTextTotal);

        mRecycleView.setAdapter(mAdapter);
    }

    public void refreshData(){
        mAdapter.clearData();
        List<ShoppingCart> datas = mCartProvider.getAll();
        mAdapter.addData(datas);

        mRecycleView.setAdapter(mAdapter);
        mAdapter.showTotalPrice();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            MainActivity activity = (MainActivity) context;

            mToolBar = (CustomToolbar) activity.findViewById(R.id.customtoolbar);
        }
    }

    private void changeToolbar(){
        mToolBar.hideSearchView();
        mToolBar.showTitleView();
        mToolBar.setTitle(R.string.cart);
        mToolBar.getRightButton().setVisibility(View.VISIBLE);
        mToolBar.getRightButton().setText(R.string.edit);
        mToolBar.getRightButton().setTag(ACTION_EDIT);
        mToolBar.getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.toolbar_rightButton){
                    int tag = (int) v.getTag();
                    if (tag == ACTION_EDIT){
                        showDelControl();
                    }else if (tag == ACTION_COMPLETE){
                        hideEditControl();
                    }
                }
            }
        });

    }

    public void showDelControl(){
        mToolBar.getRightButton().setText(R.string.complete);
        mToolBar.getRightButton().setTag(ACTION_COMPLETE);

        mTextTotal.setText("");
        mButtonSettlement.setVisibility(View.GONE);

        mButtonDelete.setVisibility(View.VISIBLE);
        mAdapter.checkAll_None(false);
        mCheckAll.setChecked(false);
    }

    public void hideEditControl(){
        mToolBar.getRightButton().setText(R.string.edit);
        mToolBar.getRightButton().setTag(ACTION_EDIT);

        mTextTotal.setVisibility(View.VISIBLE);
        mButtonSettlement.setVisibility(View.VISIBLE);
        mButtonDelete.setVisibility(View.GONE);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckAll.setChecked(true);
    }
}
