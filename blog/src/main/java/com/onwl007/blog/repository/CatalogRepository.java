package com.onwl007.blog.repository;

import com.onwl007.blog.domain.Catalog;
import com.onwl007.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Catalog仓库
 * @author onwl007@126.com
 * @date 2017/10/26 20:31
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long>{

    /**
     * 根据用户查询
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户查询
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user,String name);
}
