package com.qunincey.shop.dao;

import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.Order;
import com.qunincey.shop.bean.OrderItem;
import com.qunincey.shop.bean.Product;
import com.qunincey.shop.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import sun.security.x509.OIDMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

    public List<Category> findCategroy()throws SQLException{
            QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
            String sql=" select * from category ";
            return qr.query(sql,new BeanListHandler<Category>(Category.class));
    }

    public int findCountByCid(int cid) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select count(*) from product where cid = ? ";
        Long count= qr.query(sql,new ScalarHandler<Long>(1),cid);
        return count.intValue();
    }

    public List<Product> findProductByPage(int cid,int index,int CurrentCount) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select * from product where cid =? limit ?,? ";
        List<Product> list=qr.query(sql,new BeanListHandler<Product>(Product.class),cid,index,CurrentCount);
        return  list;
    }

    public Product findProductInfo(int pid) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select * from product where pid= ? ";
        return qr.query(sql,new BeanHandler<Product>(Product.class),pid);


    }
    /*
    向order表插入数据
    */
    public void addOrders(Order order ){
        QueryRunner qr=new QueryRunner();
        String sql=" insert into orders values (?,?,?,?,?,?,?,?) ";
        try {
            Connection conn=DataSourceUtils.getConnection();
            qr.update(conn,sql,order.getOid(),order.getOrdertime(),order.getTotal(),order.getState()
                       ,order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
    * 向orderitem表插入数据
    * */
    public void addOrderItem(Order order){
        QueryRunner qr=new QueryRunner();
        String sql=" insert into orderitem values (?,?,?,?,?) ";
        try {
            Connection conn=DataSourceUtils.getConnection();
            List<OrderItem> orderItem=order.getOrderItems();
            for (OrderItem item:
                 orderItem) {
                qr.update(conn,sql,item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void updateAdrr(Order order) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" update orders set address=?,name=?,telephone=? where uid=? ";
        qr.update(sql,order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
    }

    public List<Order> findOrderById(String uid) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select * from orders where uid=? ";
        return qr.query(sql,new BeanListHandler<Order>(Order.class),uid);

    }

    public List<Map<String,Object>> findOrderItemById(String oid) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,product p where i.pid=p.pid and i.oid= ? ";
        List<Map<String,Object>> maplist=qr.query(sql,new MapListHandler(),oid);
        return maplist;
    }



}
