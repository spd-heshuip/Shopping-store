package com.learn.shuip.yayashop.adapter;

import android.content.Context;

import com.learn.shuip.yayashop.R;

import java.util.List;

/**
 * Created by Administrator on 15-10-22.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder>{
    public SimpleAdapter(Context context, int layoutId) {
        super(context, R.layout.template_hot_cardview);
    }

    public SimpleAdapter(Context context, List<T> datas, int layoutId) {
        super(context, layoutId,datas);
    }
}
