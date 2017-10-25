package com.onwl007.blog.vo;

import java.io.Serializable;

/**
 * Tag值对象
 * @author onwl007@126.com
 * @date 2017/10/25 20:44
 */
public class TagVO implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;
    private Long count;

    public TagVO(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
