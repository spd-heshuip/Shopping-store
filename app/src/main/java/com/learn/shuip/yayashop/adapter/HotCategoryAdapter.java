package com.learn.shuip.yayashop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.http.FrescoHelper;

import java.util.List;

/**
 * Created by Administrator on 15-10-16.
 */
public class HotCategoryAdapter extends RecyclerView.Adapter<HotCategoryAdapter.ViewHolder>{

    private List<Ware> mDatas;
    private LayoutInflater mInflater;
    public HotCategoryAdapter(List<Ware> datas) {
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.template_hot_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ware ware = mDatas.get(position);

        holder.mDescription.setText(ware.getName());
        holder.mPrice.setText("￥" + ware.getPrice());
        FrescoHelper.loadImage(ware.getImgUrl(), holder.mDraweeView);

    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0)
            return mDatas.size();
        return 0;
    }

    public Ware getData(int location){
        return mDatas.get(location);
    }

    public List<Ware> getDatas() {
        return mDatas;
    }

    public void clearData(){
        if (mDatas != null){
            mDatas.clear();
            notifyItemRangeRemoved(0,mDatas.size());
        }
    }

    public void addData(List<Ware> datas){
        this.addData(0,datas);
    }

    public void addData(int position,List<Ware> datas){
        if (datas != null && datas.size() > 0){
            mDatas.addAll(datas);
            notifyItemRangeChanged(position,datas.size());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView mDraweeView;
        private TextView mDescription;
        private TextView mPrice;
        private Button mBuy;

        public ViewHolder(View itemView) {
            super(itemView);

            mDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.img_hot);
            mDescription = (TextView) itemView.findViewById(R.id.description_hot);
            mPrice = (TextView) itemView.findViewById(R.id.price_hot);
            mBuy = (Button) itemView.findViewById(R.id.buy_button)
;        }
    }
}
