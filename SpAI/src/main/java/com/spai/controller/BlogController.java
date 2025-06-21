package com.spai.controller;


import com.spai.pojo.Article;
import com.spai.pojo.Comment;
import com.spai.pojo.User;
import com.spai.service.ArticleServiceImpl;
import com.spai.service.CommentServiceImpl;
import com.spai.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/article")
public class BlogController {

    @Autowired
    private ArticleServiceImpl articleservice;

    @Autowired
    private UserServiceImpl userservice;

    @Autowired
    private CommentServiceImpl commentService;

    @GetMapping("/ShowAllArticle")
    public String ShowAll(Model model) {
        List<User> users = userservice.getUsers();
        model.addAttribute("users", users);
        return "main/ShowAllArticle";
    }

    @GetMapping("/goShowArticledetails/{id}")
    public String goShowArticledetails(@PathVariable int id, Model model) {
        User user = userservice.selectById(id);
        List<Article> articles = articleservice.getarticles(id);
        model.addAttribute("user", user);
        model.addAttribute("articles", articles);
        return "main/ShowArticledetails";
    }

    @RequestMapping("/deletearticle/{id}")
    public String deletearticle(@PathVariable int id) {
        int authorid = articleservice.getauthorid(id);
        articleservice.deleteartcile(id);
        return "redirect:/article/goShowArticledetails/" + authorid;
    }

    @PostMapping("/addarticle")
    public String addarticle(Article article) {
        System.out.println("文章id："+article.getId());
        articleservice.addarticle(article);
        int authorid = article.getAuthorid();
        return "redirect:/article/goShowArticledetails/" + authorid;
    }

    @PostMapping("/updatearticle")
    public String updatearticle(Article article) {
        articleservice.updatearticle(article);
        int authorid = articleservice.getauthorid(article.getId());
        return "redirect:/article/goShowArticledetails/" + authorid;
    }

    @GetMapping("/selectarticle/{authorid}")
    public String selectarticle(String str, Model model, @PathVariable int authorid) {
        List<Article> articles = articleservice.selectBystr(str);
        User user = userservice.selectById(authorid);
        model.addAttribute("articles", articles);
        model.addAttribute("user", user);
        return "main/SearchArticle";
    }

    @GetMapping("/Checkarticle/{id}")
    public String article(@PathVariable int id, Model model){//article的id
        Article article = articleservice.selectArticleByid(id);
        List<Comment> comments = commentService.selectCommentByarticleid(id);
        int authorid = articleservice.getauthorid(id);
        User user = userservice.selectById(authorid);
        model.addAttribute("comments",comments);
        model.addAttribute("user", user);
        model.addAttribute("article", article);
        return "main/Check";
    }
}
