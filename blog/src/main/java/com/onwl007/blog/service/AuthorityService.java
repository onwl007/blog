package com.onwl007.blog.service;

import com.onwl007.blog.domain.Authority;

/**
 * Authority接口
 */
public interface AuthorityService {

    /**
     * 根据id获取Authority
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
