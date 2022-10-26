package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String FILE_NAME_CSV = "data.csv";
    public static final String FILE_NAME_XML = "data.xml";
    public static final String[] COLUMN_MAPPING = {"id", "firstName", "lastName", "country", "age"};
    public static int coutFiles = 1;

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> employeeList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> bean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            employeeList = bean.parse();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return employeeList;
    }

    public static void listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        writeString(json);
    }

    public static void writeString(String json) {
        try (FileWriter file = new FileWriter("data" + coutFiles + ".json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public static List<Employee> parseXML(String file) {
        List<Employee> employeeList = new ArrayList<Employee>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(FILE_NAME_XML));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    NodeList node1 = element.getElementsByTagName("id");
                    String id = node1.item(0).getTextContent();
                    NodeList node2 = element.getElementsByTagName("firstName");
                    String firstName = node2.item(0).getTextContent();
                    NodeList node3 = element.getElementsByTagName("lastName");
                    String lastName = node3.item(0).getTextContent();
                    NodeList node4 = element.getElementsByTagName("country");
                    String country = node4.item(0).getTextContent();
                    NodeList node5 = element.getElementsByTagName("age");
                    String age = node5.item(0).getTextContent();
                    employeeList.add(new Employee(Long.parseLong(id), firstName, lastName, country, Integer.parseInt(age)));
                }
            }

        } catch (Exception ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        return employeeList;
    }


    public static void main(String[] args) {
        List<Employee> listCSV = parseCSV(COLUMN_MAPPING, FILE_NAME_CSV);
        listToJson(listCSV);
        coutFiles++;
        List<Employee> listXML = parseXML(FILE_NAME_XML);
        listToJson(listXML);
    }
}