package com.qunincey.shop.web.servlet;


import com.qunincey.shop.bean.Product;
import com.qunincey.shop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="ProductInfo",urlPatterns = "/ProductInfo")
public class ProductInfo extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pid=req.getParameter("pid");
        ProductService proS=new ProductService();
        Product product=proS.findProductInfo(pid);
        req.setAttribute("product",product);
        req.getRequestDispatcher("/product_info.jsp").forward(req,resp);
    }
}
