package com.uon.uonwams.models;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class CSVFile {
    private final String pathname;
    private List<String> header;
    private List<LinkedHashMap<String, String>> data;

//    public static void main(String[] args) throws Exception {
//        CSVFile file = new CSVFile("files/user - Copy.csv");
////        List<HashMap<String, String>> myData = file.getData();
////        List<String> myHeader = file.getHeader();
////        System.out.println("data: " + myData);
////        System.out.println("name: " + myData.getFirst().get("name"));
////        System.out.println("header: " + myHeader);
//
////        UserData userData = new UserData();
////        insertRecord("files/new_file.csv", UserData.users.getFirst().toHashMap());
////        insertRecord("files/user - Copy.csv", UserData.users.getFirst().toHashMap());
//
////        ActivityData activityData = new ActivityData();
////        insertRecord("files/activity - Copy.csv", ActivityData.activities.get(10).toHashMap());
////        readRecord("files/activity - Copy.csv");
//
////        User temp = new User(3, "Tom Jerry", "mypassword", "test@mail.com");
////        file.updateRecord(temp.toHashMap(), "userId");
//
//        User temp = new User(3, "Rishi Sunak", "$2b$12$Hd6cMwb1WR6DHD5yhwL/E.BB1fMuC8jALHJTdr0TtfORSgr/i9w6C", "w.suthika@gmail.com");
//        file.deleteRecord(temp.toHashMap(), "userId");
//    }

    public CSVFile(String pathname) {
        this.pathname = pathname;
        readRecord();
//        this.rowCount = this.data.size();
    }

    public List<String> getHeader() {
        return header;
    }

    public List<LinkedHashMap<String, String>> getData() {
        return data;
    }

    public void readRecord()
    {
        // Ref: https://www.geeksforgeeks.org/reading-csv-file-java-using-opencsv/
        List<LinkedHashMap<String, String>> records = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(this.pathname);
            CSVReader reader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = reader.readNext()) != null) {
                LinkedHashMap<String, String> recordMap = new LinkedHashMap<String, String>();
                List<String> header = new ArrayList<>();
                int index = 0;
                for (String cell : nextRecord) {
                    if (this.header == null) {
                        header.add(cell);
                    } else {
                        String key = this.header.get(index);
                        recordMap.put(key, cell);
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
//        this.latestId = Integer.parseInt(records.getLast().get(this.header.getFirst()));
        this.data = records;
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

            this.data.add(record);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(LinkedHashMap<String, String> record, String key) {
        // Ref: https://stackoverflow.com/questions/4397907/updating-specific-cell-csv-file-using-java
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

    public void deleteRecord(int id, String key) {
        Integer replaceRow = null;
        try {
            // delete in csv
            File file = new File(this.pathname);
            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();

            for (int row = 1; row < this.data.size() + 1; row++) {
                if (replaceRow == null && csvBody.get(row)[0].equals(Integer.toString(id))) {
                    replaceRow = row;
                }
                if (replaceRow != null) {
                    if (row == this.data.size()) {
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
