package com.shrcb.NL2SQLGen.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 索引元数据
 */
@Data
@AllArgsConstructor
public class IndexMetadata {
    private String indexName;
    private String columnName;
    private boolean nonUnique;
    private short type;
    private String ascOrDesc;
}
