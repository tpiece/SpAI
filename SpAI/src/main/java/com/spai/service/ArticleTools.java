package com.spai.service;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import com.spai.pojo.Article;
import com.spai.pojo.User;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ArticleTools {

    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AIServiceImpl aiService;

    @Autowired
    VectorStore vectorStore;


    int flag = 0;

    public record DeleteArticleRequest(int articleId){
    }

    public record AddArticleRequest(int authorId, String title, String content ){
    }

    public record UpdateArticleRequest(int articleId, String title, String content, int authorId){
    }

    public record PolishArticleRequest(int articleId){
    }

    public record WriteArticleRequest(int authorId, String demand){
    }

    public record ShowArticleRequest(int articleId){

    }




    @Bean
    @Description("处理文章删除")
    public Function<DeleteArticleRequest, String> deleteArticle(){
        return deleteArticleRequest -> {
            System.out.println("======处理文章删除======");
            vectorStore.delete(List.of(articleService.selectArticleByid(deleteArticleRequest.articleId).toString()));
            articleService.deleteartcile(deleteArticleRequest.articleId);
            System.out.println("======文章删除成功======");
            return "文章删除成功";
        };
    }

    @Bean
    @Description("处理修改文章")
    public Function<UpdateArticleRequest, String> updateArticle(){
        return updateArticleRequest -> {
            Article article = new Article(updateArticleRequest.articleId, updateArticleRequest.title, updateArticleRequest.content, updateArticleRequest.authorId);
            articleService.updatearticle(article);
            return "";
        };
    }

    @Bean
    @Description("处理查看文章")
    public Function<ShowArticleRequest, String> showArticle(){
        return showArticleRequest -> {
            System.out.println("======处理查看文章======");
            Article article = articleService.selectArticleByid(showArticleRequest.articleId);
            if(article == null) return "文章不存在";
            System.out.println("查看文章的id:"+article.getId());
            System.out.println("======处理查看文章完成======");
            return "文章标题为："+article.getTitle()+"        "+"文章内容为："+article.getContent();
        };
    }

    @Bean
    @Description("处理文章润色")
    public Function<PolishArticleRequest, String> polishArticle(){
        return polishArticleRequest -> {
            System.out.println("======开始润色文章======");
            System.out.println("润色的文章id:"+polishArticleRequest.articleId);
            Article article = articleService.selectArticleByid(polishArticleRequest.articleId);
            if(article == null) return "没有找到对应的文章";
            System.out.println("润色前的文章:"+article.getContent());
            String newContent = aiService.botResponse("可以帮我润色一下这句话吗,只给出润色后对应的文章的内容，不要给出任何无关文章内容的语句："+article.getContent());
            System.out.println("润色后的文章："+newContent);
            article.setContent(newContent);
            articleService.updatearticle(article);
            System.out.println("======润色完成======");
            return "润色完成";
        };
    }

    @Bean
    @Description("根据需求写文章")
    public Function<WriteArticleRequest, String> writeArticleByAIWithoutFile(){

        return writeArticleRequest -> {
            if(flag == 0){
                System.out.println("======开始AI写作======");
                int authorid = writeArticleRequest.authorId;
                String demand = writeArticleRequest.demand;
                System.out.println("demand:"+demand);
                User user = userService.selectById(authorid);
                if(user == null) return "没有找到对应的作者";
                System.out.println("AI写作的作者:"+ user.getUsername());
                String title="";
                String content = "";
                if(flag == 0){
                    flag=1;
                    title = aiService.botResponse("帮我根据要求设计一个文章标题,只给出文章的标题，不要给出任何无关文章标题的语句,你的回答应该只有标题的内容:"+demand);
                    System.out.println("AI写作生成的文章标题："+title);
                }
                if(flag == 1){
                    flag=2;
                    content = aiService.botResponse("帮我根据要求给出文章内容,只给出写作后对应的文章的内容，不要给出任何无关文章内容的语句，你的回答应该只有文章内容而不含标题:"+demand+"     标题为"+title);
                    System.out.println("AI写作生成的文章内容："+content);
                }
                Article article = new Article(0,title,content, user.getId());
                articleService.addarticle(article);
                vectorStore.add(List.of(new Document(article.toString())));
                try (FileWriter writer = new FileWriter("src/main/resources/rag/1.txt", true)) {
                    writer.write("文章标题：" + article.getTitle() + "\n");
                    writer.write("文章内容：" + article.getContent() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                flag=0;
                System.out.println("======AI写作完成======");
                return "文章标题："+article.getTitle()+"\n"+"文章内容:"+article.getContent();
            }
            return "AI写作完成";
        };
    }



}
