package com.tco.misc;

public class HaversineCalculator implements DistanceCalculator {
    
    // private static final double EARTH_RADIUS_KM = 6371.0; // Default Earth radius in kilometers

    public HaversineCalculator() {}

    @Override
    public long between(GeographicCoordinate from, GeographicCoordinate to, double earthRadius) {

        double lat1 = from.latRadians();
        double lon1 = from.lonRadians();
        double lat2 = to.latRadians();
        double lon2 = to.lonRadians();

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(earthRadius * c);
    }
}
