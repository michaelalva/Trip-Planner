package com.tco.misc;

public class CosinesCalculator implements DistanceCalculator {

    public CosinesCalculator() {
    }

    @Override
    public long between(GeographicCoordinate from, GeographicCoordinate to, double earthRadius) {

        double fromLat = from.latRadians();
        double fromLon = from.lonRadians();
        double toLat = to.latRadians();
        double toLon = to.lonRadians();

        double deltaLon = toLon - fromLon;

        double a = Math.sin(fromLat) * Math.sin(toLat);
        double b = Math.cos(fromLat) * Math.cos(toLat);
        double c = Math.cos(deltaLon);

        double inner = a + (b * c);

        double finalVal = Math.acos(inner) * earthRadius;

        return Math.round(finalVal);
    }

}
