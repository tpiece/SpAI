package com.spai;

import com.spai.pojo.Article;
import com.spai.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootApplication
@Slf4j
public class SpAiApplication {

    @Autowired
    private ArticleService articleService;

    public static void main(String[] args) {
        SpringApplication.run(SpAiApplication.class, args);
    }

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }

    // In the real world, ingesting documents would often happen separately, on a CI
    // server or similar.
    @Bean
    CommandLineRunner ingestTermOfServiceToVectorStore(EmbeddingModel embeddingModel, VectorStore vectorStore,
                                                       @Value("classpath:rag/1.txt") Resource termsOfServiceDocs) {

        return args -> {
            vectorStore.write(new TokenTextSplitter().transform(new TextReader(termsOfServiceDocs).read()));
            List<Article> articles = articleService.getallArticles();
            vectorStore.add(List.of(new Document(articles.toString())));
            // Ingest the document into the vector store
//            vectorStore.write(new TokenTextSplitter().transform(new TextReader(termsOfServiceDocs).read()));

            vectorStore.similaritySearch("Cancelling Bookings").forEach(doc -> {
                log.info("Similar Document: {}", doc.getContent());
            });
        };
    }
    
}
