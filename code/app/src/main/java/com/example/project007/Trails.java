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
    private Integer ID;
    private Location location;
    private boolean ignoreCondition;



    public boolean isIgnoreCondition() {
        return ignoreCondition;
    }

    public void setIgnoreCondition(boolean ignoreCondition) {
        this.ignoreCondition = ignoreCondition;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    //for Binomial Trails w.o location
    public Trails(String trail_title, String date, String type, String time, String success, String failure, Integer ID, boolean ignoreCondition) {
        this.trail_title = trail_title;
        this.date = date;
        this.type = type;
        this.time = time;
        this.success = success;
        this.failure = failure;
        if (ID != null){
            this.ID = ID;
        }
        else{
            this.ID = TrailsDatabaseController.generateTrailsId();
        }
        this.ignoreCondition = ignoreCondition;
    }

    //for Binomial Trails w/ location
    public Trails(String trail_title, String date, String type, String time, String success, String failure, Integer ID, Location location,  boolean ignoreCondition) {
        this.trail_title = trail_title;
        this.date = date;
        this.type = type;
        this.time = time;
        this.success = success;
        this.failure = failure;
        if (ID != null){
            this.ID = ID;
        }
        else{
            this.ID = TrailsDatabaseController.generateTrailsId();
        }
        this.location = location;
        this.ignoreCondition = ignoreCondition;
    }

    //for Three other Trails w.o location
    public Trails(String trail_title, String date, String type, String time, String variesData, Integer ID, boolean ignoreCondition) {
        this.trail_title = trail_title;
        this.date = date;
        this.type = type;
        this.time = time;
        this.VariesData = variesData;
        if (ID != null){
            this.ID = ID;
        }
        else{
            this.ID = TrailsDatabaseController.generateTrailsId();
        }
        this.ignoreCondition = ignoreCondition;
    }

    //for Three other Trails w/ location
    public Trails(String trail_title, String date, String type, String time, String variesData, Integer ID, Location location, boolean ignoreCondition) {
        this.trail_title = trail_title;
        this.date = date;
        this.type = type;
        this.time = time;
        this.VariesData = variesData;
        if (ID != null){
            this.ID = ID;
        }
        else{
            this.ID = TrailsDatabaseController.generateTrailsId();
        }
        this.location = location;
        this.ignoreCondition = ignoreCondition;
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

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
