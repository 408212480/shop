package com.qunincey.shop.web.servlet;


import com.qunincey.shop.service.UserService;
import com.qunincey.shop.utils.CommonsUtiles;
import com.qunincey.shop.utils.MailUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet(name = "User",urlPatterns = "/user")
public class User extends BaseServlet{
    /*
    * 注册账号
    * */
    public void registered(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("utf-8");

        Map<String,String[]> propreties=req.getParameterMap();
        com.qunincey.shop.bean.User user=new com.qunincey.shop.bean.User();
        try {
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class aClass, Object o) {
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    Date parse=null;
                    try {
                        parse=format.parse(o.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            },Date.class);
//            自动装配bean
            BeanUtils.populate(user,propreties);
            user.setUid(CommonsUtiles.getUUID());
            user.setTelephone(null);
            user.setState(0);
//            激活码
            user.setCode(CommonsUtiles.getUUID());
            UserService userService=new UserService();
            boolean isregister= userService.register(user);

            if (isregister){
                resp.sendRedirect(req.getContextPath()+"/registerSuccess.jsp");
                String emailMsg="恭喜你注册成功，点击下列链接激活账户<a href='http://localhost:8080/shop/user?method=activeCode&ActiveCode="+user.getCode()+"'>" +
                        "http://localhost:8080/shop/user?method=activeCode&ActiveCode="+user.getCode()+"</a>";

                try {
                    MailUtils.sendMail(user.getEmail(),emailMsg);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }else {
                resp.sendRedirect(req.getContextPath()+"/registerFail.jsp");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /*
    * 激活账号
    * */
    public void activeCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    /*
    * 检查名字是否被注册
    * */
    public void checkusername(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username=req.getParameter("username");
        UserService service=new UserService();
        boolean  isExist=service.checkusername(username);
        String json="{\"isExist\":"+isExist+"}";
        resp.getWriter().write(json);
    }
}
