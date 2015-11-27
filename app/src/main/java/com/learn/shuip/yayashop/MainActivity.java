package com.learn.shuip.yayashop;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.learn.shuip.yayashop.bean.Tab;
import com.learn.shuip.yayashop.fragment.CartFragment;
import com.learn.shuip.yayashop.fragment.CategoryFragment;
import com.learn.shuip.yayashop.fragment.HomeFragment;
import com.learn.shuip.yayashop.fragment.HotFragment;
import com.learn.shuip.yayashop.fragment.MineFragment;
import com.learn.shuip.yayashop.util.CartProvider;
import com.learn.shuip.yayashop.widget.CustomToolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,FragmentTabHost.OnTabChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int numbet_cart = 0;

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;

    private CustomToolbar mCustomToolbar;
    private ViewPager mViewPager;
    private ViewStub mViewStub;
    private TextView mTextBadge;

    private CartFragment mCartFragment;

    private List<Tab> mTabs = new ArrayList<Tab>(5);

    private CartProvider mCartProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCartProvider = CartProvider.getInstance();

        numbet_cart = mCartProvider.getCartNumber();

        initToolBar();
        initTab();
        initViewPager();
    }

    private void initToolBar(){
        mCustomToolbar = (CustomToolbar) findViewById(R.id.customtoolbar);
    }

    private void initTab(){
        Tab tab_home = new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot,R.drawable.selector_icon_hot,HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory,R.drawable.selector_icon_category,CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            if (getString(tab.getTitle()).equals("购物车"))
                tabSpec.setIndicator(buildIndicatorWithBadge(tab));
            else
                tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(),null);
        }

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
    }

    private void initViewPager(){
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(),mTabs));
        mViewPager.addOnPageChangeListener(this);
    }

    private View buildIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicartor);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }

    private View buildIndicatorWithBadge(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator_with_badge,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicartor);
        mTextBadge = (TextView) view.findViewById(R.id.tabBadge);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        if (numbet_cart > 0){
            mTextBadge.setText(numbet_cart + "");
        }

        return view;
    }

    private void AnimationTextView(TextView textView){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(textView,"scaleX",1f,1.5f,1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(textView,"scaleY",1f,1.5f,1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(1500);
        animatorSet.start();
    }

    public void setCartBadge(int number){
        if (mTextBadge != null){
            mTextBadge.setText(number + "");
            AnimationTextView(mTextBadge);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.setCurrentItem(position);
        mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);

        if (tabId == getString(R.string.cart)){
            if (mCartFragment == null){
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tabId);
                if (fragment != null){
                    mCartFragment = (CartFragment) fragment;
                    mCartFragment.refreshData();
                }
            }else {
                mCustomToolbar.hideSearchView();
                mCustomToolbar.setTitle(R.string.cart);
                mCustomToolbar.getRightButton().setVisibility(View.VISIBLE);
//                mCartFragment.refreshData();
            }
        }else {
            mCustomToolbar.showSearchView();
            mCustomToolbar.hideTitleView();
            mCustomToolbar.getRightButton().setVisibility(View.GONE);
        }

    }

    static class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Tab> mTabs;

        public TabFragmentPagerAdapter(FragmentManager fm,List<Tab> mTabs) {
            super(fm);
            this.mTabs = mTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            try {
                fragment = (Fragment) mTabs.get(position).getFragment().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartBadge(mCartProvider.getCartNumber());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
