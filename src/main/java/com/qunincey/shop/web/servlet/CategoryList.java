package com.qunincey.shop.web.servlet;


import com.google.gson.Gson;
import com.qunincey.shop.bean.Category;
import com.qunincey.shop.service.ProductService;

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
        ProductService proS=new ProductService();
        //        查找分类
        List<Category> category=proS.findCategroy();
        resp.setContentType("text/html;charset=UTF-8");
        Gson gson=new Gson();
        String json=gson.toJson(category);
        resp.getWriter().write(json);
    }
}
