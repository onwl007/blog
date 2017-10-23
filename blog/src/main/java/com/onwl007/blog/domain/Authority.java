package com.onwl007.blog.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

/**
 * 权限
 */
@Entity
public class Authority implements GrantedAuthority{

    private static final long serialVersionUID=1L;

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增长策略
    private Long id;//用于用户唯一标识

    @Column(nullable = false)//映射为字段，值不能为空
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
