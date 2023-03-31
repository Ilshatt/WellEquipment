import model.Equipment;
import model.Well;
import repository.EquipmentRepository;
import repository.WellRepository;
import service.DatabaseService;
import service.FileService;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        byte action;
        Scanner scanner = new Scanner(System.in);
        try {
            DatabaseService.getInstance().connect();
            WellRepository.createTable();
            EquipmentRepository.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        do {
            System.out.println("1 - создание оборудования на скважине");
            System.out.println("2 - вывод информации об оборудовании");
            System.out.println("3 - экспорт данных в формате xml");
            System.out.println("4 - завершение работы приложения");
            System.out.print("Выберите необходимое действие: ");
            action = scanner.nextByte();
            scanner.nextLine();
            switch (action) {
                case 1:
                    try {
                        System.out.print("Введите имя скважины: ");
                        String name = scanner.nextLine();
                        Well well = WellRepository.selectByName(name);
                        if (well == null) {
                            WellRepository.insertRecord(new Well(0, name));
                            well = WellRepository.selectByName(name);
                        }
                        System.out.print("Введите количество оборудования: ");
                        int count = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < count; i++) {
                            name = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                            Equipment equipment = new Equipment(0, name, well);
                            EquipmentRepository.insertRecord(equipment);
                        }
                        System.out.println("Оборудование успешно добавлено");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Well well;
                    int count;
                    System.out.print("Введите имена скважин через пробел: ");
                    String[] names = scanner.nextLine().split(" ");
                    try {
                        for (String name : names) {
                            well = WellRepository.selectByName(name);
                            count = well != null ? EquipmentRepository.getCount(well) : 0;
                            System.out.println(name + " " + count);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.print("Введите имя файла: ");
                    String fileName = scanner.nextLine();
                    FileService.getInstance().export(fileName);
                    System.out.println("Экспорт успешно завершён");
                    break;
                case 4:
                    System.out.println("Программа успешно завершена");
                    break;
                default:
                    System.out.println("Необходимо ввести целое число от 1 до 4!");
            }
        }
        while (action != 4);
        try {
            DatabaseService.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}