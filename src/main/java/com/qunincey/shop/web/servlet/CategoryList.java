package com.qunincey.shop.web.servlet;


import com.google.gson.Gson;
import com.qunincey.shop.bean.Category;
import com.qunincey.shop.service.ProductService;
import com.qunincey.shop.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryList",urlPatterns = "/CategoryList")
public class CategoryList extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jedis jedis= JedisPoolUtils.getJedis();
        String categoryListjson=jedis.get("categoryList");
        if (categoryListjson==null){
            ProductService proS=new ProductService();
            //        查找分类
            List<Category> category=proS.findCategroy();
            Gson gson=new Gson();
            categoryListjson=gson.toJson(category);
            jedis.set("categoryListjson",categoryListjson);
        }
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(categoryListjson);
    }
}
