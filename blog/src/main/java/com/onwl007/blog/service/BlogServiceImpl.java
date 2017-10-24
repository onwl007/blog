package com.onwl007.blog.service;

import com.onwl007.blog.domain.Blog;
import com.onwl007.blog.domain.Catalog;
import com.onwl007.blog.domain.User;
import com.onwl007.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * blog服务
 */
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;
    //@Autowired
    //private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        return null;
    }

    @Override
    public void removeBlog(Long id) {

    }

    @Override
    public Blog getBlogById(Long id) {
        return null;
    }

    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User suser, String title, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        return null;
    }

    @Override
    public void readingIncrease(Long id) {

    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        return null;
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {

    }

    @Override
    public Blog createVote(Long blogId) {
        return null;
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {

    }
}
