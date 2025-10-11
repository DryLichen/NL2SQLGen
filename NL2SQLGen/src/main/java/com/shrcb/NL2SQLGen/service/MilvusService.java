package com.shrcb.NL2SQLGen.service;

import io.milvus.exception.MilvusException;
import org.springframework.stereotype.Service;

/**
 * Milvus服务接口
 * 功能：插入向量数据
 */
@Service
public interface MilvusService {

    public void initCollection(String collectionName, int dimension) throws MilvusException;
}

