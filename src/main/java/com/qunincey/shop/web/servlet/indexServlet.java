package com.qunincey.shop.web.servlet;


import com.qunincey.shop.bean.Product;
import com.qunincey.shop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "indexServlet",urlPatterns = "/indexServlet")
public class   indexServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);

        ProductService proS=new ProductService();
//        查找热门商品
        List<Product> HotProduct=proS.findHotProduct();
//        查找最新商品
        List<Product> NewProduct=proS.findNewProduct();

        req.setAttribute("HotProduct",HotProduct);
        req.setAttribute("NewProduct",NewProduct);

        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
