package com.onwl007.blog.domain;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity//实体
public class User implements UserDetails,Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增策略
    private Long id;//实体唯一标识

    @NotEmpty(message = "姓名不能为空")
    @Size(min = 2,max = 20)
    @Column(nullable = false,length = 20)//映射为字段，值不能为空
    private String name;

    @NotEmpty(message = "邮箱不能为空")
    @Size(max = 50)
    @Email(message = "邮箱格式不对")
    @Column(nullable = false,length = 50,unique = true)
    private String email;

    @NotEmpty(message = "账号不能为空")
    @Size(min = 3,max = 30)
    @Column(nullable = false,length = 20,unique = true)//映射为字段，值不能为空
    private String username;//用户登录时的唯一账号

    @NotEmpty(message = "密码不能为空")
    @Size(max = 100)
    @Column(length = 100)//映射为字段，值不能为空
    private String password;//密码

    @Column(length = 200)
    private String avatar;//头像图片地址

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    //JPA的规范要求无参构造函数，设为protected防止直接使用
    protected User() {

    }

    public User(Long id, String name, String email,String username,String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username=username;
        this.password=password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString(){
        return String.format("User[id=%d,username='%s',name='%s',email='%s',password='%s']",id,username,name,email,password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //需要将List<Authority>转成List<SimpleGrantedAuthority>, 否则前端拿不到角色列表名称
        List<SimpleGrantedAuthority> simpleGrantedAuthorities=new ArrayList<>();
        for (GrantedAuthority authority:this.authorities){
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleGrantedAuthorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
