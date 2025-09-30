package com.shrcb.NL2SQLGen.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 获取语义向量化后数据 simCSE
 */
@Service
public interface EmbeddingService {

    /**
     * 将文本向量化，同步方法
     * @param text text to be embedded
     * @return vector of input text
     */
    List<Float> embed(String text);

    /**
     * 将文本向量化，异步方法
     * @param text text to be embedded
     * @return vector of input text
     */
    Mono<List<Float>> embedAsync(String text);

    /**
     * 将文本向量化，批量方法
     * @param texts text list to be embedded
     * @return vector list of input text
     */
    public List<List<Float>> batchEmbed(List<String> texts);
}
