package com.uon.uonwams.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

abstract class DATFileStructure {
    public abstract int getId();
}

public class DATFile<T extends DATFileStructure> {
    private final String pathname;
    private List<T> data;

    public DATFile(String pathname) {
        this.pathname = pathname;
        readRecord();
    }

    public List<T> getData() {
        return data;
    }

    public void readRecord() {
        try
        {
            File f = new File(this.pathname);
            if (!f.exists()) {
                System.out.println("File does not exist");
                return;
            }
            if (f.length() == 0) {
                this.data = new ArrayList<>();
                return;
            }
            FileInputStream inStream =  new FileInputStream(f);
            ObjectInputStream objectInputFile = new ObjectInputStream(inStream);
            this.data = (List<T>) objectInputFile.readObject();
            objectInputFile.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertRecord(T record) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            list.add(record);

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertRecords(List<T> records) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            list = Stream.concat(list.stream(), records.stream()).toList();

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(T record) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            Integer updateIndex = null;
            for (int i=0; i<list.size(); i++) {
                if (list.get(i).getId() == record.getId()) {
                    list.set(i, record);
                    updateIndex = i;
                    break;
                }
            }
            if (updateIndex == null) {
                System.out.println("Not Found ID");
                return;
            }

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(int id) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            Integer deleteIndex = null;
            for (int i=0; i<list.size(); i++) {
                if (list.get(i).getId() == id) {
                    list.remove(i);
                    deleteIndex = i;
                    break;
                }
            }
            if (deleteIndex == null) {
                System.out.println("Not Found ID");
                return;
            }

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
