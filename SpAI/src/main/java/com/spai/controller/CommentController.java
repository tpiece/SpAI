package com.spai.controller;

import com.spai.pojo.Comment;
import com.spai.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "http://localhost:8081")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    // 提交评论
    @PostMapping("/addComment")
    public Map<String, Object> addComment(@RequestParam String content, @RequestParam int articleId) {

        System.out.println("添加评论");
        // 保存评论
        commentService.saveComment(content, articleId);

        Comment comment = commentService.selectCommentByid(commentService.getMaxCommentid());

        System.out.println(comment.getTime());

        // 返回新评论的数据
        Map<String, Object> response = new HashMap<>();
        response.put("content", comment.getContent());
        response.put("time", comment.getTime());
        return response;
    }

    @GetMapping("/getCommentsByArticleId")
    public List<Comment> getCommentsByArticleId(@RequestParam int id) {
        return commentService.selectCommentByarticleid(id);
    }

    @PostMapping("/saveComment")
    public Map<String, Object> saveComment(@RequestBody Comment comment) {
        // 保存评论
        commentService.saveComment(comment.getContent(), comment.getArticleid());

        Comment cmt = commentService.selectCommentByid(commentService.getMaxCommentid());
        System.out.println(cmt.getArticleid());


        // 返回新评论的数据
        Map<String, Object> response = new HashMap<>();
        response.put("content", cmt.getContent());
        response.put("time", cmt.getTime());
        response.put("articleid", cmt.getArticleid());
        response.put("id", cmt.getId());

        return response;
    }


}