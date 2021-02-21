package com.example.jinglong_trialbook;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

// a single class use to give a frame of experiment
// save all value inside
public class Experiment implements Serializable {

    private String experimentName;
    private String experimentDescription;
    private String experimentDate;
    private boolean[] experimentTrails;

    public Experiment(String experimentName, String experimentDescription, String experimentDate, ArrayList<Boolean> experimentTrails) {
        this.experimentName = experimentName;
        this.experimentDescription = experimentDescription;
        this.experimentDate = experimentDate;
        this.experimentTrails = new boolean[experimentTrails.size()];

        for (int i = 0; i < experimentTrails.size(); i++) {

            this.experimentTrails[i] = experimentTrails.get(i) == Boolean.TRUE;
        }
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentDescription() {
        return experimentDescription;
    }

    public void setExperimentDescription(String experimentDescription) {
        this.experimentDescription = experimentDescription;
    }

    public String getExperimentDate() {
        return experimentDate;
    }

    public void setExperimentDate(String experimentDate) {
        this.experimentDate = experimentDate;
    }

    public ArrayList<Boolean> getExperimentTrails() {
        ArrayList<Boolean> result = new ArrayList<Boolean>();
        for (int i = 0; i < experimentTrails.length; i++) {
            if (experimentTrails[i]){
                result.add(Boolean.TRUE);
            }
            else{
                result.add(Boolean.FALSE);
            }
        }
        return result;
    }

    public void setExperimentTrails(ArrayList<Boolean> experimentTrails) {
        this.experimentTrails = new boolean[experimentTrails.size()];

        for (int i = 0; i < experimentTrails.size(); i++) {
            this.experimentTrails[i] = experimentTrails.get(i) == Boolean.TRUE;
        }
    }

    // compute the current success rate
    @SuppressLint("DefaultLocale")
    public String computeRate(){
        int trueTimes = 0, falseTimes = 0;
        for (boolean experimentTrail : experimentTrails) {
            if (experimentTrail == Boolean.TRUE) {
                trueTimes++;
            } else {
                falseTimes++;
            }
        }
        float midRate = ((float)trueTimes/(float)(trueTimes+falseTimes));
        double rate = Math.round(midRate * 10000.0) / 10000.0;
        DecimalFormat df = new DecimalFormat("#%");

        return df.format(rate);
    }
}
