package com.huawei.probation.packagedeliveryapp.data;

import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart {

    private ArrayList<OnCartChangedListener> mOnCartChangedListeners;

    private HashMap<ProductData, Integer> mCart;

    public Cart() {
        mCart = new HashMap<>();
        mOnCartChangedListeners = new ArrayList<OnCartChangedListener>();
    }

    private void callCartChangedListener(){
        float totalPrice = getTotalCartPrice();
        for(OnCartChangedListener listener : mOnCartChangedListeners){
            listener.onCartChangedListener(mCart, totalPrice);
        }
    }

    private void callOnItemChanged(ProductData productData, int count){
        for(OnCartChangedListener listener: mOnCartChangedListeners){
            listener.onItemChangedListener(productData, count);
        }
    }

    private void callProductRemovedListener(ProductData productRemoved){
        for(OnCartChangedListener listener : mOnCartChangedListeners){
            listener.onItemRemovedListener(productRemoved);
        }
    }

    private void callCartClearedListener(){
        for(OnCartChangedListener listener : mOnCartChangedListeners){
            listener.onCartClearedListener();
        }
    }

    public float getTotalCartPrice(){
        float totalPrice = 0;
        for(ProductData product: mCart.keySet()){
            totalPrice += mCart.get(product) * product.getmProductPrice();
        }
        return totalPrice;
    }


    public int getItemCount(ProductData productData){
        if(mCart.containsKey(productData)){
            return mCart.get(productData);
        }else{
            return 0;
        }
    }

    public void putItem(ProductData productData, int count){
        mCart.put(productData, count);
        callOnItemChanged(productData,count);
        callCartChangedListener();
    }

    public void removeItem(ProductData productData){
        if(mCart.containsKey(productData)){
            mCart.remove(productData);
            callProductRemovedListener(productData);
        }
    }

    public void clear(){
        mCart.clear();
        callCartClearedListener();
    }

    public HashMap<ProductData, Integer> getCart(){
        return mCart;
    }

    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener){
        mOnCartChangedListeners.clear();
        mOnCartChangedListeners.add(onCartChangedListener);
    }

    public ArrayList<Pair<ProductData, Integer>> toListPair(){
        ArrayList<Pair<ProductData, Integer>> result = new ArrayList<>();
        for(ProductData key : mCart.keySet()){
            result.add( new Pair<ProductData, Integer>( key, mCart.get(key) ) );
        }
        return result;
    }

    public void addOnCartChangedListener(OnCartChangedListener onCartChangedListener){
        if(!mOnCartChangedListeners.contains(onCartChangedListener))
        {
            mOnCartChangedListeners.add(onCartChangedListener);
        }
        else{
            throw new RuntimeException("AAAa");
        }
    }

    public void removeOnCartChangedListener(OnCartChangedListener onCartChangedListener){
        if(mOnCartChangedListeners.contains(onCartChangedListener)){
            mOnCartChangedListeners.remove(onCartChangedListener);
        }
    }
}
