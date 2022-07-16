package com.stuffer.stuffers.models;

public class ListCountry {
    String name;
    String flagPath;

    public ListCountry(String name, String flagPath) {
        this.name = name;
        this.flagPath = flagPath;
    }

    public String getName() {
        return name;
    }

    public String getFlagPath() {
        return flagPath;
    }
}
