package com.onwl007.blog.controller;

import com.onwl007.blog.domain.Catalog;
import com.onwl007.blog.domain.User;
import com.onwl007.blog.service.CatalogService;
import com.onwl007.blog.util.ConstraintViolationExceptionHandler;
import com.onwl007.blog.util.ResultGenerator;
import com.onwl007.blog.vo.CatalogVO;
import com.onwl007.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author onwl007@126.com
 * @date 2017/11/2 21:19
 */
@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ResultGenerator generator;


    /**
     * 获取分类列表
     * @param username
     * @return
     */
    @GetMapping
    public String listCatalogs(@RequestParam(value = "username",required = true)String username){
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        //判断操作用户是否是分类的所有者
        boolean isOwner = false;

        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }

        if (isOwner){
            return generator.getSuccessResult("获取分类列表成功",catalogs).toString();
        }
        return generator.getFailResult("获取分类列表失败",isOwner).toString();
    }

    /**
     * 发表分类
     * @param catalogVO
     * @return
     */
    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVO.username)")//指定用户才能操作方法
    public String createCatalogs(@RequestBody CatalogVO catalogVO){
        String username = catalogVO.getUsername();
        Catalog catalog = catalogVO.getCatalog();

        User user = (User) userDetailsService.loadUserByUsername(username);
        try {
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (Exception e){
            return generator.getFailResult(e.getMessage(),catalog).toString();
        }
        return generator.getSuccessResult("添加分类成功",catalog).toString();
    }

    /**
     * 删除分类
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public String deleteCatalogById(String username,@PathVariable("id") Long id){
        try {
            catalogService.removeCatalog(id);
        }catch (Exception e){
            return generator.getFailResult(e.getMessage().toString()).toString();
        }
        return generator.getSuccessResult("删除指定分类成功").toString();
    }

    /**
     * 根据id获取分类信息
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id, Model model) {
        Catalog catalog = catalogService.getCatalogById(id);
        if (catalog!=null){
            return generator.getSuccessResult("获取分类信息成功",catalog).toString();
        }
        return generator.getFailResult("获取分类信息失败").toString();
    }

}
