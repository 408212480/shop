package com.qunincey.shop.service;

import com.qunincey.shop.bean.*;
import com.qunincey.shop.dao.ProductDao;
import com.qunincey.shop.utils.DataSourceUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

    public Product findProductInfo(String pid){
        ProductDao productDao=new ProductDao();
        Product product=null;
        try {
            product=productDao.findProductInfo(Integer.parseInt(pid));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    /*
    * 存储订单
    * */
    public  void  submitOrder(Order order){

        ProductDao productDao=new ProductDao();
        try {
            /*开始事务*/
            DataSourceUtils.startTransaction();
            productDao.addOrders(order);
            productDao.addOrderItem(order);
        } catch (SQLException e) {
            try {
                DataSourceUtils.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                DataSourceUtils.commitAndRelease();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateOrderAdrr(Order order) {
        ProductDao productDao=new ProductDao();
        try {
            productDao.updateAdrr(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    /*
    * 通过uid查询所有订单
    *
    * */
    public List<Order> findAllOrder(String uid) {
        ProductDao productDao=new ProductDao();
        List<Order> list=null;
        try {
            list=productDao.findOrderById(uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    /*
    * 通过订单id查询每个订单项
    *
    * */

    public List<Map<String,Object>> findAllOrderItem(String oid) {
        ProductDao productDao=new ProductDao();
        List<Map<String,Object>> maplist=null;
        try {
            maplist=productDao.findOrderItemById(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maplist;


    }

}
