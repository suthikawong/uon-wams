package com.uon.uonwams.models;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class CSVFile {
    private String pathname;
    private List<String> header;
    private List<HashMap<String, String>> data;
    private int rowCount;

    public static void main(String[] args) throws Exception {
        CSVFile file = new CSVFile("files/user - Copy.csv");
//        List<HashMap<String, String>> myData = file.getData();
//        List<String> myHeader = file.getHeader();
//        System.out.println("data: " + myData);
//        System.out.println("name: " + myData.getFirst().get("name"));
//        System.out.println("header: " + myHeader);

//        UserData userData = new UserData();
//        insertRecord("files/new_file.csv", UserData.users.getFirst().toHashMap());
//        insertRecord("files/user - Copy.csv", UserData.users.getFirst().toHashMap());

//        ActivityData activityData = new ActivityData();
//        insertRecord("files/activity - Copy.csv", ActivityData.activities.get(10).toHashMap());
//        readRecord("files/activity - Copy.csv");

//        User temp = new User(3, "Tom Jerry", "mypassword", "test@mail.com");
//        file.updateRecord(temp.toHashMap(), "userId");

        User temp = new User(3, "Rishi Sunak", "$2b$12$Hd6cMwb1WR6DHD5yhwL/E.BB1fMuC8jALHJTdr0TtfORSgr/i9w6C", "w.suthika@gmail.com");
        file.deleteRecord(temp.toHashMap(), "userId");
    }

    public CSVFile(String pathname) {
        this.pathname = pathname;
        this.data = readRecord();
        this.rowCount = this.data.size();
    }

    public List<String> getHeader() {
        return header;
    }

    public List<HashMap<String, String>> getData() {
        return data;
    }

    public List<HashMap<String, String>> readRecord()
    {
        // Ref: https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv/
        List<HashMap<String, String>> records = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(this.pathname);
            CSVReader reader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = reader.readNext()) != null) {
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
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public void insertRecord(LinkedHashMap<String, String> record)
    {
        // Ref: https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(LinkedHashMap<String, String> record, String key) {
        // Ref: https://stackoverflow.com/questions/4397907/updating-specific-cell-csv-file-using-java
        try {
            File file = new File(this.pathname);
            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();
            boolean isFound = false;

            // get CSV row column  and replace with by using row and column
            for (int row = 1; row < this.rowCount + 1; row++) {
                if (csvBody.get(row)[0].equals(record.get(key))) {
                    for (int col = 0; col < this.header.size(); col++) {
                        csvBody.get(row)[col] = record.get(this.header.get(col));
                    }
                    isFound = true;
                    break;
                }
            }
            reader.close();

            if (!isFound) {
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
    }

    public void deleteRecord(LinkedHashMap<String, String> record, String key) {
        try {
            // delete in csv
            File file = new File(this.pathname);
            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();
            boolean isFound = false;

            for (int row = 1; row < this.rowCount + 1; row++) {
                if (!isFound && csvBody.get(row)[0].equals(record.get(key))) {
                    isFound = true;
                }
                if (isFound) {
                    if (row == this.rowCount) {
                        for (int col = 0; col < this.header.size(); col++) {
                            csvBody.get(row)[col] = "";
                        }
                    } else {
                        for (int col = 0; col < this.header.size(); col++) {
                            csvBody.get(row)[col] = csvBody.get(row + 1)[col];
                        }
                    }

                }
            }
            reader.close();

            if (!isFound) {
                System.out.println("Not Found ID");
                return;
            }

            // Write to CSV file which is open
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();

            this.rowCount--;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
