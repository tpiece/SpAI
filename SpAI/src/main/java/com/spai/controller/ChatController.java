package com.spai.controller;

import com.spai.service.AIServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
//@CrossOrigin(origins = "http://localhost:8081")
@Slf4j
public class ChatController {

    @Autowired
    private AIServiceImpl aiService;

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String fileName = request.get("fileName");
        String botReply = "";
        if (fileName == null)  botReply = aiService.botResponse(userMessage);
        else{
            userMessage = userMessage + "\n"+"fileName：" + fileName + "\n";
            botReply = aiService.botResponse(userMessage);
        }

        System.out.println("Bot:"+botReply);
        Map<String, String> response = new HashMap<>();
        response.put("reply", botReply);
        return response;
    }



}
