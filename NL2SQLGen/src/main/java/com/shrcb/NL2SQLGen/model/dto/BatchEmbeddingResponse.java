package com.shrcb.NL2SQLGen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BatchEmbeddingResponse {
    private List<String> texts;
    private List<List<Float>> embeddings;
    private Integer count;
    private Integer embeddingDim;
}
