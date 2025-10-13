package com.shrcb.NL2SQLGen.service;

import com.shrcb.NL2SQLGen.model.entity.TableMetadata;
import com.shrcb.NL2SQLGen.model.vo.ResultVO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 获取数据库元数据
 */
@Service
public interface MetadataService {

    ResultVO initConnection(String url, String username, String password) throws SQLException;

    Map<String, Object> getDatabaseInfo() throws SQLException;

    List<TableMetadata> getTablesMetadata() throws SQLException;

}
