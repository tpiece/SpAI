package com.spai.service;

import com.spai.pojo.User;

import java.util.List;

public interface UserService {
    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(int id);

    public List<User> selectUser(String username);

    public List<User> getUsers();

    public User selectById(int id);
}
