package com.shrcb.NL2SQLGen.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 主键元数据
 */
@Data
@AllArgsConstructor
public class PrimaryKeyMetadata {
    private String columnName;
    private String pkName;
    private short keySeq;
}
