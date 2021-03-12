package com.example.project007;

import java.io.Serializable;

public class Trails implements Serializable {
    private String trail_title;
    private String date;
    private String type;
    private String time;
    private String success;
    private String failure;
    private String VariesData;

    //for Binomial Trails
    public Trails(String trail_title, String date, String type, String time, String success, String failure) {
        this.trail_title = trail_title;
        this.date = date;
        this.type = type;
        this.time = time;
        this.success = success;
        this.failure = failure;
    }
    //for Three other Trails
    public Trails(String trail_title, String date, String type, String time, String variesData) {
        this.trail_title = trail_title;
        this.date = date;
        this.type = type;
        this.time = time;
        VariesData = variesData;
    }

    public String getTrail_title() {
        return trail_title;
    }

    public void setTrail_title(String trail_title) {
        this.trail_title = trail_title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public String getVariesData() {
        return VariesData;
    }

    public void setVariesData(String variesData) {
        VariesData = variesData;
    }
}
