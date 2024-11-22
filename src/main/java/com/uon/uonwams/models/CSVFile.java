package com.uon.uonwams.models;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;

import java.io.*;
import java.util.*;

public class CSVFile {
    private List<String> header;
    private List<HashMap<String, String>> data;

    public static void main(String[] args) throws Exception {
//        CSVFile file = new CSVFile("files/user - Copy.csv");
//        List<HashMap<String, String>> myData = file.getData();
//        List<String> myHeader = file.getHeader();
//        System.out.println("data: " + myData);
//        System.out.println("name: " + myData.getFirst().get("name"));
//        System.out.println("header: " + myHeader);

        UserData userData = new UserData();
//        insertRecord("files/new_file.csv", UserData.users.getFirst().toHashMap());
        insertRecord("files/user - Copy.csv", UserData.users.getFirst().toHashMap());
//        updateRecord("files/user - Copy.csv", "Toris Johnson", 3, 1);

        ActivityData activityData = new ActivityData();
        insertRecord("files/activity - Copy.csv", ActivityData.activities.get(10).toHashMap());
//        readRecord("files/activity - Copy.csv");
    }

    public CSVFile(String pathname) throws FileNotFoundException {
        this.data = readRecord(pathname);
    }

    public List<String> getHeader() {
        return header;
    }

    public List<HashMap<String, String>> getData() {
        return data;
    }

    public List<HashMap<String, String>> readRecord(String pathname)
    {
        // Ref: https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv/
        List<HashMap<String, String>> records = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(pathname);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                HashMap<String, String> recordMap = new HashMap<String, String>();
                List<String> header = new ArrayList<>();
                int index = 0;
                for (String cell : nextRecord) {
                    if (this.header == null) {
                        header.add(cell);
                    } else {
                        recordMap.put(this.header.get(index), cell);
                        index++;
                    }
//                    System.out.print(cell + "\t");
                }
//                System.out.println();
                if (this.header == null) {
                    this.header = header;
                }
                if (recordMap.isEmpty()) continue;
                records.add(recordMap);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void insertRecord(String pathname, LinkedHashMap<String, String> record)
    {
        // Ref: https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
        try {
            File file = new File(pathname);
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateRecord(String pathname, String replace, int row, int col) throws IOException, CsvException {
        // Ref: https://stackoverflow.com/questions/4397907/updating-specific-cell-csv-file-using-java
        File inputFile = new File(pathname);

        // Read existing file
        CSVReader reader = new CSVReader(new FileReader(inputFile));
        List<String[]> csvBody = reader.readAll();
        System.out.println(csvBody.getFirst()[0]);
        // get CSV row column  and replace with by using row and column
        csvBody.get(row)[col] = replace;
        reader.close();

        // Write to CSV file which is open
        CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();
    }

    public void deleteRecord() {
        // delete in csv
    }
}
