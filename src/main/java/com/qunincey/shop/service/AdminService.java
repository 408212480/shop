package com.qunincey.shop.service;

import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.Order;
import com.qunincey.shop.bean.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface AdminService {

	public List<Category> findAllCategory();

	public void saveProduct(Product product) throws SQLException;

	public List<Order> findAllOrders();

	public List<Map<String, Object>> findOrderInfoByOid(String oid);

}
