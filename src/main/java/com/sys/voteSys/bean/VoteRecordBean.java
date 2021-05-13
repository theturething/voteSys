package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.VoteRecord;
import com.sys.voteSys.util.MySqlConn;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Zlei
 * @date 2021/5/6  14:03
 */
public class VoteRecordBean implements Serializable {

    private String logger;

    private VoteRecord voteRecord;

    public String getLogger() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSessionMap ( );
        LoginBean loginBean = (LoginBean) sessionMap.get ("loginBean");
        boolean admin = loginBean.isAdmin ( );
        if (admin){
            return "管理员";
        }
        return "用户";
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    private ArrayList<VoteRecord> auditionList = new ArrayList<> (  );

    private ArrayList<VoteRecord> multiList= new ArrayList<> (  );

    private ArrayList<VoteRecord> singleList= new ArrayList<> (  );

    public VoteRecord getVoteRecord() {
        return voteRecord;
    }

    public void setVoteRecord(VoteRecord voteRecord) {
        this.voteRecord = voteRecord;
    }

    public ArrayList<VoteRecord> getAuditionList() {
        ArrayList<VoteRecord> record = this.getRecord (2);
        auditionList=record;
        return auditionList;
    }

    public void setAuditionList(ArrayList<VoteRecord> auditionList) {
        this.auditionList = auditionList;
    }

    public ArrayList<VoteRecord> getMultiList() {
        ArrayList<VoteRecord> record = this.getRecord (1);
        multiList=record;
        return multiList;
    }

    public void setMultiList(ArrayList<VoteRecord> multiList) {
        this.multiList = multiList;
    }

    public ArrayList<VoteRecord> getSingleList() {
        ArrayList<VoteRecord> record = this.getRecord (0);
        singleList=record;
        return singleList;
    }

    public void setSingleList(ArrayList<VoteRecord> singleList) {
        this.singleList = singleList;
    }

    public String back(){
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSessionMap ( );
        LoginBean loginBean = (LoginBean) sessionMap.get ("loginBean");
        boolean admin = loginBean.isAdmin ( );
        if (admin){
            return "admin";
        }
        return "user";
    }

    public ArrayList<VoteRecord> getRecord(int voteMode){
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSessionMap ( );
        LoginBean loginBean = (LoginBean) sessionMap.get ("loginBean");
        boolean admin = loginBean.isAdmin ( );
        String username = loginBean.getUsername ( );
        ArrayList<VoteRecord> voteRecords = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            if (admin){
                preparedStatement=conn.prepareStatement ("select * from vote_record where vote_mode=?");
                preparedStatement.setInt (1,voteMode);
            }else {
                preparedStatement= conn.prepareStatement ("select * from vote_record where vote_mode=? and username = ?" );
                preparedStatement.setInt (1,voteMode);
                preparedStatement.setString (2,username);
            }
            resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                VoteRecord voteRecord = new VoteRecord ( );
                voteRecord.setId (resultSet.getInt ("id"));
                voteRecord.setUsername (resultSet.getString ("username"));
                voteRecord.setVoteMode (resultSet.getInt ("vote_mode"));
                voteRecord.setVoteTime (resultSet.getTimestamp ("vote_time"));
                voteRecord.setChoice (resultSet.getString ("choice"));
                voteRecord.setTheme (resultSet.getString ("theme"));
                voteRecords.add (voteRecord);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        return voteRecords;
    }

}
