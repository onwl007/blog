package com.onwl007.blog.repository;

import com.onwl007.blog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限仓库
 * 相当于dao层
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long>{
}
