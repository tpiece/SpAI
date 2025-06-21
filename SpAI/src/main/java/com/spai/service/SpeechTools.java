package com.spai.service;

import com.aliyun.oss.OSS;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.spai.pojo.Article;
import com.spai.pojo.User;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.FileSystemResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;


@Configuration
public class SpeechTools {


    @Autowired
    OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Autowired
    AIService aiService;

    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    SpeechServiceImpl speechService;

    @Autowired
    VectorStore vectorStore;

    static int flag = 1;


    public record TextToSpeechRequest(int articleId){

    }

    public record SpeechToTextRequest(int authorId ,String fileName){

    }

    @Bean
    @Description("文本转音频")
    public Function<TextToSpeechRequest, String> textToSpeech(){
        return textToSpeechRequest -> {
            System.out.println("======开始将文章转为音频======");
            Article article = articleService.selectArticleByid(textToSpeechRequest.articleId);
            System.out.println("需要转为音频的articleID:"+article.getId());
            SpeechResponse response = speechService.TextToSpeech(article.getContent());
            byte[] responseAsBytes = response.getResult().getOutput();

            // 生成唯一文件名（避免覆盖）
            String fileName = "audio/article_" + textToSpeechRequest.articleId() + "_" + System.currentTimeMillis() + ".mp3";

            try {
                // 上传到阿里云 OSS
                ossClient.putObject(
                        bucketName,
                        fileName,
                        new ByteArrayInputStream(responseAsBytes)
                );
            } catch (Exception e) {
                e.printStackTrace();
                return "音频上传到 OSS 失败";
            }

            // 生成文件 URL（公共读模式）
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        };
    }

    // 假设的辅助方法，用于保存音频文件
    // 辅助方法，用于保存音频文件
    private static void saveAudioToFile(byte[] audioData, Path filePath) throws IOException {
        // 创建目录（如果不存在）
        Files.createDirectories(filePath.getParent());

        // 保存音频文件
        Files.write(filePath, audioData);
    }

    @Bean
    @Description("音频转文本")
    public Function<SpeechToTextRequest, Article> WriteArticleBySpeech(){
        return speechToTextRequest -> {
            if(flag == 1){
                flag = 0;
                System.out.println("======开始将音频转为文本======");
                int authorId = speechToTextRequest.authorId;
                String fileName = speechToTextRequest.fileName;
                // 获取项目根目录
                String projectRoot = System.getProperty("user.dir");
                // 构建文件路径，包含 uploads 文件夹
//            String filePath = projectRoot + "/uploads/" + fileName;
                FileSystemResource  filePath = new FileSystemResource(projectRoot + "/uploads/" + fileName);
                System.out.println("需要转为文本的音频文件路径:"+filePath);

//            FileSystemResource audioFile = new FileSystemResource(filePath.getPath());
                AudioTranscriptionResponse response = speechService.SpeechToText(filePath);
                String text = response.getResult().getOutput();
                System.out.println("音频转文本结果:"+text);

                Article article = saveArticleBySpeech(authorId, text, aiService,articleService);

                vectorStore.add(List.of(new Document(article.toString())));


                System.out.println("======音频转文本成功======");
                flag = 1;
                return article;
            }
            return null;
        };
    }

    public static Article saveArticleBySpeech(int authorId, String Speechtext, AIService aiService,ArticleService articleService){
        if(flag == 0){
            System.out.println("======开始AI音频写作======");
            System.out.println("SpeechText:"+Speechtext);
            String title="";
            if(flag == 0){
                title = aiService.MediaResponse("帮我根据音频文本设计一个文本标题,只给出文本的标题，不要给出任何无关文本标题的语句,你的回答应该只有标题的内容"+"\n"+"SpeechText:"+Speechtext);
                System.out.println("AI音频写作生成的文章标题："+title);
                flag=1;
            }
            int articleId = articleService.selectMaxid();
            Article article = new Article(0 ,title ,Speechtext ,authorId);
            articleService.addarticle(article);
            flag=0;
            System.out.println("======AI音频写作完成======");
            return article;
        }
        return null;
    }


}
