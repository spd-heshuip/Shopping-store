package com.learn.shuip.yayashop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.learn.shuip.yayashop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;

    private List<Tab> mTabs = new ArrayList<Tab>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
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
