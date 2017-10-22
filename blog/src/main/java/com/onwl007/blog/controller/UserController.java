package com.onwl007.blog.controller;

import com.onwl007.blog.domain.Authority;
import com.onwl007.blog.domain.User;
import com.onwl007.blog.repository.UserRepository;
import com.onwl007.blog.service.AuthorityService;
import com.onwl007.blog.service.UserService;
import com.onwl007.blog.util.ConstraintViolationExceptionHandler;
import com.onwl007.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

//    List<User> getUserList(){
//        return userRepository.findAll();
//    }

    /**
     * 查询所有用户
     * @param model
     * @return
     */
    @GetMapping()
    public ModelAndView list(Model model){
        model.addAttribute("userList",userRepository.findAll());
        model.addAttribute("title","用户管理");
        return new ModelAndView("users/list","userModel",model);
    }

    /**
     * 根据id查询用户
     * @param id
     * @param model
     * @return
     */
    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id")Long id, Model model){
        User user=userRepository.findOne(id);
        model.addAttribute("users",user);
        model.addAttribute("title","查看用户");
        return new ModelAndView("users/view","userModel",model);
    }

    /**
     * 获取创建表单页面
     * @param model
     * @return
     */
    @GetMapping("/form")
    public ModelAndView createForm(Model model){
        model.addAttribute("users",new User(null,null,null,null,null));
        model.addAttribute("title","创建用户");
        return new ModelAndView("users/form","userModel",model);
    }

    /**
     * 保存或者修改用户
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(User user, Long authorityId){

        List<Authority> authorities=new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAuthorities(authorities);

        try {
            userService.saveUser(user);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }

    /**
     * 根据id删除用户
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id, Model model){
        userRepository.delete(id);
//        model.addAttribute("userList",getUserList());
//        model.addAttribute("title","删除用户");
//        return new ModelAndView("users/list","userModel",model);
        return new ModelAndView("redirect:/users");
    }

    @GetMapping(value = "/modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model){
        User user=userRepository.findOne(id);
        model.addAttribute("users",user);
        model.addAttribute("title","修改用户");
        return new ModelAndView("users/form","userModel",model);
    }
}

