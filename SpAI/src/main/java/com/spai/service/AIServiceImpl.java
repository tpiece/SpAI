package com.spai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class AIServiceImpl implements AIService{

    private final ChatClient chatClient;

    private final ChatClient MediaToText;


    public AIServiceImpl(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory , VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .defaultSystem(
                        """
                        你是一个文章系统的AI助手。请以友好、乐于助人且愉快的方式来回复。
                        你可以对文章进行删除，编写，更新，润色等操作。
                        你可以将音频与图片转为文章，也可以将文章转为音频与图片。
                        你可以对评论进行添加，删除等操作。
                        每次对文章进行删除，更新，润色等操作时你必须要知道文章的ID才能进行。
                        每次添加或生成文章，你必须要知道作者的ID编号才能进行添加，或者从历史对话记录中获取到对应的作者ID编号。
                        每次添加评论，你必须要知道对应的文章ID编号才能进行添加，或者从历史对话记录中获取到文章ID编号。
                        每次对话时检查消息记录获取有用信息(例如作者ID编号或者文章ID编号)，避免对用户造成困扰。
                        回答请用中文回答。
                        """
                    //"你是一个文章系统的AI助手。请以友好、乐于助人且愉快的方式来回复。"
                )
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().topK(4).similarityThresholdAll().build()) // RAG
                )

                .defaultFunctions("deleteArticle","updateArticle","polishArticle","writeArticleByAIWithoutFile","showArticle","deleteComment","addComment","textToSpeech","WriteArticleBySpeech","WriteArticleByImage","TextToImage")
                //.defaultTools("deleteArticle","updateArticle","polishArticle","writeArticleByAI","showArticle","deleteComment","addComment","textToSpeech","speechToText","imageToText","TextToImage")
                .build();
        this.MediaToText = chatClientBuilder
                .defaultSystem(
                        """
                        回答请用中文回答。
                        """
                )
                .build();
    }

    @Override
    public String botResponse(String message) {
        System.out.println("botResponse----usermessage:"+message);
                                //prompt提示词
        return this.chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 200))
                //用户信息
                .user(message)
                //请求大模型
                .call()
                //返回文本
                .content();
    }

    @Override
    public String MediaResponse(String message) {
        System.out.println("MediaResponse----usermessage:"+message);
        //prompt提示词
        return this.MediaToText.prompt()
                //用户信息
                .user(message)
                //请求大模型
                .call()
                //返回文本
                .content();
    }




    /*@Override
    public Flux<String> chatResponse(String message) {
        System.out.println("message:"+message);
        //prompt提示词
        return this.chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                //用户信息
                .user(message)
//                //请求大模型
//                .call()
                //流式返回
                .stream()
                //返回文本
                .content();
    }*/


}
