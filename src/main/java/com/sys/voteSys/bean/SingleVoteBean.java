package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.SingleChoice;
import com.sys.voteSys.pojo.SingleVote;
import com.sys.voteSys.pojo.VoteRecord;
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
 * @date 2021/4/29  20:52
 */
public class SingleVoteBean implements Serializable {

    private String contents=new String (  );

    private String beginTime;

    private String endTime;

    private SingleVote singleVote;

    private SingleChoice singleChoice;

    private ArrayList singleVotes=new ArrayList (  );

    private ArrayList singleChoices=new ArrayList (  );

    private ArrayList<String> choices=new ArrayList<> (  );

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
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

    public ArrayList<String> getChoices() {
        ArrayList<String> strings = new ArrayList<> ( );
        for (int i = 0; i < this.singleVote.getChoiceNum (); i++) {
            strings.add ("选项"+(i+1));
        }
        this.choices=strings;
        return choices;
    }

    public SingleVote getSingleVote() {
        return singleVote;
    }

    public void setSingleVote(SingleVote singleVote) {
        this.singleVote = singleVote;
    }

    public SingleChoice getSingleChoice() {
        return singleChoice;
    }

    public void setSingleChoice(SingleChoice singleChoice) {
        this.singleChoice = singleChoice;
    }

    public ArrayList getSingleVotes() throws SQLException {
        ArrayList<SingleVote> singleVotes =new ArrayList<> (  );
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement = conn.prepareStatement ("select * from single_vote");
        ResultSet resultSet = preparedStatement.executeQuery ( );
        while (resultSet.next ()){
            SingleVote singleVote = new SingleVote ( );
            singleVote.setVoteId (resultSet.getInt ("vote_id"));
            singleVote.setTheme (resultSet.getString ("theme"));
            singleVote.setIntroduction (resultSet.getString ("introduction"));
            singleVote.setBeginTime (resultSet.getTimestamp ("begin_time"));
            singleVote.setEndTime (resultSet.getTimestamp ("end_time"));
            singleVotes.add (singleVote);

        }
        this.singleVotes=singleVotes;
        return this.singleVotes;
    }

    public void setSingleVotes(ArrayList singleVotes) {
        this.singleVotes = singleVotes;
    }

    public ArrayList getSingleChoices() {

        return singleChoices;
    }

    public void setSingleChoices(ArrayList singleChoices) {
        this.singleChoices = singleChoices;
    }

    public String voteSingle(SingleVote singleVote,SingleChoice singleChoice){
        //  判断投票时间是否合适
        boolean judge = JudgeTime.judge1 (singleVote.getBeginTime ( ), singleVote.getEndTime ( ), new Timestamp (System.currentTimeMillis ()));
        if (!judge){
            return "timeNot";
        }

        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        VoteRecord voteRecord = new VoteRecord ( );
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSessionMap ( );
        LoginBean loginBean = (LoginBean) sessionMap.get ("loginBean");
        voteRecord.setUsername (loginBean.getUsername ());
        voteRecord.setVoteMode (0);
        voteRecord.setVoteTime (new Timestamp (System.currentTimeMillis ()));
        voteRecord.setChoice (singleChoice.getContent ());
        voteRecord.setTheme (singleVote.getTheme ());
        System.out.println (voteRecord.toString () );
        //判断用户是否已经投过该主题的票，若投过，则反馈相关信息，不让投票
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement=conn.prepareStatement ("select * from vote_record where username=? and vote_mode=? and theme=? ");
            preparedStatement.setString (1,loginBean.getUsername ());
            preparedStatement.setInt (2,0);
            preparedStatement.setString (3,singleVote.getTheme ());
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (resultSet.next ()){
                return "failureVote";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }
        //更新该选项被投票总数
        try {
            PreparedStatement preparedStatement1=conn.prepareStatement ("update single_choice set vote_total=vote_total+1 where theme=? and content=?");
            preparedStatement1.setString (1,voteRecord.getTheme ());
            preparedStatement1.setString (2,voteRecord.getChoice ());
            preparedStatement1.executeUpdate ();
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }


        iSingleVoteService.insertRecord (voteRecord);
        return "voteSingleSuccess";
    }

    public void voteSingle(ActionEvent event){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance ( ).getExternalContext ( ).getRequest ( );
        String theme = request.getParameter ("theme");
        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        SingleVote singleVoteByTheme = iSingleVoteService.getSingleVoteByTheme (theme);
        System.out.println (singleVoteByTheme );
        ArrayList<SingleChoice> singleChoicesByTheme = iSingleVoteService.getSingleChoicesByTheme (theme);
        this.singleVote.setTheme (singleVoteByTheme.getTheme ());
        this.singleVote.setIntroduction (singleVoteByTheme.getIntroduction ());
        this.singleVote.setVoteId (singleVoteByTheme.getVoteId ());
        this.singleVote.setBeginTime (singleVoteByTheme.getBeginTime ());
        this.singleVote.setEndTime (singleVoteByTheme.getEndTime ());
        this.singleChoices=singleChoicesByTheme;
    }

    public void updateSingleVote(ActionEvent event){
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance ().getExternalContext ().getRequest ();
        String theme = request.getParameter ("theme");
        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        SingleVote singleVoteByTheme = iSingleVoteService.getSingleVoteByTheme (theme);
        this.singleVote=SingleVote.builder ()
                .voteId (singleVoteByTheme.getVoteId ())
                .theme (singleVoteByTheme.getTheme ())
                .beginTime (singleVoteByTheme.getBeginTime ())
                .choiceNum (singleVoteByTheme.getChoiceNum ())
                .endTime (singleVoteByTheme.getEndTime ())
                .introduction (singleVoteByTheme.getIntroduction ())
                .build ();
        System.out.println (this.singleVote );
        ArrayList<SingleChoice> singleChoicesByTheme = iSingleVoteService.getSingleChoicesByTheme (theme);
        String tempContents = "";
        for (SingleChoice singlechoice:singleChoicesByTheme) {
            tempContents=tempContents + singlechoice.getContent () + ",";
        }
        contents=tempContents.substring (0,tempContents.length ()-1);
        beginTime=singleVote.getBeginTime ().toString ();
        endTime=singleVote.getEndTime ().toString ();
        System.out.println (contents );
    }

    public String updateSingleVote(){
        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        SingleVote singleVoteByTheme = iSingleVoteService.getSingleVoteByTheme (this.singleVote.getTheme ( ));
        iSingleVoteService.deleteSingleVote (singleVote);
        String[] split = contents.split (",");
        ArrayList<SingleChoice> singleChoices = new ArrayList<> ( );
        for (int i = 0; i < split.length; i++) {
            SingleChoice singleChoice = new SingleChoice ( );
            singleChoice.setTheme (singleVote.getTheme ());
            singleChoice.setContent (split[i]);
            singleChoices.add (singleChoice);
        }
        System.out.println (this.singleChoices.toString () );
        iSingleVoteService.insertSingleVote (singleVote,singleChoices);
        return "updateSuccess";
    }

    public String deleteSingleVote(SingleVote singleVote){
        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        iSingleVoteService.deleteSingleVote (singleVote);
        System.out.println (singleVote.toString () );
        return "singleVoteList";
    }

    public String addSingleVoteChoices(SingleVote singleVote){
        System.out.println (singleVote.toString () );
        this.singleVote.setTheme (singleVote.getTheme ());
        this.singleVote.setIntroduction (singleVote.getIntroduction ());
        this.singleVote.setChoiceNum (singleVote.getChoiceNum ());
        this.singleVote.setBeginTime (singleVote.getBeginTime ());
        this.singleVote.setEndTime (singleVote.getEndTime ());
        System.out.println (this.singleVote.toString ());
        return "addSingleVoteChoices";
    }


    public String addSingleVote(){
        singleVote.setBeginTime (Timestamp.valueOf (beginTime));
        singleVote.setEndTime (Timestamp.valueOf (endTime));
        String[] split = contents.split (",");
        ArrayList<SingleChoice> singleChoices = new ArrayList<> ( );
        for (int i = 0; i < split.length; i++) {
            SingleChoice singleChoice = new SingleChoice ( );
            singleChoice.setTheme (singleVote.getTheme ());
            singleChoice.setContent (split[i]);
            singleChoices.add (singleChoice);
        }
        if(singleVote.getChoiceNum ()!=singleChoices.size ()){
            return "addFailure";
        }
        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        // 查重
        SingleVote singleVoteByTheme = iSingleVoteService.getSingleVoteByTheme (singleVote.getTheme ( ));
        if (singleVoteByTheme!=null){
            return "addFailure";
        }
        iSingleVoteService.insertSingleVote (singleVote, singleChoices);
        return "addSuccess";
    }


}
