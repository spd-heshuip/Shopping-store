package com.learn.shuip.yayashop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.learn.shuip.yayashop.R;
import com.learn.shuip.yayashop.bean.ShoppingCart;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.http.FrescoHelper;

import java.util.List;

import androidUtils.CartProvider;
import androidUtils.ToastUtils;

/**
 * Created by Administrator on 15-10-22.
 */
public class HotWaresAdapter extends SimpleAdapter<Ware>{

    private static final String TAG = HotWaresAdapter.class.getSimpleName();

    private Context mContext;
    private CartProvider mCartProvider;

    public HotWaresAdapter(Context context, List<Ware> datas) {
        super(context,datas,R.layout.template_hot_cardview);
        this.mContext = context;
        mCartProvider = new CartProvider(context);
    }

    @Override
    public void convert(final Ware item, BaseViewHolder holder) {
        SimpleDraweeView mDraweeView = holder.getSimpleDraweeView(R.id.img_hot);
        TextView mDescription = holder.getTextView(R.id.description_hot);
        TextView mPrice = holder.getTextView(R.id.price_hot);
        Button mBuy = holder.getButton(R.id.buy_button);

        mDescription.setText(item.getName());
        mPrice.setText("￥" + item.getPrice());
        FrescoHelper.loadImage(item.getImgUrl(), mDraweeView);

        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart cart = wareToShoppingCart(item);
                mCartProvider.put(cart);

                ToastUtils.show(mContext,R.string.addToCart, Toast.LENGTH_SHORT);
            }
        });
    }

    private  ShoppingCart wareToShoppingCart(Ware ware){
        ShoppingCart cart = new ShoppingCart();

        cart.setCount(1);
        cart.setIsChecked(true);
        cart.setId(ware.getId());
        cart.setImgUrl(ware.getImgUrl());
        cart.setName(ware.getName());
        cart.setPrice(ware.getPrice());

        return cart;
    }
}
