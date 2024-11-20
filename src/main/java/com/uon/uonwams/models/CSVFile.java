package com.uon.uonwams.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFile {
    private List<String> header;
    private List<List<String>> data;

    public static void main(String[] args) throws FileNotFoundException {
        CSVFile file = new CSVFile("files/user.csv");
        List<List<String>> myData = file.getData();
        List<String> myHeader = file.getHeader();
        System.out.println("data: " + myData);
        System.out.println("header: " + myHeader);
    }

    public CSVFile(String pathname) throws FileNotFoundException {
        this.data = getRecordsFromCSVFile(pathname);
        this.header = this.data.getFirst();
        this.data.removeFirst();
    }

    public List<String> getHeader() {
        return this.header;
    }

    public List<List<String>> getData() {
        return this.data;
    }

    public List<List<String>> getRecordsFromCSVFile(String pathname) throws FileNotFoundException {
        // Ref: https://www.baeldung.com/java-csv-file-array
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(pathname))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records;
    }

    public List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                String value = rowScanner.next();
                values.add(value);
            }
        }
        return values;
    }
}
