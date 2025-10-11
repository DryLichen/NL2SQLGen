package com.shrcb.NL2SQLGen;

import com.shrcb.NL2SQLGen.service.EmbeddingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class EmbeddingTest {

    @Resource
    private EmbeddingService embeddingService;

    @Test
    void testEmbed(){
        String text = "查询年龄大于25岁的用户姓名";
        List<Float> floatList = embeddingService.embed(text);
        System.out.println(floatList);
    }

    @Test
    void testBatchEmbed() {

    }
}
