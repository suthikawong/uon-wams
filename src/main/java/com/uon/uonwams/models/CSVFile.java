/**
 Program: UON WAMS Application
 Filename: CSVFile.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

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

    /**
     Reading a CSV file in Java using OpenCSV. [source code]. Available
     from: https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv [Accessed 22th November 2024].
     */
    // read all data from CSV file
    public void readRecords() {
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

    /**
     Writing a CSV file in Java using OpenCSV. [source code]. Available
     from: https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv [Accessed 22th November 2024].
     */
    public void insertRecord(LinkedHashMap<String, String> record)
    {
        try {
            File file = new File(this.pathname);
            List<String> data = new ArrayList<>();
            Iterator<String> it = record.keySet().iterator();

            FileWriter outputFile = new FileWriter(file, true);
            CSVWriter writer = new CSVWriter(outputFile);

            while (it.hasNext()) {
                String key = it.next();
                data.add(record.get(key));
            }
            writer.writeNext(data.toArray(new String[0]));
            writer.close();

            this.data.add(record);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     updating specific cell csv file using java. [source code]. Available
     from: https://stackoverflow.com/questions/4397907/updating-specific-cell-csv-file-using-java [Accessed 22th November 2024].
     */
    public void updateRecord(LinkedHashMap<String, String> record, String key) {
        Integer replaceRow = null;
        try {
            File file = new File(this.pathname);
            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();

            // get CSV row column  and replace with by using row and column
            for (int row = 1; row < this.data.size() + 1; row++) {
                if (csvBody.get(row)[0].equals(record.get(key))) {
                    for (int col = 0; col < this.header.size(); col++) {
                        csvBody.get(row)[col] = record.get(this.header.get(col));
                    }
                    replaceRow = row;
                    break;
                }
            }
            reader.close();

            if (replaceRow == null) {
                System.out.println("Not Found ID");
                return;
            }

            // Write to CSV file which is open
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (replaceRow == null) return;
        this.data.set(replaceRow - 1, record);
    }

    public void deleteRecord(int id) {
        Integer replaceRow = null;
        try {
            // delete in csv
            File file = new File(this.pathname);
            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();

            for (int row = 1; row < this.data.size() + 1; row++) {
                if(csvBody.get(row)[0].equals(Integer.toString(id))){
                    replaceRow = row;
                    csvBody.remove(csvBody.get(row));
                    break;
                }
            }
            reader.close();

            if (replaceRow == null) {
                System.out.println("Not Found ID");
                return;
            }

            // Write to CSV file which is open
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();

            this.data.remove(replaceRow - 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
