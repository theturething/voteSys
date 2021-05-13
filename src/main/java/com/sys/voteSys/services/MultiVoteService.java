package com.sys.voteSys.services;

import com.sys.voteSys.pojo.MultiChoice;
import com.sys.voteSys.pojo.MultiVote;

import java.util.ArrayList;

/**
 * @author Zlei
 * @date 2021/5/4  18:30
 */
public interface MultiVoteService {

    public void insertMultiVote(MultiVote multiVote, ArrayList<MultiChoice> multiChoices);

    //删除多选投票
    public void deleteMultiVote(MultiVote multiVote);

    //根据theme获取投票信息
    public MultiVote getMultiVoteByTheme(String theme);

    //根据theme获取投票选项
    public ArrayList<MultiChoice> getMultiChoiceByTheme(String theme);

}
