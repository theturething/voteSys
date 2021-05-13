package com.sys.voteSys.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * 单选票的每个选项
 * @author Zlei
 * @date 2021/4/26  16:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleChoice implements Serializable {

    private int choiceId;

    private String theme;

    //选项内容
    private String content;

    private int voteTotal;

}
