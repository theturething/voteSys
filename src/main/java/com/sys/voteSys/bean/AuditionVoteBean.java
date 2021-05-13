package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.AuditionVote;
import com.sys.voteSys.pojo.VoteRecord;
import com.sys.voteSys.services.Impl.IAuditionVoteService;
import com.sys.voteSys.services.Impl.ISingleVoteService;
import com.sys.voteSys.util.JudgeTime;
import com.sys.voteSys.util.MySqlConn;
import com.sys.voteSys.util.TimeSplit;


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Zlei
 * @date 2021/5/3  18:52
 */
public class AuditionVoteBean implements Serializable {

    private String beginTime;

    private String endTime;

    private AuditionVote auditionVote;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private ArrayList<AuditionVote> auditionVotes=new ArrayList<> (  );

    public AuditionVote getAuditionVote() {
        return auditionVote;
    }

    public void setAuditionVote(AuditionVote auditionVote) {
        this.auditionVote = auditionVote;
    }

    public ArrayList<AuditionVote> getAuditionVotes() {
        ArrayList<AuditionVote> auditionVotes=new ArrayList<> (  );
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement=conn.prepareStatement ("select * from audition_vote");
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                AuditionVote auditionVote = new AuditionVote ( );
                auditionVote.setVoteId (resultSet.getInt ("vote_id"));
                auditionVote.setTheme (resultSet.getString ("theme"));
                auditionVote.setIntroduction (resultSet.getString ("introduction"));
                auditionVote.setBeginTime (resultSet.getTimestamp ("begin_time"));
                auditionVote.setEndTime (resultSet.getTimestamp ("end_time"));
                auditionVotes.add (auditionVote);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        this.auditionVotes=auditionVotes;
        return this.auditionVotes;
    }

    public void setAuditionVotes(ArrayList<AuditionVote> auditionVotes) {
        this.auditionVotes = auditionVotes;
    }

    public void updateAuditionVote(ActionEvent event){
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance ().getExternalContext ().getRequest ();
        String theme = request.getParameter ("theme");
        IAuditionVoteService iAuditionVoteService = new IAuditionVoteService ( );
        AuditionVote auditionByTheme = iAuditionVoteService.getAuditionByTheme (theme);
        this.auditionVote.setVoteId (auditionByTheme.getVoteId ());
        this.auditionVote.setTheme (auditionByTheme.getTheme ());
        this.auditionVote.setIntroduction (auditionByTheme.getIntroduction ());
        this.auditionVote.setBeginTime (auditionByTheme.getBeginTime ());
        this.auditionVote.setEndTime (auditionByTheme.getEndTime ());
        System.out.println (this.auditionVote );

    }

    public String updateAuditionVote(){
        IAuditionVoteService iAuditionVoteService = new IAuditionVoteService ( );
        AuditionVote auditionByTheme = iAuditionVoteService.getAuditionByTheme (auditionVote.getTheme ( ));
        iAuditionVoteService.deleteAudition (auditionByTheme);
        iAuditionVoteService.addAuditionVote (auditionVote);
        return "updateSuccess";
    }

    public String deleteAuditionVote(AuditionVote auditionVote){
        IAuditionVoteService iAuditionVoteService = new IAuditionVoteService ( );
        iAuditionVoteService.deleteAudition (auditionVote);
        System.out.println (auditionVote );
        return "auditionVoteList";
    }

    public String addAuditionVote(){
        IAuditionVoteService iAuditionVoteService = new IAuditionVoteService ( );
        //查重
        AuditionVote auditionByTheme = iAuditionVoteService.getAuditionByTheme (auditionVote.getTheme ( ));

        if (auditionByTheme!=null){
            return "addFailure";
        }
        auditionVote.setBeginTime (Timestamp.valueOf (beginTime));
        auditionVote.setEndTime ( Timestamp.valueOf (endTime));
        iAuditionVoteService.addAuditionVote (auditionVote);
        return "addSuccess";
    }

    public void voteAudition(ActionEvent event){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance ( ).getExternalContext ( ).getRequest ( );
        String theme = request.getParameter ("theme");
        IAuditionVoteService iAuditionVoteService = new IAuditionVoteService ( );
        AuditionVote auditionByTheme = iAuditionVoteService.getAuditionByTheme (theme);
        this.auditionVote.setIntroduction (auditionByTheme.getIntroduction ());
        this.auditionVote.setTheme (auditionByTheme.getTheme ());
        this.auditionVote.setBeginTime (auditionByTheme.getBeginTime ());
        this.auditionVote.setEndTime (auditionByTheme.getEndTime ());
    }

    public String voteAudition(String content,AuditionVote auditionVote){
        // 判断投票时间是否合适
        boolean judge = JudgeTime.judge1 (auditionVote.getBeginTime ( ), auditionVote.getEndTime ( ), new Timestamp (System.currentTimeMillis ( )));
        if (!judge){
            return "timeNot";
        }

        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        VoteRecord voteRecord = new VoteRecord ( );
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSessionMap ( );
        LoginBean loginBean = (LoginBean) sessionMap.get ("loginBean");
        voteRecord.setUsername (loginBean.getUsername ());
        voteRecord.setVoteMode (2);
        voteRecord.setVoteTime (new Timestamp (System.currentTimeMillis ()));
        voteRecord.setChoice (content);
        voteRecord.setTheme (auditionVote.getTheme ());
        //判断用户是否已经投过该主题的票，若未，则可投，否则拒绝投票
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = conn.prepareStatement ("select * from vote_record where username=? and vote_mode=? and theme=?" );
            preparedStatement.setString (1,loginBean.getUsername ());
            preparedStatement.setInt (2,2);
            preparedStatement.setString (3,auditionVote.getTheme ());
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (resultSet.next ()){
                return "failureVote";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }

        iSingleVoteService.insertRecord(voteRecord);

        return "voteAuditionSuccess";
    }

}
