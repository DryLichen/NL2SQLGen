package com.shrcb.NL2SQLGen.controller;

import com.shrcb.NL2SQLGen.enums.ResultCode;
import com.shrcb.NL2SQLGen.model.entity.VectorRecord;
import com.shrcb.NL2SQLGen.model.vo.ResultVO;
import com.shrcb.NL2SQLGen.service.DeepSeekService;
import com.shrcb.NL2SQLGen.service.EmbeddingService;
import com.shrcb.NL2SQLGen.service.MetadataService;
import com.shrcb.NL2SQLGen.service.MilvusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class EmbeddingController {

    @Autowired
    private MilvusService milvusService;
    @Autowired
    private DeepSeekService deepSeekService;
    @Autowired
    private EmbeddingService embeddingService;
    @Autowired
    private MetadataService metadataService;

    /**
     * 建立对目标数据库的连接
     */
    @PostMapping("/connectToDatabase")
    public ResultVO connectToDatabase(String url, String username, String password) {
        try {
            return metadataService.initConnection(url, username, password);
        } catch (SQLException e) {
            return new ResultVO(ResultCode.ERROR);
        }
    }

    /**
     * 将数据库元数据向量化后存入milvus
     * @return
     */
    @PostMapping("/getDatabaseMetadata")
    public ResultVO getDatabaseMetadata(String url, String username, String password) {
        // 1. 获取数据库元数据
        try {
            metadataService.getDatabaseInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 2. 将元数据向量化
        List<String> vectorRecords = new ArrayList<>();
        vectorRecords
        embeddingService.batchEmbed(vectorRecords);

        // 3. 将向量化数据插入到Milvus
        milvusService

        return new ResultVO<>(null);
    }

    /**
     * 将用户输入的自然语言查询文本转化为sql
     * @param text 用户输入的自然语言查询文本
     * @return 生成的sql
     */
    public ResultVO genSQL(String text) {
        // 获取输入文本的向量
        List<Float> embedText = embeddingService.embed(text);

        List<> 表名 = milvusService.search(embedText);

        deepSeekService.genSQL();
        return null;
    }
}
