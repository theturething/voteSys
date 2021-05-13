package com.sys.voteSys.services.Impl;

import com.sys.voteSys.pojo.MultiChoice;
import com.sys.voteSys.pojo.MultiVote;
import com.sys.voteSys.services.MultiVoteService;
import com.sys.voteSys.util.MySqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Zlei
 * @date 2021/5/4  18:30
 */
public class IMultiVoteService implements MultiVoteService {

    @Override
    public void insertMultiVote(MultiVote multiVote, ArrayList<MultiChoice> multiChoices) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement= conn.prepareStatement ("insert into multi_vote(theme,introduction,begin_time,end_time,choice_num,choose_able)values (?,?,?,?,?,?)" );
            preparedStatement.setString (1,multiVote.getTheme ());
            preparedStatement.setString (2,multiVote.getIntroduction ());
            preparedStatement.setTimestamp (3,multiVote.getBeginTime ());
            preparedStatement.setTimestamp (4,multiVote.getEndTime ());
            preparedStatement.setInt (5,multiVote.getChoiceNum ());
            preparedStatement.setInt (6,multiVote.getChooseAble ());
            preparedStatement.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        for (MultiChoice multiChoice:multiChoices) {
            try {
                PreparedStatement preparedStatement1=conn.prepareStatement ("insert into multi_choice (theme,content)values (?,?)");
                preparedStatement1.setString (1,multiChoice.getTheme ());
                preparedStatement1.setString (2,multiChoice.getContent ());
                preparedStatement1.executeUpdate ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
        }
    }

    @Override
    public void deleteMultiVote(MultiVote multiVote) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        PreparedStatement preparedStatement1=null;
        //先删除选项
        try {
            preparedStatement = conn.prepareStatement ("delete from multi_choice where theme=?" );
            preparedStatement.setString (1,multiVote.getTheme ());
            preparedStatement.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        //删除投票基本信息
        try {
            preparedStatement1= conn.prepareStatement ("delete from multi_vote where theme = ?" );
            preparedStatement1.setString (1,multiVote.getTheme ());
            preparedStatement1.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }

    @Override
    public MultiVote getMultiVoteByTheme(String theme) {
        MultiVote multiVote = new MultiVote ( );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement= conn.prepareStatement ("select * from multi_vote where theme=?" );
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (!resultSet.next ()){
                return null;
            }

            multiVote.setVoteId (resultSet.getInt ("vote_id"));
            multiVote.setTheme (resultSet.getString ("theme"));
            multiVote.setBeginTime (resultSet.getTimestamp ("begin_time"));
            multiVote.setEndTime (resultSet.getTimestamp ("end_time"));
            multiVote.setIntroduction (resultSet.getString ("introduction"));
            multiVote.setChooseAble (resultSet.getInt ("choose_able"));
            multiVote.setChoiceNum (resultSet.getInt ("choice_num"));

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        return multiVote;
    }

    @Override
    public ArrayList<MultiChoice> getMultiChoiceByTheme(String theme) {
        ArrayList<MultiChoice> multiChoices = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement= conn.prepareStatement ("select * from multi_choice where theme=?" );
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
        return multiChoices;
    }
}
