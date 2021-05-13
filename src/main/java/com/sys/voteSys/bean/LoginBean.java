package com.sys.voteSys.bean;

import com.sys.voteSys.util.MySqlConn;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Zlei
 * @date 2021/4/23  15:31
 */
public class LoginBean implements Serializable {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    private boolean loggedIn;

    private boolean admin;

    public String loginSys() throws SQLException {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        if (admin ==false){
            preparedStatement = conn.prepareStatement ("select  * from user ");
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                if (username.equals (resultSet.getString ("username"))&&
                        password.equals (resultSet.getString ("password"))){
                    loggedIn=true;
                    return "successUser";
                }
            }
        }else {
            preparedStatement = conn.prepareStatement ("select  * from manager ");
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                if (username.equals (resultSet.getString ("username"))&&
                        password.equals (resultSet.getString ("password"))){
                    loggedIn=true;
                    return "successAdmin";
                }
            }
        }
        loggedIn=false;
        return "failure";
    }

    public String logoutSys(ActionEvent event){
        loggedIn=false;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSession (true);
        session.setAttribute ("loginBean",null);
        return "logoutSys";
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
