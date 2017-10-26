package com.onwl007.blog.service;

import com.onwl007.blog.domain.Vote;
import com.onwl007.blog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author onwl007@126.com
 * @date 2017/10/26 21:02
 */
@Service
public class VoteServiceImpl implements VoteService{

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }


    @Override
    @Transactional
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
