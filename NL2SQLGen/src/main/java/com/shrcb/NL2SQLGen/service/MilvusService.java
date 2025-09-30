package com.shrcb.NL2SQLGen.service;

import io.milvus.client.MilvusServiceClient;
import io.milvus.exception.MilvusException;
import io.milvus.param.collection.*;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

/**
 * Milvus服务接口
 * 功能：插入向量数据
 */
@Service
public interface MilvusService {

    public void initCollection(String collectionName, int dimension) throws MilvusException;
}

