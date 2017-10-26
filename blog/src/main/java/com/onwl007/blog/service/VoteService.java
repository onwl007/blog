package com.onwl007.blog.service;

import com.onwl007.blog.domain.Vote;

/**
 * Vote 服务接口
 *
 * @author onwl007@126.com
 * @date 2017/10/26 21:00
 */
public interface VoteService {

    /**
     * 根据id获取Vote
     *
     * @param id
     * @return
     */
    Vote getVoteById(Long id);

    /**
     * 根据id删除Vote
     *
     * @param id
     */
    void removeVote(Long id);

}
