package com.shrcb.NL2SQLGen.util;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

public class SQLValidator {

    public boolean validateSyntax(String sql) {
        try {
            Statement stmt = CCJSqlParserUtil.parse(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 进一步的字段存在性检查可以用解析后遍历字段名，与之前检索到的 TableInfo 对比

}
