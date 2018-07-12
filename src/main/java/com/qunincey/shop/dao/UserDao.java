package com.qunincey.shop.dao;

import com.qunincey.shop.bean.User;
import com.qunincey.shop.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

public class UserDao {

    public int regist(User user){
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String sql=" insert into user values(?,?,?,?,?,?,?,?,?,?) ";
        int update=0;
        try {
            update=qr.update(sql,user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),
                       user.getBirthday(),user.getSex(),user.getState(),user.getCode());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update;

    }

    public int active(String activeCode) {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String active=" select * from user where code = ? ";
        int rows=0;
        try {
            User user=qr.query(active,new BeanHandler<User>(User.class),activeCode);
            if (user!=null){
                String insert=" update user set state = 1 where code = ? ";
                rows=qr.update(insert,activeCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;

    }

    public Long checkUserName(String username) throws SQLException {
        QueryRunner qr=new QueryRunner(DataSourceUtils.getDataSource());
        String check=" select count(*) from user where username= ? ";
        Long rows=qr.query(check,new ScalarHandler<Long>(1),username);
        return rows;
    }

    public User login(String username, String password) throws SQLException {

        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where username=? and password=?";
        return runner.query(sql, new BeanHandler<User>(User.class), username,password);


    }
}
