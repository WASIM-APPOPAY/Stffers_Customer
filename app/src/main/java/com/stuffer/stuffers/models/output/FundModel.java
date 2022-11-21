package com.stuffer.stuffers.models.output;

public class FundModel {
    String name;
    String url;

    public FundModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
