package com.learn.shuip.yayashop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.http.FrescoHelper;

import java.util.List;

/**
 * Created by Administrator on 15-10-26.
 */
public class WaresAdapter extends SimpleAdapter<Ware>{

    public WaresAdapter(Context context, List<Ware> datas) {
        super(context, datas, R.layout.template_grid_wares);
    }

    @Override
    public void convert(Ware item, BaseViewHolder holder) {
        SimpleDraweeView draweeView = holder.getSimpleDraweeView(R.id.wares_image);
        TextView textDescrip = holder.getTextView(R.id.wares_text);
        TextView textPrice = holder.getTextView(R.id.wares_price);

        FrescoHelper.loadImage(item.getImgUrl(),draweeView);
        textDescrip.setText(item.getName());
        textPrice.setText("￥" + item.getPrice());
    }
}
