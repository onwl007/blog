package com.onwl007.blog.service;

import com.onwl007.blog.domain.*;
import com.onwl007.blog.domain.es.EsBlog;
import com.onwl007.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * blog服务
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private EsBlogService esBlogService;

    /**
     * 保存博客，同时保存ES
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew=(blog.getId()==null);
        EsBlog esBlog=null;
        Blog returnBlog=blogRepository.save(blog);

        if (isNew){
            esBlog =new EsBlog(returnBlog);
        }else {
            esBlog=esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }

        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    /**
     * 删除博客，同时删除掉ES里的
     * @param id
     */
    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
        EsBlog esBlog=esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    /**
     * 根据文章标题模糊查询
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        //模糊查询
        title="%"+title+"%";
        String tags=title;
        Page<Blog> blogs=blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tags,user,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        //模糊查询
        title="%"+title+"%";
        Page<Blog> blogs=blogRepository.findByUserAndTitleLike(user,title,pageable);
        return blogs;
    }

    /**
     * 查询所有博客并按照时间降序排列
     * @return
     */
    @Override
    public List<Blog> listBlogsByCreatTimeDsec() {
        List<Blog> blogList=blogRepository.findAllByOrderByCreateTimeDesc();
        return blogList;
    }

    /**
     *根据分类查询
     * @param catalog
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs=blogRepository.findByCatalog(catalog,pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog=blogRepository.findOne(id);
        blog.setReadSize(blog.getCommentSize()+1);
        this.saveBlog(blog);

    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog originBlog=blogRepository.findOne(blogId);
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment=new Comment(user,commentContent);
        originBlog.addComment(comment);
        return this.saveBlog(originBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originBlog=blogRepository.findOne(blogId);
        originBlog.removeComment(commentId);
        this.saveBlog(originBlog);
    }

    @Override
    public Blog createVote(Long blogId) {
        Blog originBlog=blogRepository.findOne(blogId);
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote=new Vote(user);
        boolean isExist=originBlog.addVote(vote);
        if (isExist){
            throw new IllegalArgumentException("该用户已经点过赞了");
        }
        return this.saveBlog(originBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog originBlog=blogRepository.findOne(blogId);
        originBlog.removeVote(voteId);
        this.saveBlog(originBlog);
    }
}
