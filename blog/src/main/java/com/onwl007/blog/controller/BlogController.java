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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private ResultGenerator generator;

    /**
     * 查询全部博客文章并按照时间降序排列
     * @return
     */
    @GetMapping
    public RestResult listBlogs(){
        List<Blog> blogList=blogService.listBlogsByCreatTimeDsec();
        if (blogList!=null&&blogList.size()>0){
            return generator.getSuccessResult("查询全部博客文章成功",blogList);
        }
        return generator.getFailResult("查询全部博客文章失败");
    }

    @GetMapping("/newest")
    public String listNewestEsBlogs(Model model) {
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
        model.addAttribute("newest", newest);
        return "newest";
    }

    @GetMapping("/hotest")
    public String listHotestEsBlogs(Model model) {
        List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
        model.addAttribute("hotest", hotest);
        return "hotest";
    }
}
