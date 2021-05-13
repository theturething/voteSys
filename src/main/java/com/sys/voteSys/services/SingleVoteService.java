package com.sys.voteSys.services;

import com.sys.voteSys.pojo.SingleChoice;
import com.sys.voteSys.pojo.SingleVote;
import com.sys.voteSys.pojo.User;
import com.sys.voteSys.pojo.VoteRecord;

import java.util.ArrayList;

/**
 * @author Zlei
 * @date 2021/4/30  20:00
 */
public interface SingleVoteService {

    //删除单选投票
    public void deleteSingleVote(SingleVote singleVote);

    //根据theme获取投票信息
    public SingleVote getSingleVoteByTheme(String theme);

    //根据theme获取投票选项
    public ArrayList<SingleChoice> getSingleChoicesByTheme(String theme);

    //插入投票记录
    //三种投票公用的这个投票记录插入方法
    public void insertRecord(VoteRecord voteRecord);

    public void insertSingleVote(SingleVote singleVote,ArrayList<SingleChoice> singleChoices);

}
