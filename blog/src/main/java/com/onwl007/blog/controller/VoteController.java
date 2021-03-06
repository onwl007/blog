package com.onwl007.blog.controller;

import com.onwl007.blog.domain.User;
import com.onwl007.blog.service.BlogService;
import com.onwl007.blog.service.VoteService;
import com.onwl007.blog.util.ConstraintViolationExceptionHandler;
import com.onwl007.blog.util.ResultGenerator;
import com.onwl007.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

/**
 * 点赞控制器
 *
 * @author onwl007@126.com
 * @date 2017/11/5 15:45
 */
@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private ResultGenerator generator;


    /**
     * 发表点赞
     *
     * @param blogId
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')") //指定角色权限才能操作方法
    public String createVote(Long blogId) {
        try {
            blogService.createVote(blogId);
        } catch (Exception e) {
            return generator.getFailResult("发表点赞失败").toString();
        }

        return generator.getSuccessResult("发表点赞成功").toString();
    }

    /**
     * 删除点赞
     *
     * @param id
     * @param blogId
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  //指定角色权限才能操作方法
    public String delete(@PathVariable("id") Long id, Long blogId) {
        boolean isOwner = false;
        User user = voteService.getVoteById(id).getUser();

        //判断操作用户是否是点赞的所有者
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }

        if (!isOwner) {
            return generator.getFailResult("没有操作权限，删除点赞失败").toString();
        }

        try {
            blogService.removeVote(blogId, id);
            voteService.removeVote(id);
        } catch (Exception e) {
            return generator.getFailResult(e.getMessage().toString()).toString();
        }

        return generator.getSuccessResult("删除点赞成功").toString();
    }

}
