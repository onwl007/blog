package com.onwl007.blog.service;

import com.onwl007.blog.domain.Comment;

/**
 * Comment 服务接口
 * @author onwl007@126.com
 * @date 2017/10/26 20:50
 */
public interface CommentService {

    /**
     * 根据id获取Comment
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     * 根据id删除Comment
     * @param id
     */
    void removeComment(Long id);

}
