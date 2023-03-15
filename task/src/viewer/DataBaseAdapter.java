package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseAdapter {
    SQLiteDataSource ds = new SQLiteDataSource();
    Connection con;

    public boolean OpenDataSource(String url) {
        ds.setUrl(url);
        try {
            con = ds.getConnection();
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        //InitDatabase();
        return true;
    }

    public void InitDatabase1() {
        try (Statement statement = con.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS contacts (
                        contact_id INTEGER PRIMARY KEY,
                        first_name TEXT NOT NULL,
                        last_name TEXT NOT NULL,
                        email TEXT NOT NULL UNIQUE,
                        phone TEXT NOT NULL UNIQUE
                    );
                    """);
            statement.execute("DELETE FROM contacts");
            statement.execute("INSERT INTO contacts VALUES(1, 'Sharmin', 'Pittman', 'sharmin@gmail.com', '202-555-0140')");
            statement.execute("INSERT INTO contacts VALUES(2, 'Fred', 'Hood', 'fred@gmail.com', '202-555-0175')");
            statement.execute("INSERT INTO contacts VALUES(3, 'Emeli', 'Ortega', 'emeli@gmail.com', '202-555-0138')");


            statement.execute("""
                    CREATE TABLE IF NOT EXISTS groups (
                        group_id INTEGER PRIMARY KEY,
                        name TEXT NOT NULL
                    );
                    """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void InitDatabase2() {
        try (Statement statement = con.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS projects (
                        id integer PRIMARY KEY,
                        name text NOT NULL,
                        begin_date text,
                        end_date text
                    );
                    """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDBTables() {
        List<String> result = new ArrayList<>();
        try (Statement statement = con.createStatement()) {
            try (ResultSet tables = statement.executeQuery("""
                    SELECT name
                    FROM sqlite_master
                    WHERE type ='table'
                      AND name NOT LIKE 'sqlite_%'
                    """)) {
                while (tables.next()) {
                    result.add(tables.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TableData ExecuteSQL(String SQL) throws SQLException {
        TableData result;
        try (Statement statement = con.createStatement()) {
            try(ResultSet resultSet = statement.executeQuery(SQL)) {
                result = new TableData(resultSet);
            }
        }
        return result;
    }

}
