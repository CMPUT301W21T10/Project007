package com.example.project007;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class Experiment implements Serializable {
    private String name;
    private String description;
    private String date;
    private String type;
    private Integer id;
    private String userId;
    private ArrayList<String> trailsId = new ArrayList<String>();
    private ArrayList<String> subscriptionId = new ArrayList<String>();
    private boolean requireLocation;
    private boolean condition;
    private Integer minimumTrails;
    private String region;

    public Experiment() {
    }

    public Experiment(String name, String description, String date, String type,
                      @Nullable Integer id, @Nullable ArrayList<String> trails, @Nullable ArrayList<String> subscriptionId,
                      boolean requireLocation, boolean condition, Integer minimumTrails, String region) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        this.userId = DatabaseController.getUserId();

        if (id != null){
            this.id = id;
        }
        else{
            this.id = DatabaseController.generateExperimentId();
        }
        if (trails != null){
            this.trailsId = trails;
        }
        if (subscriptionId != null){
            this.subscriptionId = subscriptionId;
        }
        this.requireLocation = requireLocation;
        this.condition = condition;
        this.minimumTrails = minimumTrails;
        this.region = region;
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

    public ArrayList<String> getTrails() {
        return trailsId;
    }

    public void setTrails(ArrayList<String> trails) {
        this.trailsId = trails;
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

    public ArrayList<String> getTrailsId() {
        return trailsId;
    }

    public void setTrailsId(ArrayList<String> trails) {
        this.trailsId = trails;
    }

    public ArrayList<String> getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(ArrayList<String> subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public boolean isRequireLocation() {
        return requireLocation;
    }

    public void setRequireLocation(boolean requireLocation) {
        this.requireLocation = requireLocation;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public Integer getMinimumTrails() {
        return minimumTrails;
    }

    public void setMinimumTrails(Integer minimumTrails) {
        this.minimumTrails = minimumTrails;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
