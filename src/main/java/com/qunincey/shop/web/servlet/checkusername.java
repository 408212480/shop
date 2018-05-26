package com.qunincey.shop.web.servlet;


import com.qunincey.shop.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "checkusername",urlPatterns = "/checkusername")
public class checkusername extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username=req.getParameter("username");
        UserService service=new UserService();
        boolean  isExist=service.checkusername(username);
        String json="{\"isExist\":"+isExist+"}";
        resp.getWriter().write(json);

    }
}
