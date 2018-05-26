package com.qunincey.shop.web.servlet;


import com.qunincey.shop.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ActiveCode",urlPatterns = "/ActiveCode")
public class ActiveCode  extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.setCharacterEncoding("utf-8");
       String ActiveCode=req.getParameter("ActiveCode");
        UserService userService=new UserService();
        boolean active=userService.active(ActiveCode);
        if (active){
            resp.sendRedirect(req.getContextPath()+"/index.jsp");
        }else {
            resp.sendRedirect(req.getContextPath()+"/register.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
