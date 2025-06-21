package com.spai.controller;

import com.spai.pojo.User;
import com.spai.service.UserService;
import com.spai.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081") // 允许来自该源的请求
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        List<User> users = userService.getUsers();
        return users;
    }

    public void addUser(User user) {
        userService.addUser(user);
    }


    @PutMapping("/updateUser")
    public void updateUser(@RequestParam User user) {
        User u = userService.selectById(user.getId());
        user.setArticleNum(u.getArticleNum());
        userService.updateUser(user);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam int id) {
        userService.deleteUser(id);
    }


}
