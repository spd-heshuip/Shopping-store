package androidUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.learn.shuip.yayashop.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-10-29.
 */
public class CartProvider {

    private static final String CART_JSON = "Cart_Json";

    private SparseArray<ShoppingCart> mDatas;

    private Context mContext;

    public CartProvider(Context context) {
        this.mContext = context;
        mDatas = new SparseArray<>();

       ListToSparse();
    }

    public void put(ShoppingCart cart){
        ShoppingCart temp = mDatas.get(cart.getId().intValue());
        if (temp != null){
            temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart;
        }
        mDatas.put(temp.getId().intValue(),temp);
        commit();
    }

    public void update(ShoppingCart cart){
        if (cart != null){
            mDatas.put(cart.getId().intValue(),cart);
        }
        commit();
    }

    public void delete(ShoppingCart cart){
        if (cart != null){
            mDatas.delete(cart.getId().intValue());
        }
        commit();
    }

    public void commit(){
        List<ShoppingCart> carts = sparseToList();
        String json = JSONUtil.toJson(carts);
        if (!TextUtils.isEmpty(json))
            PreferencesUtils.putString(mContext,CART_JSON,json);
    }

    private List<ShoppingCart> sparseToList(){
        List<ShoppingCart> temp = null;
        if (mDatas != null && mDatas.size() > 0){
            temp = new ArrayList<>();
            for (int i = 0;i < mDatas.size();i++){
                temp.add(i,mDatas.valueAt(i));
            }
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
        if (!TextUtils.isEmpty(json)){
            temp = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return temp;
    }
}
