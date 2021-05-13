package com.sys.voteSys.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Zlei
 * @date 2021/4/26  16:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiVote implements Serializable {

    private int voteId;

    //选票主题
    private String theme;

    //选票简介
    private String introduction;

    //选项总数
    private int choiceNum;

    //选几个
    private int chooseAble;

    private Timestamp beginTime;

    private Timestamp endTime;

}
