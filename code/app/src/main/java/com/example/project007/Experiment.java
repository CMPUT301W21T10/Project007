package com.example.project007;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class Experiment implements Serializable {
    private String name;
    private String description;
    private String date;
    private String type;
    private final Integer id;
    private final String userId;
    private Integer[] trailsId;

    public Experiment(String name, String description, String date, String type, @Nullable Integer id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        if (id != null){
            this.id = id;
        }
        else{
            this.id = DatabaseController.generateExperimentId();
        }
        this.userId = DatabaseController.getUserId();
    }

    public String getUserId() {
        return userId;
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

    public Integer getId() {
        return id;
    }

    public Integer[] getTrailsId() {
        return trailsId;
    }

    public void setTrailsId(Integer[] trailsId) {
        this.trailsId = trailsId;
    }


}
