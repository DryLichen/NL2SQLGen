package com.shrcb.NL2SQLGen.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 表元数据
 */
@Data
@AllArgsConstructor
public class TableMetadata {
    private String tableName;
    private String tableSchema;
    private String tableType;
    private String remarks;
    private List<ColumnMetadata> columns;
    private List<PrimaryKeyMetadata> primaryKeys;
    private List<ForeignKeyMetadata> foreignKeys;

    public TableMetadata(String tableName, String tableSchema, String tableType, String remarks) {
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.tableType = tableType;
        this.remarks = remarks;
    }
}


