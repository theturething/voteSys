package com.sys.voteSys.bean;

import com.sys.voteSys.pojo.MultiChoice;
import com.sys.voteSys.pojo.MultiVote;
import com.sys.voteSys.pojo.VoteRecord;
import com.sys.voteSys.services.Impl.IMultiVoteService;
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
 * @date 2021/5/4  15:41
 */
public class MultiVoteBean implements Serializable {

    private String beginTime;

    private String endTime;

    private String contents;

    private MultiVote multiVote;

    private ArrayList<MultiVote> multiVotes;

    private MultiChoice multiChoice;

    private ArrayList<MultiChoice> multiChoices;

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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public MultiVote getMultiVote() {
        return multiVote;
    }

    public void setMultiVote(MultiVote multiVote) {
        this.multiVote = multiVote;
    }

    public ArrayList<MultiVote> getMultiVotes() throws SQLException {
        ArrayList<MultiVote> multiVotes = new ArrayList<> ( );
        Connection conn = MySqlConn.getConn ( );
        PreparedStatement preparedStatement = conn.prepareStatement ("select * from multi_vote");
        ResultSet resultSet = preparedStatement.executeQuery ( );
        while (resultSet.next ()){
            MultiVote multiVote = new MultiVote ( );
            multiVote.setVoteId (resultSet.getInt ("vote_id"));
            multiVote.setTheme (resultSet.getString ("theme"));
            multiVote.setIntroduction (resultSet.getString ("introduction"));
            multiVote.setBeginTime (resultSet.getTimestamp ("begin_time"));
            multiVote.setEndTime (resultSet.getTimestamp  ("end_time"));
            multiVote.setChoiceNum (resultSet.getInt ("choice_num"));
            multiVote.setChooseAble (resultSet.getInt ("choose_able"));
            multiVotes.add (multiVote);
        }
        this.multiVotes=multiVotes;
        return this.multiVotes;
    }

    public void setMultiVotes(ArrayList<MultiVote> multiVotes) {
        this.multiVotes = multiVotes;
    }

    public MultiChoice getMultiChoice() {
        return multiChoice;
    }

    public void setMultiChoice(MultiChoice multiChoice) {
        this.multiChoice = multiChoice;
    }

    public ArrayList<MultiChoice> getMultiChoices() {
        return multiChoices;
    }

    public void setMultiChoices(ArrayList<MultiChoice> multiChoices) {
        this.multiChoices = multiChoices;
    }

    public void updateMultiVote(ActionEvent event){
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance ().getExternalContext ().getRequest ();
        String theme = request.getParameter ("theme");
        IMultiVoteService iMultiVoteService = new IMultiVoteService ( );
        MultiVote multiVoteByTheme = iMultiVoteService.getMultiVoteByTheme (theme);
        this.multiVote.setVoteId (multiVoteByTheme.getVoteId ());
        this.multiVote.setIntroduction (multiVoteByTheme.getIntroduction ());
        this.multiVote.setTheme (multiVoteByTheme.getTheme ());
        this.multiVote.setChooseAble (multiVoteByTheme.getChooseAble ());
        this.multiVote.setChoiceNum (multiVoteByTheme.getChoiceNum ());
        this.multiVote.setBeginTime (multiVoteByTheme.getBeginTime ());
        this.multiVote.setEndTime (multiVoteByTheme.getEndTime ());
        System.out.println (this.multiVote );
        ArrayList<MultiChoice> multiChoiceByTheme = iMultiVoteService.getMultiChoiceByTheme (theme);
        String tempContents="";
        for (MultiChoice multiChoice:multiChoiceByTheme) {
            tempContents=tempContents+multiChoice.getContent ()+",";
        }
        this.contents=tempContents.substring (0,tempContents.length ()-1);
        this.beginTime=multiVoteByTheme.getBeginTime ().toString ();
        this.endTime=multiVoteByTheme.getEndTime ().toString ();
        System.out.println (this.contents );
    }

    public String updateMultiVote(){
        IMultiVoteService iMultiVoteService = new IMultiVoteService ( );
        MultiVote multiVoteByTheme = iMultiVoteService.getMultiVoteByTheme (this.multiVote.getTheme ( ));
        iMultiVoteService.deleteMultiVote (multiVote);
        String[] split = contents.split (",");
        ArrayList<MultiChoice> multiChoices = new ArrayList<> ( );
        for (int i = 0; i < split.length; i++) {
            MultiChoice multiChoice = new MultiChoice ( );
            multiChoice.setTheme (this.multiVote.getTheme ());
            multiChoice.setContent (split[i]);
            multiChoices.add (multiChoice);
        }
        iMultiVoteService.insertMultiVote (multiVote,multiChoices);
        return "updateSuccess";
    }

    public String deleteMultiVote(MultiVote multiVote){
        IMultiVoteService iMultiVoteService = new IMultiVoteService ( );
        iMultiVoteService.deleteMultiVote (multiVote);
        System.out.println (multiVote.toString () );
        return "multiVoteList";
    }

    public String addMultiVote(){
        multiVote.setBeginTime (Timestamp.valueOf (beginTime));
        multiVote.setEndTime (Timestamp.valueOf (endTime));
        String[] split = contents.split (",");
        ArrayList<MultiChoice> multiChoices=new ArrayList<> (  );
        for (int i = 0; i < split.length; i++) {
            MultiChoice multiChoice = new MultiChoice ( );
            multiChoice.setTheme (multiVote.getTheme ());
            multiChoice.setContent (split[i]);
            multiChoices.add (multiChoice);
        }
        IMultiVoteService iMultiVoteService = new IMultiVoteService ( );
        // 查重
        MultiVote multiVoteByTheme = iMultiVoteService.getMultiVoteByTheme (multiVote.getTheme ( ));
        if (multiVoteByTheme!=null){
            return "addFailure";
        }
        iMultiVoteService.insertMultiVote (multiVote,multiChoices);
        return "addMultiVoteSuccess";
    }

    public void voteMulti(ActionEvent event){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance ( ).getExternalContext ( ).getRequest ( );
        String theme = request.getParameter ("theme");
        IMultiVoteService iMultiVoteService = new IMultiVoteService ( );
        MultiVote multiVoteByTheme = iMultiVoteService.getMultiVoteByTheme (theme);
        ArrayList<MultiChoice> multiChoiceByTheme = iMultiVoteService.getMultiChoiceByTheme (theme);
        this.multiVote.setTheme (multiVoteByTheme.getTheme ());
        this.multiVote.setIntroduction (multiVoteByTheme.getIntroduction ());
        this.multiVote.setVoteId (multiVoteByTheme.getVoteId ());
        this.multiVote.setBeginTime (multiVoteByTheme.getBeginTime ());
        this.multiVote.setEndTime (multiVoteByTheme.getEndTime ());
        this.multiVote.setChooseAble (multiVoteByTheme.getChooseAble ());
        this.multiChoices=multiChoiceByTheme;
    }

    /***
     *
     * @param multiVote
     * @return
     */
    public String voteMulti(MultiVote multiVote){
        // 判断投票时间是否合适
        boolean judge = JudgeTime.judge1 (multiVote.getBeginTime ( ), multiVote.getEndTime ( ), new Timestamp (System.currentTimeMillis ( )));
        if (!judge){
            return "timeNot";
        }

        ISingleVoteService iSingleVoteService = new ISingleVoteService ( );
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance ( ).getExternalContext ( ).getSessionMap ( );
        LoginBean loginBean = (LoginBean) sessionMap.get ("loginBean");

        //判断用户是否已经投过该主题的票，若投过，则反馈相关信息，不让投票
        Connection conn = MySqlConn.getConn ( );
        try {
            PreparedStatement preparedStatement=conn.prepareStatement ("select * from vote_record where username=? and vote_mode=? and theme=?");
            preparedStatement.setString (1,loginBean.getUsername ());
            preparedStatement.setInt (2,1);
            preparedStatement.setString (3,multiVote.getTheme ());
            ResultSet resultSet = preparedStatement.executeQuery ( );
            if (resultSet.next ()){
                return "failureVote";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }

        //判断是否投了正确的选项个数
        int countSelect=0;
        for (MultiChoice multiChoice:multiChoices){
            if (multiChoice.isSelected ( )){
                countSelect+=1;
            }
        }
        System.out.println (multiVote.getChooseAble () +"         "+countSelect);
        if (multiVote.getChooseAble ()!= countSelect){
            return "failureVote";
        }
        //进行投票操作
        for (MultiChoice multiChoice:multiChoices) {
            VoteRecord voteRecord = new VoteRecord ( );
            voteRecord.setTheme (multiVote.getTheme ());
            voteRecord.setUsername (loginBean.getUsername ());
            voteRecord.setVoteMode (1);
            voteRecord.setVoteTime (new Timestamp (System.currentTimeMillis ()));
            if (multiChoice.isSelected ()==true){
                voteRecord.setChoice (multiChoice.getContent ());
            }else {
                continue;
            }

            //更新该选项被投票总数
            PreparedStatement preparedStatement1= null;
            try {
                preparedStatement1 = conn.prepareStatement ("update multi_choice set vote_total=vote_total+1 where theme=? and content=?");
                preparedStatement1.setString (1,voteRecord.getTheme ());
                preparedStatement1.setString (2,voteRecord.getChoice ());
                preparedStatement1.executeUpdate ();
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            System.out.println (voteRecord );
            iSingleVoteService.insertRecord (voteRecord);
        }

        return "voteMultiSuccess";
    }

}
