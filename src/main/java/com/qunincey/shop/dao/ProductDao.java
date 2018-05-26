package com.qunincey.shop.dao;

import com.qunincey.shop.bean.Product;
import com.qunincey.shop.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class ProductDao {

//    获得热门商品
    public List<Product> findHotProduct() throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select * from product where is_hot=? limit ?,? ";
        return  qr.query(sql,new BeanListHandler<Product>(Product.class),1,0,9);
    }

//    获得最新商品
    public List<Product> findNewProduct() throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select * from product order by  pdate desc limit ?,? ";
        return qr.query(sql,new BeanListHandler<Product>(Product.class),0,9);
    }


}
