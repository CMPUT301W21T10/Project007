package com.example.project007;

import java.io.Serializable;

public class Experiment implements Serializable {
    private String name;
    private String trail_title;
    private String description;
    private String date;
    private String type;
    private String time;
    private String success;
    private String failure;
    private String VariesData;
    private int ID;



    public Experiment(String name, String description, String date, String type) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    //binomial trail
    public Experiment(String trail_title, String time, String date, String success, String failure, String type) {
        this.trail_title = trail_title;
        this.date = date;
        this.time = time;
        this.type = type;
        this.success = success;
        this.failure = failure;
    }

    //for three other trails
    public Experiment(String name, String time, String date, String VariesData, String type) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.time = time;
        this.VariesData = VariesData;
    }

    public String getTrail_title() {
        return trail_title;
    }

    public void setTrail_title(String trail_title) {
        this.trail_title = trail_title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setVariesData(String VariesData) {
        this.VariesData = VariesData;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
