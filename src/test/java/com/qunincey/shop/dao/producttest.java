package com.qunincey.shop.dao;

import com.qunincey.shop.bean.Product;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class producttest {

    @Test
    public void findHotProduct(){
        ProductDao pd=new ProductDao();
        try {
            List<Product> list=pd.findHotProduct();
            for (Product p:
                 list) {
                System.out.println(p.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
}
