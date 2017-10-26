package com.onwl007.blog.service;

import com.onwl007.blog.domain.Catalog;
import com.onwl007.blog.domain.User;

import java.util.List;

/**
 * Catalog服务接口
 * @author onwl007@126.com
 * @date 2017/10/26 20:35
 */
public interface CatalogService {

    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 根据id删除Catalog
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 根据id查询Catalog
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取Catalog列表
     * @param user
     * @return
     */
    List<Catalog> listCatalogs(User user);

}
