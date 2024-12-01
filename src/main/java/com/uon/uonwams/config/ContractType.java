package com.uon.uonwams.config;

public enum ContractType {
    FULL_TIME("Full-time"),
    PART_TIME_1_0("Part-time 1.0"),
    PART_TIME_0_5("Part-time 0.5");

    public final String label;

    private ContractType(String label) {
        this.label = label;
    }
}
