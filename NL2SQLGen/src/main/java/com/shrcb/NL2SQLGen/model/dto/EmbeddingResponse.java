package com.shrcb.NL2SQLGen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmbeddingResponse {
    private String text;
    private List<Float> embedding;
    private Integer embeddingDim;
}
