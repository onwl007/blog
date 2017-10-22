package com.onwl007.blog.service;

import com.onwl007.blog.domain.Authority;
import com.onwl007.blog.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Authority接口的实现
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
