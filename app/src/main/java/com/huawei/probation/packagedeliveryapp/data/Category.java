package com.huawei.probation.packagedeliveryapp.data;


import com.huawei.probation.packagedeliveryapp.common.Data;

public class Category extends Data {

    private int id;

    private String categoryName;

    private String productName;

    private String productComment;

    private String price;

    public Category() {
    }

    public Category(int id, String categoryName, String productName, int type, String productComment, String price) {
        this.id = id;
        this.categoryName = categoryName;
        this.productName = productName;
        setType(type);
        this.productComment = productComment;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductComment() {
        return productComment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductComment(String productComment) {
        this.productComment = productComment;
    }
}
