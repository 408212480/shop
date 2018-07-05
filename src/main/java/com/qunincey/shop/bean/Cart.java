package com.qunincey.shop.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    /*
    * 购物车存入购物项
    * */
    private Map<String,CartItem> cartItems=new HashMap<>();
    /*
    * 总金额
    * */
    private double Totle;

    public Map<String, CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Map<String, CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotle() {
        return Totle;
    }

    public void setTotle(double totle) {
        Totle = totle;
    }
}
