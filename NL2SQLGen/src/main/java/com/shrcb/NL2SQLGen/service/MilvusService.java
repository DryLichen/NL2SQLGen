package com.shrcb.NL2SQLGen.service;

import io.milvus.client.MilvusServiceClient;
import io.milvus.exception.MilvusException;
import io.milvus.param.collection.*;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

@Service
public class MilvusService {

    private final MilvusServiceClient milvusClient;

    public MilvusService(MilvusServiceClient milvusClient) {
        this.milvusClient = milvusClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initMilvus() throws MilvusException {
        String collectionName = "table_schema";
        int embeddingDim = 768; // 你的 embedding 维度
        initCollection(collectionName, embeddingDim);
    }

    public void initCollection(String collectionName, int dimension) throws MilvusException {
        boolean exists = milvusClient.hasCollection(
                HasCollectionParam.newBuilder()
                        .withCollectionName(collectionName)
                        .build()
        ).getData();

        if (!exists) {
            FieldType vectorField = FieldType.newBuilder()
                    .withName("embedding")
                    .withDataType(io.milvus.param.DataType.FloatVector)
                    .withDimension(dimension)
                    .build();

            FieldType tableField = FieldType.newBuilder()
                    .withName("table_name")
                    .withDataType(io.milvus.param.DataType.VarChar)
                    .withMaxLength(255)
                    .build();

            CreateCollectionParam createParam = CreateCollectionParam.newBuilder()
                    .withCollectionName(collectionName)
                    .withDescription("存储数据库表结构向量")
                    .withShardsNum(2)
                    .addFieldType(vectorField)
                    .addFieldType(tableField)
                    .build();

            milvusClient.createCollection(createParam);
            System.out.println("✅ Collection 创建成功: " + collectionName);

            milvusClient.createIndex(
                    CreateIndexParam.newBuilder()
                            .withCollectionName(collectionName)
                            .withFieldName("embedding")
                            .withIndexType(IndexType.IVF_FLAT)
                            .withMetricType(MetricType.IP)
                            .withExtraParam("{\"nlist\":128}")
                            .withSyncMode(true)
                            .build()
            );
            System.out.println("✅ 索引创建成功");
        } else {
            System.out.println("⚠️ Collection 已存在: " + collectionName);
        }
    }
}

