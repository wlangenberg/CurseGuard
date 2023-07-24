package dev.spaxter.curseguard.models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryResult extends ArrayList<ArrayList<Map<String, Object>>> {
    public QueryResult(final ResultSet resultSet) throws SQLException {
        ResultSetMetaData metadata = resultSet.getMetaData();
        ArrayList<ArrayList<Map<String, Object>>> resultList = new ArrayList<ArrayList<Map<String, Object>>>();

        while (resultSet.next()) {
            ArrayList<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
            Map<String, Object> columns = new HashMap<String, Object>();
            for (int i = 0; i < metadata.getColumnCount(); i++) {
                columns.put(metadata.getColumnName(i + 1), resultSet.getObject(i + 1));
            }
            row.add(columns);
            resultList.add(row);
        }

        this.addAll(resultList);
    }
}
