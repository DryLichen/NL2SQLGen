package com.shrcb.NL2SQLGen;

import com.shrcb.NL2SQLGen.service.MilvusService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MilvusTest {

    @Resource
    private MilvusService milvusService;

    @Test
    void testInsert() {
        // 插入数据到Milvus数据库

    }
}
