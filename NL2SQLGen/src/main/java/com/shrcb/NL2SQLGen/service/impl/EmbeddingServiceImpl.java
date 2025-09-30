package com.shrcb.NL2SQLGen.service.impl;

import com.shrcb.NL2SQLGen.model.dto.BatchEmbeddingResponse;
import com.shrcb.NL2SQLGen.model.dto.EmbeddingResponse;
import com.shrcb.NL2SQLGen.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * simCSE embedding service
 */
@Service
public class EmbeddingServiceImpl implements EmbeddingService{

    private WebClient webClient;

    // 新建一个WebClient以调用simCSE服务
    public EmbeddingServiceImpl(@Value("${simcse.url}:${simcse.port}") String simcseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(simcseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // 向量化文本
    public List<Float> embed(String text) {
        Map<String, String> request = Map.of("text", text);

        return webClient.post()
                .uri("/encode")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .map(EmbeddingResponse::getEmbedding)
                .block(); // 在非响应式环境中使用block()
    }

    // 向量化文本，异步版本
    public Mono<List<Float>> embedAsync(String text) {
        Map<String, String> request = Map.of("text", text);

        return webClient.post()
                .uri("/encode")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .map(EmbeddingResponse::getEmbedding);
    }

    // 批量向量化文本
    public List<List<Float>> batchEmbed(List<String> texts) {
        Map<String, List<String>> request = Map.of("texts", texts);

        return webClient.post()
                .uri("/batch_encode")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BatchEmbeddingResponse.class)
                .map(BatchEmbeddingResponse::getEmbeddings)
                .block();
    }
}
