package com.spai.service;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ChatModel chatModel;

    @Resource
    private OpenAiImageModel openaiImageModel;

    @Override
    public String imageToText(String filePath) {

        FileSystemResource imageResource = new FileSystemResource(filePath);

        UserMessage userMessage = new UserMessage("解释一下这个图片，不要给出（这幅图片描述了....   这张图片展示了.....   画面中....）这样的术语，直接给出对应的实际内容",
                new Media(MimeTypeUtils.IMAGE_PNG , imageResource));

        ChatResponse response = chatModel.call(new Prompt(userMessage,
                OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.GPT_4_O_MINI.getValue()).build()));

        return response.getResult().getOutput().getText();
    }

    @Override
    public String TextToImage(String text) {
        ImageResponse response = openaiImageModel.call(
                new ImagePrompt(text,
                        OpenAiImageOptions.builder()
                                .quality("hd")
                                .N(4)
                                .height(1024)
                                .width(1024).build())

        );
        System.out.println("======AI文本转图片完成======");
        return response.getResult().getOutput().getUrl();
    }




/*    ImageResponse response = openaiImageModel.call(
            new ImagePrompt("A light cream colored mini golden doodle",
                    OpenAiImageOptions.builder()
                            .quality("hd")
                            .N(4)
                            .height(1024)
                            .width(1024).build())

    );*/







/*    var imageResource = new ClassPathResource("/multimodal.test.png");

    var userMessage = new UserMessage("Explain what do you see on this picture?",
            new Media(MimeTypeUtils.IMAGE_PNG, this.imageResource));

    ChatResponse response = chatModel.call(new Prompt(this.userMessage,
            OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.GPT_4_O.getValue()).build()));*/
}
