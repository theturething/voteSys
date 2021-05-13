package com.sys.voteSys.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Zlei
 * @date 2021/5/6  21:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditionChoice implements Serializable {

    private String theme;

    private String content;

    private int voteTotal;

}
