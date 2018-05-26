package com.qunincey.shop.dao;

import com.qunincey.shop.bean.User;
import com.qunincey.shop.utils.CommonsUtiles;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

public class usertest {
    User user=null;

    @Before
    public void init(){
        user=new User();
        user.setUid(CommonsUtiles.getUUID());
        user.setUsername("qunincey");
        user.setPassword("qunincey");
        user.setName("qunincey");
        user.setEmail("qunincey");
        user.setTelephone("qunincey");
        user.setBirthday(new Date());
        user.setState(0);
        user.setCode(CommonsUtiles.getUUID());
    }


    @Test
    public void testUserDao(){
        UserDao userDao=new UserDao();
        Long a=0L;
        try {
            a=userDao.checkUserName("qunincey");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(a>0?true:false);

    }
}
