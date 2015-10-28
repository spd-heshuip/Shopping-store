package com.learn.shuip.yayashop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.CategoryList;

import java.util.List;

/**
 * Created by Administrator on 15-10-24.
 */
public class CategoryListAdapter extends SimpleAdapter<CategoryList> {

    public CategoryListAdapter(Context context, List<CategoryList> datas) {
        super(context, datas, R.layout.template_single_textview);
    }

    @Override
    public void convert(CategoryList item, BaseViewHolder holder) {
        TextView textView = holder.getTextView(R.id.category_text);

        textView.setText(item.getName());
    }
}
