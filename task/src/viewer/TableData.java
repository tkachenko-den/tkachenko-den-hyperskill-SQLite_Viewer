package viewer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableData  {
    public List<String> columns;
    public List<List<String>> data;

    public TableData(ResultSet resultSet) throws SQLException {

        columns = new ArrayList<>(resultSet.getMetaData().getColumnCount());
        for(int ii=0;ii<resultSet.getMetaData().getColumnCount();ii++) {
            columns.add(resultSet.getMetaData().getColumnName(ii+1));
        }

        data = new ArrayList<>();
        while(resultSet.next()) {
            List<String> row = new ArrayList<>(resultSet.getMetaData().getColumnCount());
            for(int ii=0;ii<resultSet.getMetaData().getColumnCount();ii++) {
                row.add(resultSet.getString(ii+1));
            }
            data.add(row);
        }
    }

    public TableData() {
        columns = new ArrayList<>(1);
        //columns.add("none");

        data = new ArrayList<>(1);
        //data.add(new ArrayList<String>(1){{add("none");}});
    }
}
