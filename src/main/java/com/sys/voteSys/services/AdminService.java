package com.sys.voteSys.services;

import com.sys.voteSys.pojo.User;
import com.sys.voteSys.util.MySqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Zlei
 * @date 2021/4/27  13:51
 */
public interface AdminService {

    public void deleteUser(User user );

    public User findUser(String username);

    public void updateUser(User user);

    public void addUser(User user );

}
