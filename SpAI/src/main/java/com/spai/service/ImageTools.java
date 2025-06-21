package com.spai.service;

import com.spai.pojo.Article;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

@Configuration
public class ImageTools {
    @Resource
    private ImageService imageService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AIService aiService;

    @Autowired
    VectorStore vectorStore;

    static int flag = 0;

    public record ImageToTextRequest(int authorId ,String fileName) {
    }

    public record TextToImageRequest(int articleId) {
    }

    @Bean
    @Description("图片转文本")
    public Function<ImageToTextRequest, Article> WriteArticleByImage() {
        return imageToTextRequest ->  {
            if(flag == 0){
                flag = 1;
                String fileName = imageToTextRequest.fileName;
                // 获取项目根目录
                String projectRoot = System.getProperty("user.dir");
                // 构建文件路径，包含 uploads 文件夹
                String filePath = projectRoot + "/uploads/" + fileName;
                System.out.println("需要转为文本的图片路径:"+filePath);

                //图片信息
                String imagetext = imageService.imageToText(filePath);

                Article article = saveArticleByImage(imageToTextRequest.authorId, imagetext, aiService,articleService);

                vectorStore.add(List.of(new Document(article.toString())));

                flag = 0;

                return article;
            }

            return null;
        };
    }


    /**
     * 文本转图片，将文本保存
     * @param authorId
     * @param Imagetext
     * @param aiService
     * @param articleService
     * @return
     */
    public static Article saveArticleByImage(int authorId, String Imagetext, AIService aiService, ArticleService articleService){
        if(flag == 1){
            System.out.println("======开始AI图片写作======");
            System.out.println("SpeechText:"+Imagetext);
            String title="";
            if(flag == 1){
                title = aiService.MediaResponse("帮我根据图片文本设计一个文本标题,只给出文本的标题，不要给出任何无关文本标题的语句,你的回答应该只有标题的内容"+"\n"+"ImageText:"+Imagetext);
                System.out.println("AI图片写作生成的文本标题："+title);
                flag = 0;
            }
            Article article = new Article(0 ,title ,Imagetext ,authorId);
            articleService.addarticle(article);
            System.out.println("======AI图片写作完成======");
            flag = 1;
            return article;
        }
        return null;
    }


    @Bean
    @Description("文章转图片")
    public Function<TextToImageRequest, String> TextToImage() {
        return textToImageRequest -> {
            System.out.println("======开始AI文本转图片======");
            Article article = articleService.selectArticleByid(textToImageRequest.articleId);
            return imageService.TextToImage(article.getContent());
        };
    }



}
