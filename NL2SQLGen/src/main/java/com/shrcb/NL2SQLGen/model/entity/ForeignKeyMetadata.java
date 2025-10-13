package com.shrcb.NL2SQLGen.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 外键元数据
 */
@Data
@AllArgsConstructor
public class ForeignKeyMetadata {
    private String fkColumnName;
    private String pkTableName;
    private String pkColumnName;
    private String fkName;
    private short keySeq;
}
