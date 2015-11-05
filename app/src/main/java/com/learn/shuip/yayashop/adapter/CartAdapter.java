package com.learn.shuip.yayashop.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.ShoppingCart;
import com.learn.shuip.yayashop.http.FrescoHelper;
import com.learn.shuip.yayashop.widget.CustomAddSubNumberView;

import java.util.Iterator;
import java.util.List;

import androidUtils.CartProvider;

/**
 * Created by Administrator on 15-10-29.
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener{

    private CheckBox mCheckBox;
    private TextView mTotalText;
    private List<ShoppingCart> mDatas;
    private CartProvider mCartProvider;

    public CartAdapter(Context context, List<ShoppingCart> datas,CheckBox checkBox,TextView textView) {
        super(context, datas, R.layout.template_cart_adapterview);

        this.mCheckBox = checkBox;
        this.mTotalText = textView;
        this.mDatas = datas;
        mCartProvider = new CartProvider(context);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(mCheckBox.isChecked());
                showTotalPrice();
            }
        });

        setOnItemClickListener(this);

        showTotalPrice();
    }

    @Override
    public void convert(final ShoppingCart item, BaseViewHolder holder) {
        CheckBox checkBox = (CheckBox) holder.getView(R.id.check);
        SimpleDraweeView img = holder.getSimpleDraweeView(R.id.img_cart);
        TextView text_descri = holder.getTextView(R.id.text_cart);
        final TextView text_price = holder.getTextView(R.id.price_cart);
        CustomAddSubNumberView numberView = (CustomAddSubNumberView) holder.getView(R.id.number_view);

        checkBox.setChecked(item.isChecked());
        FrescoHelper.loadImage(item.getImgUrl(), img);
        text_descri.setText(item.getName());
        text_price.setText("￥" + item.getPrice());
        numberView.setValue(item.getCount());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setIsChecked(isChecked);
                mCartProvider.update(item);

                checkListen();
                showTotalPrice();
            }
        });

        numberView.setOnButtonClickListener(new CustomAddSubNumberView.OnButtonClickListener() {
            @Override
            public void onAddButtonClick(View view, int value) {
                item.setCount(value);
                mCartProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onSubButtonClick(View view, int value) {
                item.setCount(value);
                mCartProvider.update(item);
                showTotalPrice();
            }
        });

    }

    public void showTotalPrice(){
        float total = computetTotalPrice();
        mTotalText.setText(Html.fromHtml("合计:￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    private float computetTotalPrice(){
        float totalPrice = 0;
        if (mDatas != null && mDatas.size() > 0){
            for (ShoppingCart cart : mDatas){
                if (cart.isChecked())
                    totalPrice = totalPrice + cart.getCount() * cart.getPrice();
            }
        }
        return totalPrice;
    }

    private void checkListen(){
        int count = 0;
        int checkNum = 0;
        if (mDatas != null && mDatas.size() > 0){
            count = mDatas.size();
            for (ShoppingCart cart : mDatas){
                if (!cart.isChecked()){
                    mCheckBox.setChecked(false);
                    break;
                }else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum)
                mCheckBox.setChecked(true);
        }
    }

    public void checkAll_None(boolean isChecked){
        if (mDatas == null || mDatas.size() < 0)
            return;
        int position = 0;
        for (ShoppingCart cart : mDatas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(position);
            position++;
        }
    }

    public void delCartItem(){
//        for (ShoppingCart cart : mDatas){
//            if (cart.isChecked()){
//                int position = mDatas.indexOf(cart);
//                mDatas.remove(cart);
//                mCartProvider.delete(cart);
//                notifyItemRemoved(position);
//            }
//        }

        if (mDatas == null || mDatas.size() < 0)
            return;
        for (Iterator iterator = mDatas.iterator();iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart)iterator.next();
            if (cart.isChecked()){
                int position = mDatas.indexOf(cart);
                iterator.remove();
                mCartProvider.delete(cart);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ShoppingCart cart = getitem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);

        checkListen();
        showTotalPrice();
    }
}
