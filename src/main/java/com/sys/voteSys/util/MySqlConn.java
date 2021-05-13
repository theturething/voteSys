package com.sys.voteSys.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Zlei
 * @date 2021/4/23  15:32
 */
@ManagedBean
@SessionScoped
public class MySqlConn {

    private static Connection connection;

    public static Connection getConn(){

        String url = "jdbc:mysql://localhost:3306/voteSys?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=true";

        String username = "root";
        String password = "123456";

        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection (url,username,password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace ( );
        }

        return connection;

    }

}
