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
    private ArrayList<Trails> trails = new ArrayList<Trails>();

    public Experiment(String name, String description, String date, String type, @Nullable Integer id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        if (id != null){
            this.id = id;
        }
        else{
            this.id = DatabaseController.generateId();
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

    public ArrayList<Trails> getTrails() {
        return trails;
    }

    public void setTrails(ArrayList<Trails> trails) {
        this.trails = trails;
    }

    public void addTrail(Trails trail) {
        this.trails.add(trail);
    }

}
