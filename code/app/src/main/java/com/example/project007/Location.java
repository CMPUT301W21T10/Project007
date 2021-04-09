package com.example.project007;

import java.io.Serializable;

/**
 * This is Location Class
 * This class gives frame of a single Location
 */
public class Location implements Serializable {
    double longitude;
    double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }



    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
