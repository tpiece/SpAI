package com.spai.service;

import com.spai.pojo.Article;

import java.util.List;

public interface ArticleService {
    public int getarticleNum(int id);

    public void addarticle(Article article);

    public void deleteartcile(int id);

    public void updatearticle(Article article);

    public List<Article> getarticles(int id);

    public int getauthorid(int id);

    public List<Article> selectBystr(String str);

    public Article selectArticleByid(int id);

    public int selectMaxid();

    public List<Article> getallArticles();

}
