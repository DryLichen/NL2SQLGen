package com.shrcb.NL2SQLGen.service.impl;

import com.shrcb.NL2SQLGen.enums.ResultCode;
import com.shrcb.NL2SQLGen.model.entity.*;
import com.shrcb.NL2SQLGen.model.vo.ResultVO;
import com.shrcb.NL2SQLGen.service.MetadataService;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetadataServiceImpl implements MetadataService {

    private Connection connection;

    /**
     * 尝试和数据库建立连接
     * @param url
     * @param username
     * @param password
     * @return 建立连接的状态，成功为true, 失败为false
     * @throws SQLException
     */
    public ResultVO initConnection(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);

        if (connection != null) {
            return new ResultVO(ResultCode.SUCCESS);
        } else {
            return new ResultVO<>(ResultCode.ERROR);
        }
    }

    /**
     * 获取数据库基本信息
     */
    public Map<String, Object> getDatabaseInfo() throws SQLException {
        Map<String, Object> dbInfo = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();

        dbInfo.put("database_product_name", metaData.getDatabaseProductName());
        dbInfo.put("database_product_version", metaData.getDatabaseProductVersion());
        dbInfo.put("driver_name", metaData.getDriverName());
        dbInfo.put("driver_version", metaData.getDriverVersion());
        dbInfo.put("jdbc_version", metaData.getJDBCMajorVersion() + "." + metaData.getJDBCMinorVersion());

        return dbInfo;
    }

    /**
     * 获取所有表的元数据信息
     */
    public List<TableMetadata> getTablesMetadata() throws SQLException {
        List<TableMetadata> tables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        // 获取所有表
        ResultSet tablesResult = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (tablesResult.next()) {
            String tableName = tablesResult.getString("TABLE_NAME");
            String tableSchema = tablesResult.getString("TABLE_SCHEM");
            String tableType = tablesResult.getString("TABLE_TYPE");
            String remarks = tablesResult.getString("REMARKS");

            TableMetadata table = new TableMetadata(tableName, tableSchema, tableType, remarks);

            // 获取表的列信息
            table.setColumns(getColumnsMetadata(tableSchema, tableName));

            // 获取表的主键信息
            table.setPrimaryKeys(getPrimaryKeysMetadata(tableSchema, tableName));

            // 获取表的外键信息
            table.setForeignKeys(getForeignKeysMetadata(tableSchema, tableName));

            tables.add(table);
        }

        tablesResult.close();
        return tables;
    }

    /**
     * 获取表的列信息
     */
    private List<ColumnMetadata> getColumnsMetadata(String schema, String tableName)
            throws SQLException {
        List<ColumnMetadata> columns = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet columnsResult = metaData.getColumns(null, schema, tableName, "%");

        while (columnsResult.next()) {
            String columnName = columnsResult.getString("COLUMN_NAME");
            String dataType = columnsResult.getString("TYPE_NAME");
            int columnSize = columnsResult.getInt("COLUMN_SIZE");
            int decimalDigits = columnsResult.getInt("DECIMAL_DIGITS");
            boolean isNullable = "YES".equals(columnsResult.getString("IS_NULLABLE"));
            String columnDefault = columnsResult.getString("COLUMN_DEF");
            String remarks = columnsResult.getString("REMARKS");

            ColumnMetadata column = new ColumnMetadata(
                    columnName, dataType, columnSize, decimalDigits,
                    isNullable, columnDefault, remarks
            );

            columns.add(column);
        }

        columnsResult.close();
        return columns;
    }

    /**
     * 获取表的主键信息
     */
    private List<PrimaryKeyMetadata> getPrimaryKeysMetadata(String schema, String tableName)
            throws SQLException {
        List<PrimaryKeyMetadata> primaryKeys = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet pkResult = metaData.getPrimaryKeys(null, schema, tableName);

        while (pkResult.next()) {
            String columnName = pkResult.getString("COLUMN_NAME");
            String pkName = pkResult.getString("PK_NAME");
            short keySeq = pkResult.getShort("KEY_SEQ");

            PrimaryKeyMetadata pk = new PrimaryKeyMetadata(columnName, pkName, keySeq);
            primaryKeys.add(pk);
        }

        pkResult.close();
        return primaryKeys;
    }

    /**
     * 获取表的外键信息
     */
    private List<ForeignKeyMetadata> getForeignKeysMetadata(String schema, String tableName)
            throws SQLException {
        List<ForeignKeyMetadata> foreignKeys = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet fkResult = metaData.getImportedKeys(null, schema, tableName);

        while (fkResult.next()) {
            String fkColumnName = fkResult.getString("FKCOLUMN_NAME");
            String pkTableName = fkResult.getString("PKTABLE_NAME");
            String pkColumnName = fkResult.getString("PKCOLUMN_NAME");
            String fkName = fkResult.getString("FK_NAME");
            short keySeq = fkResult.getShort("KEY_SEQ");

            ForeignKeyMetadata fk = new ForeignKeyMetadata(
                    fkColumnName, pkTableName, pkColumnName, fkName, keySeq
            );
            foreignKeys.add(fk);
        }

        fkResult.close();
        return foreignKeys;
    }

    /**
     * 获取索引信息
     */
    public List<IndexMetadata> getIndexesMetadata(String schema, String tableName)
            throws SQLException {
        List<IndexMetadata> indexes = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet indexResult = metaData.getIndexInfo(null, schema, tableName, false, false);

        while (indexResult.next()) {
            String indexName = indexResult.getString("INDEX_NAME");
            String columnName = indexResult.getString("COLUMN_NAME");
            boolean nonUnique = indexResult.getBoolean("NON_UNIQUE");
            short type = indexResult.getShort("TYPE");
            String ascOrDesc = indexResult.getString("ASC_OR_DESC");

            IndexMetadata index = new IndexMetadata(
                    indexName, columnName, nonUnique, type, ascOrDesc
            );
            indexes.add(index);
        }

        indexResult.close();
        return indexes;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
