package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stuffer.stuffers.models.Country.Result;

import java.util.List;

public class CountryCodeResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }







}


