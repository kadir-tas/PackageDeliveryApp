package com.huawei.probation.packagedeliveryapp.data;

public class ProductData {

    private String mProductId;
    private String mCategory;
    private String mProductName;
    private float mProductPrice;
    private String mDetail;

    public ProductData(String mProductId, String mProductName, float mProductPrice, String category) {
        this.mProductId = mProductId;
        this.mProductName = mProductName;
        this.mProductPrice = mProductPrice;
        this.mCategory = category;
    }

    public String getmProductId() {
        return mProductId;
    }

    public void setmProductId(String mProductId) {
        this.mProductId = mProductId;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public float getmProductPrice() {
        return mProductPrice;
    }

    public void setmProductPrice(float mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmDetail() {
        return mDetail;
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(getmProductId());
    }
}
