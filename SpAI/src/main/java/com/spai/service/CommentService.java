package com.spai.service;

import com.spai.pojo.Comment;

import java.util.List;

public interface CommentService {

    public Comment selectCommentByid(int id);

    public void addComment(Comment comment);

    public void deleteComment(int id);

    public List<Comment> selectCommentByarticleid(int articleid);

    public void saveComment(String content, int articleId);

    public int getMaxCommentid();

}
