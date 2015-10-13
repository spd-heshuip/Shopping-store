package com.learn.shuip.yayashop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.HomeCategory;

import java.util.List;

/**
 * Created by Administrator on 15-10-13.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{

    private static final int VIEW_TYPE_L = 0;
    private static final int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;
    private List<HomeCategory> mDatas;

    public HomeCategoryAdapter(List<HomeCategory> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_L)
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,viewGroup,false));
        else
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int positon) {
        HomeCategory category = mDatas.get(positon);
        viewHolder.textTitle.setText(category.getName());
        viewHolder.imageViewBig.setImageResource(category.getImgBig());
        viewHolder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
        viewHolder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0){
            return VIEW_TYPE_R;
        }else
            return VIEW_TYPE_L;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
        }
    }
}
