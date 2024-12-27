package com.uon.uonwams.configs;

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
