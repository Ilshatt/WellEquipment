package repository;

import model.Well;
import service.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WellRepository {

    private static final String tableName = "Well";

    public static void createTable() throws SQLException {
        DatabaseService.getInstance().createTable(
                tableName,
                new String[]{
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT",
                        "name TEXT NOT NULL UNIQUE"
                });
    }

    public static void insertRecord(Well well) throws SQLException {
        DatabaseService.getInstance().insertRecord(
                tableName,
                new String[]{"name"},
                new String[]{well.getName()});
    }

    public static void updateRecord(Well well) throws SQLException {
        DatabaseService.getInstance().updateRecord(
                tableName,
                new String[]{"name = " + well.getName()},
                new String[]{"id = " + well.getId()});
    }

    public static void deleteRecord(Well well) throws SQLException {
        DatabaseService.getInstance().deleteRecord(
                tableName,
                new String[]{"id = " + well.getId()}
        );
    }

    public static ArrayList<Well> selectAll() {
        ArrayList<Well> wellArrayList = new ArrayList<>();
        try {
            ResultSet resultSet = DatabaseService.getInstance().selectRecords(
                    tableName, new String[]{"id", "name"}, null);
            while (resultSet.next()) {
                wellArrayList.add(new Well(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wellArrayList;
    }

    public static Well selectByName(String name) throws SQLException {
        ResultSet resultSet = DatabaseService.getInstance().selectRecords(
                tableName,
                new String[]{"id", "name"},
                new String[]{"name = '" + name + "'"}
        );
        if (resultSet.next()) {
            return new Well(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            );
        }
        return null;
    }
}