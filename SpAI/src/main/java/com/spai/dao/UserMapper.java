package com.spai.dao;

import com.spai.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(int id);

    public List<User> selectUser(String username);

    public List<User> getUsers();

    public User selectById(int id);
}
