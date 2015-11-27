package com.learn.shuip.yayashop.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.Campaign;
import com.learn.shuip.yayashop.bean.HomeCampaign;
import com.learn.shuip.yayashop.http.FrescoHelper;

import java.util.List;

/**
 * Created by Administrator on 15-10-13.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{

    private static final String TAG = HomeCategoryAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_L = 0;
    private static final int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;
    private List<HomeCampaign> mDatas;
    private Context mContext;

    private OnItemClickListener mOnClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnClickListener = listener;
    }

    public HomeCategoryAdapter(List<HomeCampaign> mDatas,Context context) {
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_L) {
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview, viewGroup, false), mDatas, mOnClickListener);
        }
        else{
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,viewGroup,false),mDatas,mOnClickListener);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int positon) {
        HomeCampaign homeCampaign = mDatas.get(positon);
        viewHolder.textTitle.setText(homeCampaign.getTitle());

        FrescoHelper.loadImage(homeCampaign.getCpOne().getImgUrl(), viewHolder.imageViewBig);
        FrescoHelper.loadImage(homeCampaign.getCpTwo().getImgUrl(),viewHolder.imageViewSmallBottom);
        FrescoHelper.loadImage(homeCampaign.getCpThree().getImgUrl(), viewHolder.imageViewSmallTop);
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
        SimpleDraweeView imageViewBig;
        SimpleDraweeView imageViewSmallTop;
        SimpleDraweeView imageViewSmallBottom;

        private List<HomeCampaign> data;
        private OnItemClickListener listener;

        public ViewHolder(View itemView,List<HomeCampaign> data,OnItemClickListener listener) {
            super(itemView);
            this.data = data;
            this.listener = listener;
            textTitle = (TextView) itemView.findViewById(R.id.text_title);

            imageViewBig = (SimpleDraweeView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (SimpleDraweeView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (SimpleDraweeView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                anim(view);
            }
        }

        private void anim(final View view){
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"alpha",1.0F,0.5F,1.0F)
                    .setDuration(500);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    HomeCampaign homeCampaign = data.get(getLayoutPosition());
                    switch (view.getId()){
                        case R.id.imgview_big:
                            listener.onClick(view, homeCampaign.getCpOne());
                            break;
                        case R.id.imgview_small_top:
                            listener.onClick(view, homeCampaign.getCpTwo());
                            break;
                        case R.id.imgview_small_bottom:
                            listener.onClick(view, homeCampaign.getCpThree());
                            break;
                    }
                }
            });
            objectAnimator.start();
        }
    }

    public interface OnItemClickListener {
        void onClick(View view,Campaign campaign);
    }
}
