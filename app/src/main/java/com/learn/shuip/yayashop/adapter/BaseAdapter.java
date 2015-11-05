package com.learn.shuip.yayashop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 15-10-22.
 */
public abstract class BaseAdapter<T,H extends BaseViewHolder> extends
        RecyclerView.Adapter<BaseViewHolder>{

    protected List<T> mDatas;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected int mLayoutId;

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public BaseAdapter(Context context, int layoutId) {
        this(context,layoutId,null);
    }

    public BaseAdapter(Context context,int layoutId,List<T> datas) {
        this.mContext = context;
        this.mDatas= datas;
        mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutId, parent, false);
        return new BaseViewHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = getitem(position);
        convert(item, (H) holder);
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0)
            return mDatas.size();
        return 0;
    }

    public T getitem(int position){
        return mDatas.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void clearData(){
        if (mDatas == null)
            return;
        int count = mDatas.size();
        if (count > 0){
            mDatas.clear();
            notifyItemRangeRemoved(0,count);
        }
    }

    public void addData(List<T> datas){
        addData(0,datas);
    }

    public List<T> getDatas(){
        if (mDatas != null){
            return mDatas;
        }
        return null;
    }

    public void addData(int position,List<T> datas){
        if (mDatas == null)
            return;
        if (datas != null && datas.size() > 0){
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, datas.size());
        }
    }

    public abstract void convert(T item,H holder);

}
