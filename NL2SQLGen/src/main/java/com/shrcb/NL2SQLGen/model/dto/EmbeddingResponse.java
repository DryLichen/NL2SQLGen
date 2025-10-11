package com.shrcb.NL2SQLGen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddingResponse {
    private String text;
    private List<Float> embedding;
    private Integer embeddingDim;
}
