package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.User;
import com.sys.voteSys.util.MySqlConn;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Zlei
 * @date 2021/4/24  13:26
 */
@ManagedBean
@SessionScoped
public class RegisterBean implements Serializable {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String register() throws SQLException {
        FacesMessage facesMessage=null;
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        if (user.getPassword ().length ()<6||user.getPassword ().length ()>20){
            facesMessage = new FacesMessage ("请把密码设置在6-20个字符之内", "ERROR MSG");
            facesMessage.setSeverity (FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance ().addMessage (null,facesMessage);
            return "register";
        }
        else {
            preparedStatement=conn.prepareStatement ("select username from user");
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                if (user.getUsername ().equals (resultSet.getString ("username"))){
                    facesMessage = new FacesMessage ("该用户名已被注册，请换一个用户名", "ERROR MSG");
                    facesMessage.setSeverity (FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance ().addMessage (null,facesMessage);
                    return "register";
                }
            }
            preparedStatement=conn.prepareStatement ("insert into user (username,password,sex,age,email,phone) values(?,?,?,?,?,?) ");
            preparedStatement.setString (1, user.getUsername ( ));
            preparedStatement.setString (2, user.getPassword ( ));
            preparedStatement.setString (3,user.getSex ());
            preparedStatement.setInt (4,user.getAge ());
            preparedStatement.setString (5,user.getEmail ());
            preparedStatement.setString (6,user.getPhone ());
            preparedStatement.executeUpdate ();
            return "login";
        }

    }
}
