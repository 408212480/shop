package com.qunincey.shop.service;

import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.PageBean;
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

    public PageBean findProductByCid(int cid,int CurrentPage,int CurrentCount){
        PageBean pageBean=new PageBean();

        pageBean.setCurrentCount(CurrentCount);
        pageBean.setCurrentPage(CurrentPage);
        ProductDao productDao=new ProductDao();
        int totalCount=0;
        try {
            totalCount= productDao.findCountByCid(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        设置所有数目
        pageBean.setTotalCount(totalCount);
        int totalPage= (int) Math.ceil(1.0*(totalCount/CurrentCount));
        pageBean.setTotalPage(totalPage);

//        开始的索引
        int index=(CurrentPage-1)*CurrentCount;
        List<Product> list=null;
        try {
            list=productDao.findProductByPage(cid,index,CurrentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setList(list);
        return pageBean;
    }
}
