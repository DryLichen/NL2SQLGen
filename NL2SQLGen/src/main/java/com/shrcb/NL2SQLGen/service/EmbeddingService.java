package com.shrcb.NL2SQLGen.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmbeddingService {
    private final RestTemplate rt = new RestTemplate();
    private final String embedUrl; // e.g. http://localhost:8000/embed

    public EmbeddingService(String embedUrl) { this.embedUrl = embedUrl; }

    public float[] embed(String text) {
        // payload & response format depend on your embedding service
        Map<String, String> payload = Map.of("text", text);
        float[] vector = rt.postForObject(embedUrl, payload, float[].class);
        return vector;
    }
}
