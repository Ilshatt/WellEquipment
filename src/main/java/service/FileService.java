package service;

import model.Equipment;
import model.Well;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import repository.EquipmentRepository;
import repository.WellRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileService {

    private static FileService instance;

    private FileService() {
    }

    public static FileService getInstance() {
        if (instance == null) {
            instance = new FileService();
        }
        return instance;
    }

    public void export(String fileName) {
        XMLOutputter XMLOut = new XMLOutputter();
        XMLOut.setFormat(Format.getPrettyFormat());
        Element rootElement, wellElement, equipmentElement;
        rootElement  = new Element("dbinfo");
        Document document = new Document(rootElement);
        ArrayList<Well> wellArrayList;
        ArrayList<Equipment> equipmentArrayList;
        wellArrayList = WellRepository.selectAll();
        for (Well well : wellArrayList) {
            wellElement = new Element("well");
            wellElement.setAttribute("name", well.getName());
            wellElement.setAttribute("id", String.valueOf(well.getId()));
            equipmentArrayList = EquipmentRepository.selectByWell(well);
            for (Equipment equipment: equipmentArrayList) {
                equipmentElement = new Element("equipment");
                equipmentElement.setAttribute("name", equipment.getName());
                equipmentElement.setAttribute("id", String.valueOf(equipment.getId()));
                wellElement.addContent(equipmentElement);
            }
            rootElement.addContent(wellElement);
        }
        try {
            XMLOut.output(document, new FileOutputStream(fileName + ".xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
