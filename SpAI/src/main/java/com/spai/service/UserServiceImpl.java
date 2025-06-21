package com.spai.service;


import com.spai.dao.UserMapper;
import com.spai.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;

    @Override
    public void addUser(User user) {
        usermapper.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        usermapper.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        usermapper.deleteUser(id);
    }

    @Override
    public List<User> selectUser(String username) {
        return usermapper.selectUser(username);
    }

    @Override
    public List<User> getUsers() {
        return usermapper.getUsers();
    }

    @Override
    public User selectById(int id) {
        return usermapper.selectById(id);
    }
}
