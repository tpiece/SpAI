package com.spai.controller;

import com.spai.pojo.Article;
import com.spai.pojo.User;
import com.spai.service.ArticleServiceImpl;
import com.spai.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081") // 允许来自该源的请求
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/getArticlesByUser")
    public List<Article> getArticles(@RequestParam int userId) {
        return articleService.getarticles(userId);
    }

    @GetMapping("/getArticleByid")
    public Article getArticleByid(@RequestParam int id) {
        return articleService.selectArticleByid(id);
    }

    @GetMapping("/getArticleAuthorName")
    public String getArticleAuthorName(@RequestParam int id) {
        int authorid = articleService.getauthorid(id);
        User user = userService.selectById(authorid);
        return user.getUsername();
    }
}
