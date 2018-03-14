package com.onwl007.blog.controller;

import com.onwl007.blog.domain.Blog;
import com.onwl007.blog.domain.Comment;
import com.onwl007.blog.domain.User;
import com.onwl007.blog.service.BlogService;
import com.onwl007.blog.service.CommentService;
import com.onwl007.blog.util.ConstraintViolationExceptionHandler;
import com.onwl007.blog.util.ResultGenerator;
import com.onwl007.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 评论控制器
 *
 * @author onwl007@126.com
 * @date 2017/11/5 15:01
 */
@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ResultGenerator generator;

    /**
     * 获取评论列表
     *
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping
    public String listComments(@RequestParam(value = "blogId", required = true) Long blogId, Model model) {
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> comments = blog.getComments();

        //判断操作用户是否是评论的所有者
        String commentOwner = "";
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null) {
                commentOwner = principal.getUsername();
            }
        }

        if (commentOwner != null) {
            return generator.getSuccessResult("获取评论列表成功", comments).toString();
        }
        return generator.getFailResult("获取评论列表失败").toString();
    }

    /**
     * 发表评论
     *
     * @param blogId
     * @param commentContent
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')") //指定角色权限才能操作方法
    public String createComment(Long blogId, String commentContent) {
        try {
            blogService.createComment(blogId, commentContent);
        } catch (Exception e) {
            return generator.getFailResult(e.getMessage().toString()).toString();
        }

        return generator.getSuccessResult("发表评论成功").toString();
    }

    /**
     * 删除评论
     *
     * @param id
     * @param blogId
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')") //指定角色权限才能操作方法
    public String delete(@PathVariable("id") Long id, Long blogId) {
        boolean isOwner = false;
        User user = commentService.getCommentById(id).getUser();

        //判断操作用户是否是评论的所有者
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }

        if (!isOwner) {
            return generator.getFailResult("没有操作权限。删除评论失败").toString();
        }

        try {
            blogService.removeComment(blogId, id);
            commentService.removeComment(id);
        } catch (Exception e) {
            return generator.getFailResult(e.getMessage().toString()).toString();
        }

        return generator.getSuccessResult("删除评论成功").toString();

    }
}
