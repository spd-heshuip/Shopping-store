package com.learn.shuip.yayashop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.Campaign;
import com.learn.shuip.yayashop.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 15-10-13.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{

    private static final int VIEW_TYPE_L = 0;
    private static final int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;
    private List<HomeCampaign> mDatas;
    private Context mContext;

    private onCampaignClickListener mOnClickListener;

    public void setOnCampaignClickListener(onCampaignClickListener listener) {
        this.mOnClickListener = listener;
    }

    public HomeCategoryAdapter(List<HomeCampaign> mDatas,Context context) {
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_L)
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,viewGroup,false),mDatas,mOnClickListener);
        else
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,viewGroup,false),mDatas,mOnClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int positon) {
        HomeCampaign homeCampaign = mDatas.get(positon);
        viewHolder.textTitle.setText(homeCampaign.getTitle());
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);

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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        private List<HomeCampaign> data;
        private onCampaignClickListener listener;

        public ViewHolder(View itemView,List<HomeCampaign> data,onCampaignClickListener listener) {
            super(itemView);
            this.data = data;
            this.listener = listener;
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            HomeCampaign homeCampaign = data.get(getLayoutPosition());
            if (listener != null){
                switch (v.getId()){
                    case R.id.imgview_big:
                        listener.onClick(v, homeCampaign.getCpOne());
                        break;
                    case R.id.imgview_small_top:
                        listener.onClick(v, homeCampaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom:
                        listener.onClick(v, homeCampaign.getCpThree());
                        break;
                }
            }
        }
    }

    public interface onCampaignClickListener{
        void onClick(View view,Campaign campaign);
    }
}
