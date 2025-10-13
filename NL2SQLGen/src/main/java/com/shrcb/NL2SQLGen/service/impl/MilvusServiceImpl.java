package com.shrcb.NL2SQLGen.service.impl;

import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DropCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.vector.request.InsertReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Milvus服务接口
 * 功能：插入向量数据
 */
@Service
public class MilvusServiceImpl {

    private static final String COLLECTION_NAME = "nl2sql_vectors";
    private static final int DIM = 768;

    @Autowired
    private MilvusClientV2 milvusClient;
    @Autowired
    private EmbeddingServiceImpl embeddingService;

    /**
     * Create a new collection of vectors
     */
    public void createCollection() {
        // 检查集合是否存在
        Boolean hasCollection = milvusClient.hasCollection(HasCollectionReq.builder()
                .collectionName(COLLECTION_NAME)
                .build());

        // 如果存在则先删除集合
        if (hasCollection) {
            milvusClient.dropCollection(DropCollectionReq.builder()
                    .collectionName(COLLECTION_NAME)
                    .build());
        }

        // define a Collection Schema
        CreateCollectionReq.CollectionSchema collectionSchema = milvusClient.createSchema();
        // add 6 fileds
        collectionSchema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.Int64)
                .isPrimaryKey(Boolean.TRUE)
                .autoID(Boolean.FALSE)
                .build());
        collectionSchema.addField(AddFieldReq.builder()
             .fieldName("text_id")
             .dataType(DataType.VarChar)
             .maxLength(100)
             .build());
        collectionSchema.addField(AddFieldReq.builder()
             .fieldName("text_content")
             .dataType(DataType.VarChar)
             .maxLength(4000)
             .build());
        collectionSchema.addField(AddFieldReq.builder()
             .fieldName("vector")
             .dataType(DataType.FloatVector)
             .dimension(DIM)
             .build());
        collectionSchema.addField(AddFieldReq.builder()
                .fieldName("category")
                .dataType(DataType.FloatVector)
                .maxLength(50)
                .build());
        collectionSchema.addField(AddFieldReq.builder()
                .fieldName("create_time")
                .dataType(DataType.Int64)
                .build());

        // 创建向量集合
        milvusClient.createCollection(CreateCollectionReq.builder()
                .collectionSchema(collectionSchema)
                .build());
    }

    /**
     * insert vectors into a collection
     * @param schemaInfo
     * @param vector
     */
    public void insertSchema(String schemaInfo, List<Float> vector) {
        InsertReq insertReq = InsertReq.builder()
                .collectionName(COLLECTION_NAME)
                .data(Arrays.asList(
                        Arrays.asList(schemaInfo),
                        Arrays.asList(vector)
                ))
                .build();

        milvusClient.insert(insertReq);
    }

    /**
     * search a certain vector
     * @param queryVector
     * @param topK
     * @return
     */
    public List<String> searchSimilarSchemas(List<Float> queryVector, int topK) {
        SearchReq searchReq = SearchReq.builder()
                .collectionName(COLLECTION_NAME)
                .vector(List.of(queryVector))
                .annsField("vector")
                .metricType("L2")
                .topK(topK)
                .build();

        SearchResp searchResp = milvusClient.search(searchReq);
        return searchResp.getSearchResults().get(0).get(0).getEntity().get("schema_info").toString();
    }
}

