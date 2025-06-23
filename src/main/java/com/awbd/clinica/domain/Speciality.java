package com.awbd.clinica.domain;

public enum Speciality {
    ENDODONT("Endodont"),
    ORTODONT("Ortodont"),
    RADIOLOG("Radiolog"),
    STOMATOLOG("Stomatolog");

    private final String displayName;

    Speciality(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
