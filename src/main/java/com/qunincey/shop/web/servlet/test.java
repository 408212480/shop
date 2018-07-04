package com.qunincey.shop.web.servlet;

import com.qunincey.shop.bean.Product;
import com.qunincey.shop.service.ProductService;
import com.qunincey.shop.utils.FreemarkUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "test",urlPatterns = "/test")
public class test extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Map<String, Object> root = new HashMap<>();
        ProductService proS=new ProductService();
        Product product=proS.findProductInfo("1");
        root.put("product",product);

        FreemarkUtil fu=new FreemarkUtil(req);
        Configuration configuration=fu.getCfg();

        Template template= configuration.getTemplate("test2.ftl");
        resp.setContentType("text/html;charset="+template.getEncoding());
        Writer writer=resp.getWriter();
        try {
            template.process(root,writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
