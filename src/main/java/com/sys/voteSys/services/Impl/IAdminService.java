package com.sys.voteSys.services.Impl;

import com.sys.voteSys.pojo.User;
import com.sys.voteSys.services.AdminService;
import com.sys.voteSys.util.MySqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Zlei
 * @date 2021/4/27  13:57
 */
public class IAdminService implements AdminService {

    @Override
    public void deleteUser(User user){
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = conn.prepareStatement ("delete from user where username = ?");
            preparedStatement.setString (1,user.getUsername ());
            int i = preparedStatement.executeUpdate ( );
            System.out.println (i );
        } catch (SQLException throwable) {
            throwable.printStackTrace ( );
        }
    }

    @Override
    public User findUser(String username) {
        User user=new User (  );
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=conn.prepareStatement ("select * from user where username=?");
            preparedStatement.setString (1,username);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (!resultSet.next ()){
                return null;
            }

                user=User.builder ()
                            .id (resultSet.getInt ("id"))
                            .username (resultSet.getString ("username"))
                            .password (resultSet.getString ("password"))
                            .sex (resultSet.getString ("sex"))
                            .age (resultSet.getInt ("age"))
                            .email (resultSet.getString ("email"))
                            .phone (resultSet.getString ("phone"))
                            .build ();

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;

        try {
            preparedStatement=conn.prepareStatement ("update user set password=?,sex=?,age=?,email=?,phone=? where username=? ");
            preparedStatement.setString (1,user.getPassword ());
            preparedStatement.setString (2, user.getSex ( ));
            preparedStatement.setInt (3,user.getAge ());
            preparedStatement.setString (4, user.getEmail ( ));
            preparedStatement.setString (5,user.getPhone ());
            preparedStatement.setString (6, user.getUsername ( ));
            int i = preparedStatement.executeUpdate ( );
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }

    }

    @Override
    public void addUser(User user) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=conn.prepareStatement ("insert into user (username,password,sex,age,email,phone) values(?,?,?,?,?,?) ");
            preparedStatement.setString (1, user.getUsername ( ));
            preparedStatement.setString (2, user.getPassword ( ));
            preparedStatement.setString (3,user.getSex ());
            preparedStatement.setInt (4,user.getAge ());
            preparedStatement.setString (5,user.getEmail ());
            preparedStatement.setString (6,user.getPhone ());
            preparedStatement.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }

    }


}
