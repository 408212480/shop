package com.qunincey.shop.web.servlet;

import com.qunincey.shop.bean.PageBean;
import com.qunincey.shop.bean.Product;
import com.qunincey.shop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "productListByCid",urlPatterns = "/productListByCid")
public class productListByCid extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cid= Integer.parseInt(req.getParameter("cid"));
        String currentPage= req.getParameter("currentPage");
        if (currentPage==null){
            currentPage="1";
        }
        int currentCount=12;
        ProductService psd=new ProductService();
        PageBean pageBean=psd.findProductByCid(cid, Integer.parseInt(currentPage),currentCount);
        req.setAttribute("pageBean",pageBean);
        req.setAttribute("cid",cid);
        req.setAttribute("currentPage",currentPage);
        req.getRequestDispatcher("/product_list.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
