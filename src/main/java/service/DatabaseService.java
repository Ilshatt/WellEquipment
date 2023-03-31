package service;

import org.sqlite.JDBC;

import java.sql.*;

public class DatabaseService {

    private DatabaseService() {
    }

    private static DatabaseService instance;

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private static final String CONECTION_STRING = "jdbc:sqlite:test.db";

    private static Connection connection;

    public void connect() throws SQLException {
        connection = null;
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection(CONECTION_STRING);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void createTable(String name, String[] fields) throws SQLException {
        if (connection == null) {
            return;
        }
        String query = "CREATE TABLE if not exists %s (%s)";
        Statement statement = connection.createStatement();
        statement.execute(String.format(query, name,
                String.join(",", fields)));
    }

    public void insertRecord(String name, String[] fields, String[] values) throws SQLException {
        if (connection == null) {
            return;
        }
        String query = "INSERT INTO %s (%s) VALUES('%s')";
        Statement statement = connection.createStatement();
        statement.execute(String.format(query, name,
                String.join(",", fields),
                String.join("','", values)));
    }

    public void updateRecord(String name, String[] fields, String[] wheres) throws SQLException {
        if (connection == null) {
            return;
        }
        String query = "UPDATE %s SET %s WHERE %s";
        Statement statement = connection.createStatement();
        statement.execute(String.format(query, name,
                String.join(",", fields),
                String.join(" AND ", wheres)));
    }

    public void deleteRecord(String name, String[] wheres) throws SQLException {
        String query = "DELETE FROM %s WHERE %s";
        Statement statement = connection.createStatement();
        statement.execute(String.format(query, name,
                String.join(" AND ", wheres)));
    }

    public ResultSet selectRecords(String name, String[] fields, String[] wheres) throws SQLException {
        String query = "SELECT %s FROM %s";
        Statement statement = connection.createStatement();
        query = String.format(query,
                String.join(",", fields),
                name);
        if (wheres != null) {
            query += String.format(" WHERE %s", String.join(" AND ", wheres));
        }
        return statement.executeQuery(query);
    }

    public int getCount(String name, String[] wheres) throws SQLException {
        String query = "SELECT COUNT(*) FROM %s WHERE %s";
        Statement statement = connection.createStatement();
        return statement.executeQuery(String.format(query, name,
                String.join(" AND ", wheres))).getInt(1);
    }
}