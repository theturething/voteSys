package com.sys.voteSys.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 单选选票
 * @author Zlei
 * @date 2021/4/26  16:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleVote implements Serializable {

    private int voteId;

    //选票主题
    private String theme;

    //选票简介
    private String introduction;

    private int choiceNum;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp beginTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp endTime;
}
