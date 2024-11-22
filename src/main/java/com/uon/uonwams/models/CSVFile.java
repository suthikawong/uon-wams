package com.uon.uonwams.models;

import com.uon.uonwams.data.ActivityData;
import com.uon.uonwams.data.UserData;

import java.io.*;
import java.util.*;

public class CSVFile {
    private List<String> header;
    private List<HashMap<String, String>> data;

    public static void main(String[] args) throws Exception {
//        CSVFile file = new CSVFile("files/user.csv");
//        List<HashMap<String, String>> myData = file.getData();
//        List<String> myHeader = file.getHeader();
//        System.out.println("data: " + myData);
//        System.out.println("name: " + myData.getFirst().get("name"));
//        System.out.println("header: " + myHeader);

        UserData userData = new UserData();
        insertRecord("files/user.csv", UserData.users.getFirst().toHashMap());

        ActivityData activityData = new ActivityData();
        insertRecord("files/activity.csv", ActivityData.activities.getFirst().toHashMap());
    }

    public CSVFile(String pathname) throws FileNotFoundException {
        this.data = getRecordsFromCSVFile(pathname);
    }

    public List<String> getHeader() {
        return header;
    }

    public List<HashMap<String, String>> getData() {
        return data;
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
            String tempValue = "";
            while (rowScanner.hasNext()) {
                String value = rowScanner.next();

                if (this.header == null) {
                    header.add(value);
                    continue;
                }

                if (!tempValue.isBlank() && value.endsWith("\"")) {
                    tempValue += value;
                    obj.put(this.header.get(index), tempValue);
                    tempValue = "";
                    index++;
                } else if (value.startsWith("\"") || !tempValue.isBlank()) {
                    tempValue += value + ",";
                } else {
                    obj.put(this.header.get(index), value);
                    index++;
                }
            }
        }
        if (this.header == null) {
            this.header = header;
        }
        return obj;
    }

    public static void insertRecord(String pathname, LinkedHashMap<String, String> record) throws IOException {
        StringBuilder data = new StringBuilder();
        Iterator<String> it = record.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            data.append(record.get(key));
            if (it.hasNext()) {
                data.append(',');
            }
        }
        data.append("\n");

        // Ref: https://www.baeldung.com/java-append-to-file
        FileWriter fw = new FileWriter(pathname, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(String.valueOf(data));
        bw.close();
    }

    public void updateRecord(String pathname, LinkedHashMap<String, String> record) {
        // update in csv
    }

    public void deleteRecord() {
        // delete in csv
    }
}
