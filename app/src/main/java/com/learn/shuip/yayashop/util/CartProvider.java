package com.learn.shuip.yayashop.util;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.learn.shuip.yayashop.bean.ShoppingCart;
import com.learn.shuip.yayashop.bean.Ware;
import com.learn.shuip.yayashop.system.ShopApplication;

import java.util.ArrayList;
import java.util.List;

import androidUtils.JSONUtil;
import androidUtils.PreferencesUtils;

/**
 * Created by Administrator on 15-10-29.
 */
public class CartProvider {

    private static final String TAG = CartProvider.class.getSimpleName();

    private static final String CART_JSON = "Cart_Json";

    private SparseArray<ShoppingCart> mDatas;

    private static class LazyHolder{
        private static final CartProvider mInstance = new CartProvider();
    }
    private Context mContext;


    private CartProvider() {
        mDatas = new SparseArray<>();
        mContext = ShopApplication.getContext();

        ListToSparse();
    }

    public static final CartProvider getInstance(){
        return LazyHolder.mInstance;
    }

    public void put(ShoppingCart cart){
        ShoppingCart temp = mDatas.get(cart.getId().intValue());
        if (temp != null){
            temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart;
            temp.setCount(1);
        }
        mDatas.put(cart.getId().intValue(),temp);
        commit();
    }

    public void put(Ware ware){
        ShoppingCart cart = wareToShoppingCart(ware);
        put(cart);
    }

    public void update(ShoppingCart cart){
        mDatas.put(cart.getId().intValue(), cart);
        commit();
    }

    public void delete(ShoppingCart cart){
        mDatas.delete(cart.getId().intValue());
        commit();
    }

    public void commit(){
        List<ShoppingCart> carts = sparseToList();
        String json = JSONUtil.toJson(carts);
        if (json != null){
            PreferencesUtils.putString(mContext, CART_JSON, json);
        }
        else{
            PreferencesUtils.putString(mContext,CART_JSON,"");
        }
    }

    private List<ShoppingCart> sparseToList(){
        int size = mDatas.size();
        List<ShoppingCart> temp = new ArrayList<>(size);
        for (int i = 0;i < size;i++){
            temp.add(mDatas.valueAt(i));
        }
        return temp;
    }

    private void ListToSparse(){
        List<ShoppingCart> carts = getAll();
        if (carts != null && carts.size() > 0){
            for (ShoppingCart cart : carts){
                mDatas.put(cart.getId().intValue(),cart);
            }
        }
    }

    public List<ShoppingCart> getAll(){
        String json = PreferencesUtils.getString(mContext, CART_JSON);

        List<ShoppingCart> temp = null;
        if (json != null){
            temp = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return temp;
    }

    public int getCartNumber(){
        int number = 0;
        List<ShoppingCart> carts = getAll();
        for (ShoppingCart cart : carts){
            number = number + cart.getCount();
        }
        return number;
    }

    private ShoppingCart wareToShoppingCart(Ware ware) {
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
