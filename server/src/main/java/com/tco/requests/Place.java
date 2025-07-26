package com.tco.requests;

import java.util.HashMap;
import com.tco.misc.GeographicCoordinate;

public class Place extends HashMap<String, String> implements GeographicCoordinate {

    public Place() {
        super();
    }

    public Place(String lat, String lon) {
        super();
        this.put("latitude", lat);
        this.put("longitude", lon);
    }

    public double latDegrees() {
        return strToDouble(this.get("latitude"));
    }

    public double lonDegrees() {
        return strToDouble(this.get("longitude"));
    }

    public double latRadians() {
        return degreesToRadians(latDegrees());
    }

    public double lonRadians() {
        return degreesToRadians(lonDegrees());
    }

    private double degreesToRadians(double num) {
        return num * (Math.PI / 180.0);
    }

    private double strToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given lat/lon value is not a valid double.");
        }
    }
}
