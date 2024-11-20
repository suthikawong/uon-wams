package com.uon.uonwams.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CSVFile {
    private List<String> header;
    private List<HashMap<String, String>> data;

    public static void main(String[] args) throws FileNotFoundException {
        CSVFile file = new CSVFile("files/user.csv");
        List<HashMap<String, String>> myData = file.getData();
        List<String> myHeader = file.getHeader();
        System.out.println("data: " + myData);
        System.out.println("name: " + myData.getFirst().get("name"));
        System.out.println("header: " + myHeader);
    }

    public CSVFile(String pathname) throws FileNotFoundException {
        this.data = getRecordsFromCSVFile(pathname);
    }

    public List<String> getHeader() {
        return header;
    }

    public List<HashMap<String, String>> getData() {
        return this.data;
    }

    public List<HashMap<String, String>> getRecordsFromCSVFile(String pathname) throws FileNotFoundException {
        // Ref: https://www.baeldung.com/java-csv-file-array
        List<HashMap<String, String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(pathname))) {
            while (scanner.hasNextLine()) {
                HashMap<String, String> obj = getRecordFromLine(scanner.nextLine());
                if (obj.isEmpty()) continue;
                records.add(obj);
            }
        }
        return records;
    }

    public HashMap<String, String> getRecordFromLine(String line) {
        HashMap<String, String> obj = new HashMap<String, String>();
        List<String> header = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            int index = 0;
            while (rowScanner.hasNext()) {
                String value = rowScanner.next();
                if (this.header == null) {
                    header.add(value);
                } else {
                    obj.put(this.header.get(index), value);
                }
                index++;
            }
        }
        if (this.header == null) {
            this.header = header;
        }
        return obj;
    }
}
