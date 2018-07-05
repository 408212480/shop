package com.qunincey.shop.bean;

public class CartItem {

    private Product product;//加入购物车的商品
    private int buyNum;//购买的数量
    private double subTotal;//小计

    public CartItem(Product product, int buyNum, double subTotal) {
        this.product = product;
        this.buyNum = buyNum;
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
