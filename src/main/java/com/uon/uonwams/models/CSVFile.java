package com.uon.uonwams.models;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class CSVFile {
    private final String pathname;
    private List<String> header;
    private List<LinkedHashMap<String, String>> data;

    public CSVFile(String pathname) {
        this.pathname = pathname;
        readRecords();
    }

    public List<LinkedHashMap<String, String>> getData() {
        return data;
    }

    // read all data from CSV file
    public void readRecords() {
        // Ref: https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv/
        List<LinkedHashMap<String, String>> records = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(this.pathname);
            CSVReader reader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = reader.readNext()) != null) {
                LinkedHashMap<String, String> recordMap = new LinkedHashMap<String, String>();
                List<String> header = new ArrayList<>();
                // create index variable to map cell value and header key
                int index = 0;
                for (String cell : nextRecord) {
                    // if header not exists, add the cell to header list
                    if (this.header == null) {
                        header.add(cell);
                    } else {
                        String key = this.header.get(index);
                        recordMap.put(key, cell);
                        index++;
                    }
                }
                if (this.header == null) {
                    this.header = header;
                }
                // skip when there are no data
                if (recordMap.isEmpty()) continue;
                // add hash map to the list
                records.add(recordMap);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // assign the hash map list to this.data
        this.data = records;
    }
}
