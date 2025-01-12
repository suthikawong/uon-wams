/**
 Program: UON WAMS Application
 Filename: ActivityType.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ActivityType extends DATFileStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String name;
    protected LinkedHashMap<String, Double> formula;

    public ActivityType(int id, String name, LinkedHashMap<String, Double> formula) {
        this.id = id;
        this.name = name;
        this.formula = formula;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LinkedHashMap<String, Double> getFormula() {
        return formula;
    }

    @Override
    public String toString() {
        return "ActivityType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", formula=" + formula +
                '}';
    }
}
