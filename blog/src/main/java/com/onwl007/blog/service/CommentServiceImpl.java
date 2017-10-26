package com.onwl007.blog.service;

import com.onwl007.blog.domain.Comment;
import com.onwl007.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author onwl007@126.com
 * @date 2017/10/26 20:53
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
         return commentRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
