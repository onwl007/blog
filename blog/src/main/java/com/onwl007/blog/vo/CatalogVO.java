package com.onwl007.blog.vo;

import com.onwl007.blog.domain.Catalog;

import javax.tools.JavaCompiler;
import java.io.Serializable;

/**
 * @author onwl007@126.com
 * @date 2017/10/26 21:05
 */
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Catalog catalog;

    public CatalogVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
