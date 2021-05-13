package com.sys.voteSys.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Zlei
 * @date 2021/4/26  16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiChoice implements Serializable {

    private int choiceId;

    private String theme;

    //选项内容
    private String content;

    private int voteTotal;

    private boolean isSelected=false;

}
