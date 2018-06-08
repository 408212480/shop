package com.qunincey.shop.service;

import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.Product;
import com.qunincey.shop.dao.ProductDao;

import java.sql.SQLException;
import java.util.List;

public class ProductService {


    public List<Product> findHotProduct(){
        ProductDao productDao=new ProductDao();
        List<Product> list=null;
        try {
             list=productDao.findHotProduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> findNewProduct(){
        ProductDao productDao=new ProductDao();
        List<Product> list=null;
        try {
            list=productDao.findNewProduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Category> findCategroy(){
        ProductDao product=new ProductDao();
        List<Category> list=null;
        try {
            list=product.findCategroy();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
