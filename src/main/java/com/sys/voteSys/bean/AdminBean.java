package com.sys.voteSys.bean;


import com.sys.voteSys.pojo.User;
import com.sys.voteSys.services.AdminService;
import com.sys.voteSys.services.Impl.IAdminService;
import com.sys.voteSys.util.MySqlConn;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * @author Zlei
 * @date 2021/4/26  20:11
 */
public class AdminBean implements Serializable {

    private User user;

    private ArrayList users;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement = conn.prepareStatement ("select * from user ");
        ResultSet resultSet = preparedStatement.executeQuery ( );
        while (resultSet.next ()){
            User user = new User ( );
            user.setId (resultSet.getInt ("id"));
            user.setUsername (resultSet.getString ("username"));
            user.setPassword (resultSet.getString ("password"));
            user.setAge (resultSet.getInt ("age"));
            user.setSex (resultSet.getString ("sex"));
            user.setEmail (resultSet.getString ("email"));
            user.setPhone (resultSet.getString ("phone"));
            users.add (user);
        }
        this.users=users;
        return this.users;
    }

    public void setUsers(ArrayList users) {
        this.users = users;
    }

    public AdminBean() {

    }

    public AdminBean(User user, ArrayList users) {
        this.user = user;
        this.users = users;
    }

    public String updateUser(){
        IAdminService iAdminService = new IAdminService ( );
        User userOld = iAdminService.findUser (this.user.getUsername ( ));
        User newUser = new User ( );
        newUser.setUsername (userOld.getUsername ());
        newUser.setPassword (user.getPassword ());
        newUser.setAge (user.getAge ());
        newUser.setSex (user.getSex ());
        newUser.setEmail (user.getEmail ());
        newUser.setPhone (user.getPhone ());
        iAdminService.updateUser (newUser);
        return "viewUser";
    }


    public void updateUser(ActionEvent event){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance ( ).getExternalContext ( ).getRequest ( );
        String username = request.getParameter ("username");
        IAdminService iAdminService = new IAdminService ( );
        User user = iAdminService.findUser (username);
        this.user.setId (user.getId ());
        this.user.setUsername (user.getUsername ());
        this.user.setPassword (user.getPassword ());
        this.user.setAge (user.getAge ());
        this.user.setSex (user.getSex ());
        this.user.setEmail (user.getEmail ());
        this.user.setPhone (user.getPhone ());
    }

    public String addUser(){
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement =null;
        try {
            preparedStatement=conn.prepareStatement ("select username from user");
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                if (user.getUsername ().equals (resultSet.getString ("username"))){
                    return "addUserFailure";
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        if (user.getUsername ()==null||user.getUsername ().length ()==0){
            return "addUserFailure";
        }
        AdminService adminService = new IAdminService ( );
        adminService.addUser (user);
        System.out.println (user.toString () );
        return "viewUser";
    }

    public String deleteUser(User user){
        AdminService adminService = new IAdminService ( );
        adminService.deleteUser (user);
        System.out.println (user.toString () );
        return "viewUser";
    }



}
