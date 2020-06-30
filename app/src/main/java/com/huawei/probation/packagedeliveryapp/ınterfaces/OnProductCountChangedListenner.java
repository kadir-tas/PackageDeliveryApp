package com.huawei.probation.packagedeliveryapp.ınterfaces;

import com.huawei.probation.packagedeliveryapp.data.ProductData;

public interface OnProductCountChangedListenner {
        void onProductIncerementedListener(ProductData productData);
        void onProductDecrementedListener(ProductData productData);
        void onProductCountChangedListener(ProductData productData, int count);
        void onProductRemovedListener(ProductData productData);

}
