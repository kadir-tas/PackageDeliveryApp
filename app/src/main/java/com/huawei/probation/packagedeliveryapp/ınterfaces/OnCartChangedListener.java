package com.huawei.probation.packagedeliveryapp.ınterfaces;

import com.huawei.probation.packagedeliveryapp.data.ProductData;

import java.util.HashMap;

public interface OnCartChangedListener {
    void onItemChangedListener(ProductData productData, int count);
    void onCartChangedListener(HashMap<ProductData, Integer> cart, float totalPrice);
    void onItemRemovedListener(ProductData productData);
    void onCartClearedListener();
}
