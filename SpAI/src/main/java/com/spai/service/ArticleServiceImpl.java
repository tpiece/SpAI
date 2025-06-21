package com.spai.service;


import com.spai.dao.ArticleMapper;
import com.spai.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articlemapper;

    @Override
    public int getarticleNum(int id) {
        return articlemapper.getarticleNum(id);
    }

    @Override
    public void addarticle(Article article) {
        articlemapper.addarticle(article);
    }

    @Override
    public void deleteartcile(int id) {
        articlemapper.deleteartcile(id);
    }

    @Override
    public void updatearticle(Article article) {
        articlemapper.updatearticle(article);
    }

    @Override
    public List<Article> getarticles(int id) {
        return articlemapper.getarticles(id);
    }

    @Override
    public int getauthorid(int id) {
        return articlemapper.getauthorid(id);
    }

    @Override
    public List<Article> selectBystr(String str) {
        return articlemapper.selectBystr(str);
    }

    @Override
    public Article selectArticleByid(int id) {
        return articlemapper.selectArticleByid(id);
    }

    @Override
    public int selectMaxid() {
        return articlemapper.selectMaxid();
    }

    @Override
    public List<Article> getallArticles() {
        return articlemapper.getallArticles();
    }


}
