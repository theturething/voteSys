package com.sys.voteSys.pojo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 海选基本信息
 * @author Zlei
 * @date 2021/4/29  20:43
 */
@Data
public class AuditionVote implements Serializable {

    private int voteId;

    private String theme;

    private String introduction;

    private Timestamp beginTime;

    private Timestamp endTime;


}
