package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String FILENAME = "data.csv";
    public static final String[] COLUMN_MAPPING = {"id", "firstName", "lastName", "country", "age"};

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
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Employee> list = parseCSV(COLUMN_MAPPING, FILENAME);
        listToJson(list);
    }
}