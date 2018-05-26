package com.qunincey.shop.service;

import com.qunincey.shop.bean.User;
import com.qunincey.shop.dao.UserDao;

import java.sql.SQLException;

public class UserService {

    public  boolean register(User user){

        UserDao dao=new UserDao();
        int rows=dao.regist(user);
        return rows>0?true:false;
    }
    public  boolean active(String ActiveCode){
        UserDao dao=new UserDao();
        int rows=dao.active(ActiveCode);
        return rows>0?true:false;
    }
    public boolean checkusername(String username){
        UserDao dao=new UserDao();
        Long rows= 0L;
        try {
            rows = dao.checkUserName(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows>0?true:false;
    }
}
