package com.onwl007.blog.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * 自定义配置
     * @param http
     * @throws Exception
     */
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/css/**","/js/**","/fonts/**","/index").permitAll();//都可以访问
    }
}
