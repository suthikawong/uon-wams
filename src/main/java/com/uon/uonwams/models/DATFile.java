/**
 Program: UON WAMS Application
 Filename: DATFile.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// create DATFileStructure abstract class to ensure that child classes implement getId method
// This will allow DATFile class to work with multiple class
abstract class DATFileStructure {
    public abstract int getId();
}

// create DATFile class as a generic class
// the instance that was pass as a parameter must have getId method
public class DATFile<T extends DATFileStructure> {
    private final String pathname;
    private List<T> data;

    // when initialized, read all data from the DAT file
    public DATFile(String pathname) {
        this.pathname = pathname;
        readRecords();
    }

    public List<T> getData() {
        return data;
    }

    // read all data from DAT file
    public void readRecords() {
        try
        {
            File f = new File(this.pathname);
            // validate the file does not exist
            if (!f.exists()) {
                System.out.println("File does not exist");
                return;
            }
            // assign empty list when there are no data in the file
            if (f.length() == 0) {
                this.data = new ArrayList<>();
                return;
            }
            FileInputStream inStream =  new FileInputStream(f);
            ObjectInputStream objectInputFile = new ObjectInputStream(inStream);
            // store data in DAT file into this.data
            this.data = (List<T>) objectInputFile.readObject();
            objectInputFile.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // insert a data into a DAT file
    public void insertRecord(T record) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            // add a new record to the list
            list.add(record);

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            // write data to the DAT file
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            // assign new data to this.data
            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // insert multiple data into a DAT file
    public void insertRecords(List<T> records) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            // concat new records to the list
            list = Stream.concat(list.stream(), records.stream()).toList();

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            // write data to the DAT file
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            // assign new data to this.data
            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // update a specific data in the DAT file
    public void updateRecord(T record) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            Integer updateIndex = null;
            // iterate through the list to find the data that their id match
            for (int i=0; i<list.size(); i++) {
                if (list.get(i).getId() == record.getId()) {
                    // update data
                    list.set(i, record);
                    updateIndex = i;
                    break;
                }
            }
            // if there is no matching id, exit the function and log to the terminal
            if (updateIndex == null) {
                System.out.println("Not Found ID");
                return;
            }

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            // write data to the DAT file
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            // assign new data to this.data
            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // delete a specific data in the DAT file
    public void deleteRecord(int id) {
        try {
            List<T> list = new ArrayList<T>(this.data);
            Integer deleteIndex = null;
            // iterate through the list to find the data that their id match
            for (int i=0; i<list.size(); i++) {
                if (list.get(i).getId() == id) {
                    // remove data
                    list.remove(i);
                    deleteIndex = i;
                    break;
                }
            }
            // if there is no matching id, exit the function and log to the terminal
            if (deleteIndex == null) {
                System.out.println("Not Found ID");
                return;
            }

            FileOutputStream outStream = new FileOutputStream(this.pathname);
            ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);
            // write data to the DAT file
            objectOutputFile.writeObject(list);
            objectOutputFile.close();

            // assign new data to this.data
            this.data = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
