package com.ll.springai.config;

import com.ll.springai.service.MockWeatherService;
import org.springframework.ai.chroma.ChromaApi;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.ai.vectorsore.ChromaVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfiguration {
    @Bean
    public FunctionCallback weatherFunctionInfo() {

        return FunctionCallbackWrapper.builder(new MockWeatherService())
                .withName("WeatherInfo")
                .withDescription("获取城市天气信息")
                .withResponseConverter((response) -> "" + response.temp() + response.unit())
                .build();
    }

    @Bean
    public VectorStore chromaVectorStore(EmbeddingClient openAiEmbeddingClient, ChromaApi chromaApi) {
        return new ChromaVectorStore(openAiEmbeddingClient, chromaApi, "TestCollection");
    }
}
