package com.spai.service;

import com.spai.pojo.Article;
import com.spai.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.Date;
import java.util.function.Function;

@Configuration
public class CommentTools {
    @Autowired
    CommentServiceImpl commentService;

    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    AIServiceImpl aiService;

    public record DeleteCommentRequest(int id){

    }

    public record AddCommentRequest(int ArticleId, String demand){

    }



    @Bean
    @Description("处理评论删除")
    public Function<DeleteCommentRequest,String> deleteComment(){
        return deleteCommentRequest -> {
            System.out.println("======处理评论删除======");
            commentService.deleteComment(deleteCommentRequest.id);
            System.out.println("删除的评论ID："+deleteCommentRequest.id);
            System.out.println("======评论删除成功======");
            return "评论删除成功"  ;
        };
    }

    @Bean
    @Description("处理添加评论")
    public Function<AddCommentRequest,String> addComment(){
        return addCommentRequest -> {
            System.out.println("======开始AI评论======");
            int articleId = addCommentRequest.ArticleId;
            String demand = addCommentRequest.demand;
            System.out.println("demand:"+demand);
            Article article = articleService.selectArticleByid(articleId);
            if(article == null) return "没有找到对应的文章";
            System.out.println("文章的内容："+article.getContent() );
            String content = "";
            content = aiService.botResponse("帮我根据要求给出评论内容,只给出完成对应的评论的内容，不要给出任何无关评论内容的语句，你的回答应该只有评论内容而不含其他内容:"+demand+"\n"+"文章内容："+article.getContent());
            System.out.println("AI生成的评论内容："+content);

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setArticleid(articleId);
            commentService.saveComment(content,articleId);

            System.out.println("======AI评论完成======");
            return "评论内容："+comment.getContent();
        };
    }


}
