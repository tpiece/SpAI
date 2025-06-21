package com.spai.dao;

import com.spai.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
    public Comment selectCommentByid(int id);

    public void addComment(Comment comment);

    public void deleteComment(int id);

    public List<Comment> selectCommentByarticleid(int articleid);

    public int getMaxCommentid();

}
