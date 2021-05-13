package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.AuditionChoice;
import com.sys.voteSys.pojo.MultiChoice;
import com.sys.voteSys.pojo.SingleChoice;
import com.sys.voteSys.pojo.VoteRecord;
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
 * @date 2021/5/6  20:48
 */
public class VoteTotalBean implements Serializable {

    private String theme;

    private ArrayList<SingleChoice> singleChoices=new ArrayList<> (  );

    private ArrayList<MultiChoice> multiChoices = new ArrayList<> (  );

    private ArrayList<AuditionChoice> auditionChoices = new ArrayList<> (  );

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ArrayList<SingleChoice> getSingleChoices() {
        ArrayList<SingleChoice> singleChoices = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement = conn.prepareStatement ("select * from single_choice where theme = ? order by theme,vote_total DESC ");
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                SingleChoice singleChoice = new SingleChoice ( );
                singleChoice.setChoiceId (resultSet.getInt ("choice_id"));
                singleChoice.setTheme (resultSet.getString ("theme"));
                singleChoice.setContent (resultSet.getString ("content"));
                singleChoice.setVoteTotal (resultSet.getInt ("vote_total"));
                singleChoices.add (singleChoice);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        this.singleChoices=singleChoices;
        return this.singleChoices;
    }

    public void setSingleChoices(ArrayList<SingleChoice> singleChoices) {
        this.singleChoices = singleChoices;
    }

    public ArrayList<MultiChoice> getMultiChoices() {
        ArrayList<MultiChoice> multiChoices = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement = conn.prepareStatement ("select * from multi_choice where theme=? order by theme ,vote_total DESC ");
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                MultiChoice multiChoice = new MultiChoice ( );
                multiChoice.setChoiceId (resultSet.getInt ("choice_id"));
                multiChoice.setTheme (resultSet.getString ("theme"));
                multiChoice.setContent (resultSet.getString ("content"));
                multiChoice.setVoteTotal (resultSet.getInt ("vote_total"));
                multiChoices.add (multiChoice);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        this.multiChoices=multiChoices;
        return this.multiChoices;
    }

    public void setMultiChoices(ArrayList<MultiChoice> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public ArrayList<AuditionChoice> getAuditionChoices() {
        ArrayList<AuditionChoice> auditionChoices = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement = conn.prepareStatement ("select theme,choice,count(choice) as vote_total from vote_record where vote_mode=2 and theme = ?group by choice,theme  order by theme DESC");
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                AuditionChoice auditionChoice = new AuditionChoice ( );
                auditionChoice.setTheme (resultSet.getString ("theme"));
                auditionChoice.setContent (resultSet.getString ("choice"));
                auditionChoice.setVoteTotal (resultSet.getInt ("vote_total"));
                auditionChoices.add (auditionChoice);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        this.auditionChoices = auditionChoices;
        return this.auditionChoices;
    }

    public void setAuditionChoices(ArrayList<AuditionChoice> auditionChoices) {
        this.auditionChoices = auditionChoices;
    }

    public void theme1(ActionEvent event){
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance ().getExternalContext ().getRequest ();
        String theme = request.getParameter ("theme1");
        this.theme=theme;
    }

}
