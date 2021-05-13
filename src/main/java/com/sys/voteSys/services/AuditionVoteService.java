package com.sys.voteSys.services;

import com.sys.voteSys.pojo.AuditionVote;

/**
 * @author Zlei
 * @date 2021/5/3  20:55
 */
public interface AuditionVoteService {

    public void addAuditionVote(AuditionVote auditionVote);

    public AuditionVote getAuditionByTheme(String theme);

    public void deleteAudition(AuditionVote auditionVote);

}
