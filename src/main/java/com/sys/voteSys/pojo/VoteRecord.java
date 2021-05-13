package com.sys.voteSys.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Zlei
 * @date 2021/4/29  20:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRecord implements Serializable {

    private int id;

    private String username;

    private int voteMode;

    private Timestamp voteTime;

    private String choice;

    private String theme;

}
