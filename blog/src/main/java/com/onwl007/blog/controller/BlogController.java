package com.onwl007.blog.controller;

import com.onwl007.blog.domain.Blog;
import com.onwl007.blog.domain.RestResult;
import com.onwl007.blog.domain.User;
import com.onwl007.blog.domain.es.EsBlog;
import com.onwl007.blog.service.BlogService;
import com.onwl007.blog.service.EsBlogService;
import com.onwl007.blog.util.ResultGenerator;
import com.onwl007.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * blog控制器
 */
@RestController
@RequestMapping("blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ResultGenerator generator;

    /**
     * 查询全部博客文章并按照时间降序排列
     *
     * @return
     */
    @GetMapping
    public String listBlogs() {
        List<Blog> blogList = blogService.listBlogsByCreatTimeDsec();
        if (blogList != null && blogList.size() > 0) {
            return generator.getSuccessResult("查询全部博客文章成功", blogList).toString();
        }
        return generator.getFailResult("查询全部博客文章失败").toString();
    }

    @GetMapping("/{id}")
    public String queryBlogById(@PathVariable("id") Long id) {
        Blog blog = blogService.getBlogById(id);
        if (!blog.equals("")) {
            return generator.getSuccessResult("查询博客成功", blog).toString();
        }
        return generator.getFailResult("查询博客失败").toString();
    }

    @GetMapping("/newest")
    public String listNewestEsBlogs() {
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
        if (newest != null && newest.size() > 0) {
            return generator.getSuccessResult("查询最新 Top5 文章成功", newest).toString();
        }
        return generator.getFailResult("查询最新 Top5 失败").toString();
    }

    @GetMapping("/hotest")
    public String listHotestEsBlogs() {
        List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
        if (hotest != null && hotest.size() > 0) {
            return generator.getSuccessResult("查询最热 Top5 文章成功", hotest).toString();
        }
        return generator.getFailResult("查询最热 Top5 失败").toString();
    }
}
