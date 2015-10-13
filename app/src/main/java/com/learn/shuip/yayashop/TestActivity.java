package com.learn.shuip.yayashop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.learn.shuip.yayashop.widget.CustomToolbar;

/**
 * Created by Administrator on 15-10-12.
 */
public class TestActivity extends AppCompatActivity{

    private CustomToolbar mCustomToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mCustomToolbar = (CustomToolbar) findViewById(R.id.customtoolbar);
    }
}
