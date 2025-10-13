package com.shrcb.NL2SQLGen.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Filed元数据
 */
@Data
@AllArgsConstructor
public class ColumnMetadata {
    private String columnName;
    private String dataType;
    private int columnSize;
    private int decimalDigits;
    private boolean isNullable;
    private String columnDefault;
    private String remarks;

}
