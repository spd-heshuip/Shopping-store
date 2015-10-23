package com.learn.shuip.yayashop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 15-10-22.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private BaseAdapter.OnItemClickListener mItemClickListener;

    private SparseArray<View> mViews;

    public BaseViewHolder(View itemView,BaseAdapter.OnItemClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.mItemClickListener = itemClickListener;
        this.mViews = new SparseArray<View>();
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }

    public Button getButton(int viewId) {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return retrieveView(viewId);
    }

    public SimpleDraweeView getSimpleDraweeView(int viewId){
        return retrieveView(viewId);
    }

    public View getView(int viewId){
        return retrieveView(viewId);
    }

    private <T extends View> T retrieveView(int viewId){
        View view = mViews.get(viewId);
        if (view == null){
            view = itemView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null){
            mItemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
