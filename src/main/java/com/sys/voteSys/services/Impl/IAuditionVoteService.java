package com.sys.voteSys.services.Impl;

import com.sys.voteSys.pojo.AuditionVote;
import com.sys.voteSys.services.AuditionVoteService;
import com.sys.voteSys.util.MySqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Zlei
 * @date 2021/5/3  20:56
 */
public class IAuditionVoteService implements AuditionVoteService {

    @Override
    public void addAuditionVote(AuditionVote auditionVote) {
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement=conn.prepareStatement ("insert into audition_vote(theme,introduction,begin_time,end_time)values (?,?,?,?)");
            preparedStatement.setString (1,auditionVote.getTheme ());
            preparedStatement.setString (2,auditionVote.getIntroduction ());
            preparedStatement.setTimestamp (3,auditionVote.getBeginTime ());
            preparedStatement.setTimestamp (4,auditionVote.getEndTime ());
            preparedStatement.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }

    @Override
    public AuditionVote getAuditionByTheme(String theme) {
        Connection conn = MySqlConn.getConn ( );
        AuditionVote auditionVote = new AuditionVote ( );
        try {
            PreparedStatement preparedStatement = conn.prepareStatement ("select * from audition_vote where theme=?");
            preparedStatement.setString (1,theme);
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (!resultSet.next ()){
                return null;
            }

                auditionVote.setVoteId (resultSet.getInt ("vote_id"));
                auditionVote.setTheme (theme);
                auditionVote.setBeginTime (resultSet.getTimestamp ("begin_time"));
                auditionVote.setEndTime (resultSet.getTimestamp ("end_time"));
                auditionVote.setIntroduction (resultSet.getString ("introduction"));

        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        return auditionVote;
    }

    @Override
    public void deleteAudition(AuditionVote auditionVote) {
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=conn.prepareStatement ("delete from audition_vote where theme = ?");
            preparedStatement.setString (1,auditionVote.getTheme ());
            int i = preparedStatement.executeUpdate ( );
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
    }
}
