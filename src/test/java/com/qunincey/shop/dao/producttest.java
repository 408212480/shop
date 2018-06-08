package com.qunincey.shop.dao;

import com.google.gson.Gson;
import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.Product;
import com.qunincey.shop.service.ProductService;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class producttest {

    @Test
    public void findHotProduct(){
        ProductService ps=new ProductService();
            List<Product> list=ps.findHotProduct();
            for (Product p:
                 list) {
                System.out.println(p.toString());
            }


    }

    @Test
    public void findNewProduct(){
        ProductDao pd=new ProductDao();
        try {
            List<Product> list=pd.findNewProduct();
            for (Product p:
                    list) {
                System.out.println(p.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findCategroy(){
        ProductService productService=new ProductService();
        List<Category> list=  productService.findCategroy();
        Gson gson=new Gson();
        String json=gson.toJson(list);
        System.out.println(json);
    }
}
