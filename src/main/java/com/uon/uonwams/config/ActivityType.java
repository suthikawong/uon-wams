/**
 Program: UON WAMS Application
 Filename: ActivityType.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.config;

public enum ActivityType {
    ATSR("ATSR"),
    TLR("TLR"),
    SA("SA"),
    OTHER("Other");

    public final String label;

    private ActivityType(String label) {
        this.label = label;
    }
}
