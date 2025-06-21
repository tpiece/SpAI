package com.spai.dao;

import com.spai.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper {
    public int getarticleNum(int id);

    public void addarticle(Article article);

    public void deleteartcile(int id);

    public void updatearticle(Article article);

    public List<Article> getarticles(int id);

    public int getauthorid(int id);

    public List<Article> selectBystr(String str);

    public Article selectArticleByid(int id);

    public int selectMaxid();

    List<Article> getallArticles();
}
