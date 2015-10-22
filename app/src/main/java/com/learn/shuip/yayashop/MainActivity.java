package com.learn.shuip.yayashop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.learn.shuip.yayashop.widget.CustomToolbar;
import com.learn.shuip.yayashop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,FragmentTabHost.OnTabChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private CustomToolbar mCustomToolbar;
    private ViewPager mViewPager;

    private List<Tab> mTabs = new ArrayList<Tab>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Log.d(TAG,"onTabchanged");
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
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
