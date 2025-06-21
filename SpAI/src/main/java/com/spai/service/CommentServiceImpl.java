package com.spai.service;

import com.spai.dao.CommentMapper;
import com.spai.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment selectCommentByid(int id) {
        return commentMapper.selectCommentByid(id);
    }

    @Override
    public void addComment(Comment comment) {
        commentMapper.addComment(comment);
    }

    @Override
    public void deleteComment(int id) {
        commentMapper.deleteComment(id);
    }

    @Override
    public List<Comment> selectCommentByarticleid(int articleid) {
        return commentMapper.selectCommentByarticleid(articleid);
    }

    // 保存评论
    public void saveComment(String content, int articleId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticleid(articleId);
        comment.setTime(new Date());
        commentMapper.addComment(comment);
    }

    @Override
    public int getMaxCommentid() {
        return commentMapper.getMaxCommentid();
    }

}
