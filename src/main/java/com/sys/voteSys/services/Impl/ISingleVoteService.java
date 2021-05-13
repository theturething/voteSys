package com.sys.voteSys.services.Impl;

import com.sys.voteSys.pojo.SingleChoice;
import com.sys.voteSys.pojo.SingleVote;
import com.sys.voteSys.pojo.VoteRecord;
import com.sys.voteSys.services.SingleVoteService;
import com.sys.voteSys.util.MySqlConn;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Zlei
 * @date 2021/4/30  20:02
 */
public class ISingleVoteService implements SingleVoteService {

    @Override
    public void deleteSingleVote(SingleVote singleVote) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        PreparedStatement preparedStatement1=null;
        //先删除选项
        try {
            preparedStatement=conn.prepareStatement ("delete from single_choice where theme=?");
            preparedStatement.setString (1,singleVote.getTheme ());
            int i = preparedStatement.executeUpdate ( );
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        //删除投票基本信息
        try {
            preparedStatement1=conn.prepareStatement ("delete from single_vote where theme=?");
            preparedStatement1.setString (1,singleVote.getTheme ());
            int i = preparedStatement1.executeUpdate ( );
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }



    @Override
    public SingleVote getSingleVoteByTheme(String theme) {
        SingleVote singleVote = new SingleVote ( );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement=conn.prepareStatement ("select * from single_vote where theme = ?");
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (!resultSet.next ()){
                return null;
            }
            singleVote.setVoteId (resultSet.getInt ("vote_id"));
            singleVote.setTheme (resultSet.getString ("theme"));
            singleVote.setIntroduction (resultSet.getString ("introduction"));
            singleVote.setBeginTime (resultSet.getTimestamp ("begin_time"));
            singleVote.setEndTime (resultSet.getTimestamp ("end_time"));
            singleVote.setChoiceNum (resultSet.getInt ("choice_num"));

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        return singleVote;
    }


    @Override
    public ArrayList<SingleChoice> getSingleChoicesByTheme(String theme) {
        ArrayList<SingleChoice> singleChoices=new ArrayList<> (  );
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;

        try {
            preparedStatement = conn.prepareStatement ("select * from single_choice where theme=?");
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                SingleChoice singleChoice = new SingleChoice ( );
                singleChoice=SingleChoice.builder ()
                        .choiceId (resultSet.getInt ("choice_id"))
                        .theme (resultSet.getString ("theme"))
                        .content (resultSet.getString ("content"))
                        .voteTotal (resultSet.getInt ("vote_total"))
                        .build ();
                singleChoices.add (singleChoice);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        return singleChoices;
    }


    @Override
    public void insertRecord(VoteRecord voteRecord) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=conn.prepareStatement ("insert into vote_record (username,vote_mode,vote_time,choice,theme)values (?,?,?,?,?)");
            preparedStatement.setString (1,voteRecord.getUsername ());
            preparedStatement.setInt (2,voteRecord.getVoteMode ());
            preparedStatement.setTimestamp (3,voteRecord.getVoteTime ());
            preparedStatement.setString (4,voteRecord.getChoice ());
            preparedStatement.setString (5,voteRecord.getTheme ());
            int i = preparedStatement.executeUpdate ( );
            System.out.println (voteRecord.getVoteTime () );
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }

    @Override
    public void insertSingleVote(SingleVote singleVote, ArrayList<SingleChoice> singleChoices) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=conn.prepareStatement ("insert into single_vote (theme,introduction,begin_time,end_time,choice_num)values (?,?,?,?,?)");
            preparedStatement.setString (1,singleVote.getTheme ());
            preparedStatement.setString (2,singleVote.getIntroduction ());
            preparedStatement.setTimestamp (3,singleVote.getBeginTime ());
            preparedStatement.setTimestamp (4,singleVote.getEndTime ());
            preparedStatement.setInt (5,singleVote.getChoiceNum ());
            preparedStatement.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        for (SingleChoice singleChoice: singleChoices) {
            try {
                PreparedStatement preparedStatement1= conn.prepareStatement ("insert into single_choice (theme,content)values (?,?)" );
                preparedStatement1.setString (1,singleChoice.getTheme ());
                preparedStatement1.setString (2,singleChoice.getContent ());
                preparedStatement1.executeUpdate ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
        }
    }
}
