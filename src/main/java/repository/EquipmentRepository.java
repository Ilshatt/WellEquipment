package repository;

import model.Equipment;
import model.Well;
import service.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EquipmentRepository {

    private static final String tableName = "Equipment";

    public static void createTable() throws SQLException {
        DatabaseService.getInstance().createTable(
                tableName,
                new String[]{
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT",
                        "name TEXT NOT NULL UNIQUE",
                        "well_id INTEGER DEFAULT NULL REFERENCES well(id)"
                });
    }

    public static void insertRecord(Equipment equipment) throws SQLException {
        DatabaseService.getInstance().insertRecord(
                tableName,
                new String[]{"name", "well_id"},
                new String[]{equipment.getName(),
                        String.valueOf(equipment.getWell().getId())});
    }

    public static void updateRecord(Equipment equipment) throws SQLException {
        DatabaseService.getInstance().updateRecord(
                tableName,
                new String[]{"name = " + equipment.getName(),
                        "well_id = " + equipment.getWell().getId()},
                new String[]{"id = " + equipment.getId()});
    }

    public static void deleteRecord(Equipment equipment) throws SQLException {
        DatabaseService.getInstance().deleteRecord(
                tableName,
                new String[]{"id = " + equipment.getId()}
        );
    }

    public static ArrayList<Equipment> selectByWell(Well well) {
        ArrayList<Equipment> equipmentArrayList = new ArrayList<>();
        try {
            ResultSet resultSet = DatabaseService.getInstance().selectRecords(
                    tableName, new String[]{"id", "name"},
                    new String[]{"well_id = " + well.getId()});
            while (resultSet.next()) {
                equipmentArrayList.add(new Equipment(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        well
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentArrayList;
    }

    public static int getCount(Well well) throws SQLException {
        return DatabaseService.getInstance().getCount(
                tableName,
                new String[]{"well_id = " + well.getId()}
        );
    }
}