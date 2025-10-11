package com.shrcb.NL2SQLGen.config;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Value("${milvus.url}")
    private String milvusHost;

    @Value("${milvus.port}")
    private Integer milvusPort;

    @Bean
    public MilvusClientV2 milvusClient() {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(milvusHost + ":" + milvusPort)
//                .username()
//                .password()
                .build();


        return new MilvusClientV2(connectConfig);
    }
}
