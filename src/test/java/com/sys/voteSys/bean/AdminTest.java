package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Zlei
 * @date 2021/4/27  9:58
 */
public class AdminTest {


    @Test
    public void viewUserTest() throws SQLException {
        AdminBean adminBean = new AdminBean ( );
        ArrayList<User> users = adminBean.getUsers ();
        System.out.println (users.get (0).toString () );
    }



}
