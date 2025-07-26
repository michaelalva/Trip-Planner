package com.tco.misc;

public class VincentyCalculator implements DistanceCalculator {

    public VincentyCalculator() {
    }

    @Override
    public long between(GeographicCoordinate from, GeographicCoordinate to, double earthRadius) {

        double fromLatitude = from.latRadians();
        double fromLongitude = from.lonRadians();
        double toLatitude = to.latRadians();
        double toLongitude = to.lonRadians();

        double lonDifference = Math.abs(toLongitude - fromLongitude);

        double numerator = Math.sqrt(
                Math.pow((Math.cos(toLatitude) * Math.sin(lonDifference)), 2) +
                        Math.pow((Math.cos(fromLatitude) * Math.sin(toLatitude) - Math.sin(fromLatitude)
                                * Math.cos(toLatitude) * Math.cos(lonDifference)), 2));

        double denominator = Math.sin(fromLatitude) * Math.sin(toLatitude) +
                Math.cos(fromLatitude) * Math.cos(toLatitude) * Math.cos(lonDifference);

        double deltaSigma = Math.atan2(numerator, denominator);

        return Math.round(deltaSigma * earthRadius);
    }
}
