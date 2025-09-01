package com.shrcb.NL2SQLGen.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Bean
    public MilvusServiceClient milvusClient() {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("127.0.0.1")
                .withPort(19530)
                .build();
        return new MilvusServiceClient(connectParam);
    }
}
